package com.gome.work.common.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.UiThread;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.*;
import android.webkit.WebSettings.RenderPriority;
import com.gome.core.greendao.AppItemBeanDao;
import com.gome.utils.AesAppUtils;
import com.gome.utils.CommonUtils;
import com.gome.utils.StringUtil;
import com.gome.utils.ToastUtil;
import com.gome.work.common.R;
import com.gome.work.common.activity.BaseGomeWorkActivity;
import com.gome.work.common.databinding.ActivityCommonWebBinding;
import com.gome.work.common.webview.jsbridge.JsNativeImpl;
import com.gome.work.common.webview.model.JsMenuInfo;
import com.gome.work.core.Constants;
import com.gome.work.core.model.AccessTokenInfo;
import com.gome.work.core.model.UserInfo;
import com.gome.work.core.model.appmarket.AppItemBean;
import com.gome.work.core.utils.GomeBpUtils;
import com.gome.work.core.utils.SharedPreferencesHelper;

import java.io.File;
import java.util.*;


/**
 * 通用h5容器类
 */
public class CommonWebActivity extends BaseGomeWorkActivity {

    public final static String EXTRA_HAS_TITLE_BAR = "is.has.title.bar";

    public final static String EXTRA_TITLE_NAME = "extra.title.name";

    public final static String EXTRA_URL = "extra.url";

    public final static String EXTRA_APP_KEY = "extra.app.key";

    public final static String EXTRA_APP_SECRET = "extra.app.secret";

    public final static String EXTRA_REFERER = "extra.referer";

    public final static String EXTRA_INJECT_JS_NAME = "extra.inject.js";

    public final static String EXTRA_IS_ASSEMBLE_SIGN = "is.assemble.sign.flag";

    public final static String EXTRA_APP_ITEM_DATA = "extra.appitem.info";

    private String mInitURL;

    private String mAppKey = "";

    private String mAppSecret = "";

    /**
     * A object name to inject to webiew.
     */
    private String mJsObjectName = "";

    private String mTitleName = "";

    private boolean isShowTitleBar;

    private Map<String, String> mReferers;

    private BaseWebChromeClient mBaseWebChromeClient;

    private JsNativeImpl mJsNativeImpl;

    private ActivityCommonWebBinding mBinding;

    private AppItemBean mAppItemBean;

    public static void startActivity(Context context, AppItemBean appItemBean) {
        Intent intent = new Intent(context, CommonWebActivity.class);
        intent.putExtra(EXTRA_IS_ASSEMBLE_SIGN, !"0".equals(appItemBean.isForceVerify));
        intent.putExtra(EXTRA_APP_KEY, appItemBean.appId);
        intent.putExtra(EXTRA_APP_SECRET, appItemBean.appSecret);
        intent.putExtra(EXTRA_URL, appItemBean.homePageUrl);
        intent.putExtra(EXTRA_TITLE_NAME, appItemBean.name);
        intent.putExtra(EXTRA_APP_ITEM_DATA, appItemBean);
        if (!TextUtils.isEmpty(appItemBean.homePageUrl)) {
            context.startActivity(intent);
        } else {
            ToastUtil.showToast(context, "暂不支持该应用");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_common_web);
        mBinding.graffitiPanel.setVisibility(View.GONE);
        mAppKey = getIntent().getStringExtra(EXTRA_APP_KEY);
        mAppSecret = getIntent().getStringExtra(EXTRA_APP_SECRET);

        mJsObjectName = getIntent().getStringExtra(EXTRA_INJECT_JS_NAME);
        isShowTitleBar = getIntent().getBooleanExtra(EXTRA_HAS_TITLE_BAR, true);

        mBinding.includeToolbar.myToolBar.setVisibility(isShowTitleBar ? View.VISIBLE : View.GONE);

        mTitleName = getIntent().getStringExtra(EXTRA_TITLE_NAME);
        mBinding.includeToolbar.myToolBar.bindActivity(this);
        mBinding.includeToolbar.myToolBar.setMiddleTitle(mTitleName);
        mBinding.includeToolbar.myToolBar.showCloseIcon(this);

        mInitURL = getIntent().getStringExtra(EXTRA_URL);
        boolean flag = getIntent().getBooleanExtra(EXTRA_IS_ASSEMBLE_SIGN, true);
        if (flag && !TextUtils.isEmpty(mAppSecret)) {
            mInitURL = assembleSignInfo(mInitURL);
        }

        mReferers = (Map<String, String>) getIntent().getSerializableExtra(EXTRA_REFERER);
        setWebView(mBinding.webView);
        mBinding.webView.loadUrl(mInitURL);

        mBinding.txtViewError.setVisibility(View.GONE);

        mBinding.txtViewError.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mBinding.webView.reload();
            }
        });

        mBinding.tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.graffitiPanel.setVisibility(View.GONE);
            }
        });
        mBinding.tvOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.llControl.setVisibility(View.GONE);
                Bitmap data = mBinding.graffitiView.getBitmap();
                mBinding.graffitiPanel.setVisibility(View.GONE);
            }
        });

        mAppItemBean = (AppItemBean) getIntent().getSerializableExtra(EXTRA_APP_ITEM_DATA);

        if (mAppItemBean == null && mAppKey != null) {
            mAppItemBean = getMDaoUtil().getAppItemBeanDao().queryBuilder()
                    .where(AppItemBeanDao.Properties.AppId.eq(mAppKey)).unique();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAppItemBean != null) {
            if (isStoreDomainAccount()) {
                GomeBpUtils.enterPage(mAppItemBean.appId, "", buildBusinessTagData());
            }
        }
    }

    private boolean isStoreDomainAccount() {
        String account = SharedPreferencesHelper.getString(Constants.PreferKeys.ACCOUNT);
        return account.endsWith("@gome.inc") || account.endsWith("@DQ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAppItemBean != null) {
            if (isStoreDomainAccount()) {
                GomeBpUtils.leavePage(mAppItemBean.appId, "", buildBusinessTagData());
            }
        }
    }

    protected Map<String, Object> buildBusinessTagData() {
        AccessTokenInfo accessTokenInfo = SharedPreferencesHelper.getAccessTokenInfo();
        UserInfo userInfoBean = SharedPreferencesHelper.getUserDetailInfo();
        Map<String, Object> result = new HashMap<>();
        if (userInfoBean != null) {
            Map<String, Object> department = new HashMap<>();
            result.put("department", department);
        } else {
            if (accessTokenInfo != null) {
//                result.put("userId", accessTokenInfo.userInfo.getEmployeeId());
            }
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public BaseWebChromeClient getWebChromeClient() {
        return mBaseWebChromeClient;
    }


    private String assembleSignInfo(String baseUrl) {
//        String timestamp1 = "1555305619863";
//        String nonce1 = "O1HWHHJEU5O9SOMX";
//        String tempData1 = timestamp1 + mAppSecret + nonce1;
//        String sign1 = AesAppUtils.getSHA1Digest(tempData1);
        List<Pair<String, String>> params = new ArrayList<>();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = StringUtil.getRandomChar(16);
        params.add(new Pair<>("timestamp", timestamp));
        params.add(new Pair<>("nonce", nonce));

        String tempData = timestamp + mAppSecret + nonce;
        String sign = AesAppUtils.getSHA1Digest(tempData);
        params.add(new Pair<>("sign", sign));

        String token = SharedPreferencesHelper.getAccessToken();
        if (!TextUtils.isEmpty(token)) {
            token = token.replace("Bearer ", "");
            params.add(new Pair<>("token", token));
        }
        if (baseUrl.contains("?")) {
            return baseUrl + "&" + CommonUtils.encodeUrl(params, false);
        } else {
            return baseUrl + "?" + CommonUtils.encodeUrl(params, false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(EXTRA_APP_KEY, mAppKey);
        outState.putString(EXTRA_APP_SECRET, mAppSecret);
        outState.putBoolean(EXTRA_HAS_TITLE_BAR, isShowTitleBar);
        outState.putString(EXTRA_INJECT_JS_NAME, mJsObjectName);
    }


    /**
     * 截屏涂鸦
     */
    public void showGraffitiView() {
        mBinding.llControl.setVisibility(View.VISIBLE);
        mBinding.graffitiPanel.setVisibility(View.VISIBLE);
        if (mBinding.graffitiView.getPBList().size() > 0) {
            mBinding.graffitiView.getPBList().clear();
            mBinding.graffitiView.invalidate();
        }
    }

    protected void setWebView(WebView webView) {
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        String oldUA = webView.getSettings().getUserAgentString();
        String newUA = oldUA + " smartoffice";
        webView.getSettings().setUserAgentString(newUA);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setBackgroundColor(Color.WHITE);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setRenderPriority(RenderPriority.HIGH);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.setWebViewClient(new MySystemWebViewClient());
        webView.setDownloadListener(new MyDownloadListener(this, mAppKey));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBaseWebChromeClient = new CommonChromeClient(this);
        } else {
            mBaseWebChromeClient = new LegacyChromeClient(this);
        }
        webView.setWebChromeClient(mBaseWebChromeClient);
        String objName = TextUtils.isEmpty(mJsObjectName) ? "SmartNative" : mJsObjectName;
        mJsNativeImpl = new JsNativeImpl(this, mBinding.webView, mAppKey, mAppSecret);

        webView.addJavascriptInterface(mJsNativeImpl, objName);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

    }


    @Override
    protected void onImageGetResult(boolean isSuccess, Uri uri, File file) {
        if (mBaseWebChromeClient instanceof CommonChromeClient) {
            mBaseWebChromeClient.onActivityResult(isSuccess, new Uri[]{uri});
        } else {
            mBaseWebChromeClient.onActivityResult(isSuccess, uri);
        }

        if (mJsNativeImpl != null) {
            mJsNativeImpl.onImageGetResult(isSuccess, uri, file);
        }
    }


    public void onFileGetResult(boolean isSuccess, File file) {
        Uri uri = getUriForFile(file);
        if (mBaseWebChromeClient instanceof CommonChromeClient) {
            mBaseWebChromeClient.onActivityResult(isSuccess, new Uri[]{uri});
        } else {
            mBaseWebChromeClient.onActivityResult(isSuccess, uri);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (mBinding != null) {
            mBinding.includeToolbar.myToolBar.setMiddleTitle(title);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mJsNativeImpl != null) {
            mJsNativeImpl.onActivityResult(requestCode, resultCode, data);
        }
    }


    @UiThread
    public void addMenuList(final List<JsMenuInfo> jsMenuInfos, final MenuItem.OnMenuItemClickListener listener) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Menu menu = mBinding.includeToolbar.myToolBar.getMenu();
                menu.clear();
                int index = 0;
                for (JsMenuInfo item : jsMenuInfos) {
                    menu.add(0, item.id, index, item.name);
                    index++;
                }
                if (jsMenuInfos.size() == 1) {
                    menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
                }
                mBinding.includeToolbar.myToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (listener != null) {
                            listener.onMenuItemClick(menuItem);
                        }
                        return true;
                    }
                });
            }
        });

    }


    /**
     * 针对5.0以及以上的版本
     */
    public class CommonChromeClient extends BaseWebChromeClient<Uri[]> {

        public CommonChromeClient(FragmentActivity activity) {
            super(activity);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (TextUtils.isEmpty(mTitleName)) {
                setTitle(title);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            mUploadMessage = filePathCallback;
            String[] types = fileChooserParams.getAcceptTypes();
            boolean isSelectImage = false;
            for (String item : types) {
                if (item.contains("image")) {
                    isSelectImage = true;
                    break;
                }
            }
            if (isSelectImage) {
                showImagePickerWindow(false, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        freeUploadCallback();
                    }
                });
            } else {
                mJsNativeImpl.handleFileChoose();
            }
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                mBinding.pbProgress.setVisibility(View.GONE);
            } else {
                if (View.GONE == mBinding.pbProgress.getVisibility()) {
                    mBinding.pbProgress.setVisibility(View.VISIBLE);
                }
                mBinding.pbProgress.setProgress(newProgress);
            }
        }
    }

    /**
     * Android API 4.1--4.4
     */
    public class LegacyChromeClient extends BaseWebChromeClient<Uri> {

        public LegacyChromeClient(FragmentActivity activity) {
            super(activity);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (TextUtils.isEmpty(mTitleName)) {
                setTitle(title);
            }
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            if (acceptType.contains("image")) {
                showImagePickerWindow(false, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        freeUploadCallback();
                    }
                });
            } else {
                mJsNativeImpl.handleFileChoose();
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                mBinding.pbProgress.setVisibility(View.GONE);
            } else {
                if (View.GONE == mBinding.pbProgress.getVisibility()) {
                    mBinding.pbProgress.setVisibility(View.VISIBLE);
                }
                mBinding.pbProgress.setProgress(newProgress);
            }
        }
    }

    public class MySystemWebViewClient extends WebViewClient {

        private boolean chkMySSLCNCert(SslCertificate cert) {
//            Bundle bundle = SslCertificate.saveState(cert);
//            byte[] bytes = bundle.getByteArray("x509-certificate");
//            if (bytes != null) {
//                try {
//                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
//                    Certificate ca = cf.generateCertificate(new ByteArrayInputStream(bytes));
//                    InputStream is = getAssets().open("server_release.cer");
//                    Certificate trustCert = cf.generateCertificate(is);
//                    return ca.equals(trustCert);
//                } catch (Exception Ex) {
//                    Ex.printStackTrace();
//                }
//            }
            return true;
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mBinding.txtViewError.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if (error.getPrimaryError() == SslError.SSL_DATE_INVALID  // 日期不正确
                    || error.getPrimaryError() == SslError.SSL_EXPIRED // 日期不正确
                    || error.getPrimaryError() == SslError.SSL_INVALID // webview BUG
                    || error.getPrimaryError() == SslError.SSL_UNTRUSTED) { // 根证书丢失
                if (chkMySSLCNCert(error.getCertificate())) {
                    handler.proceed();  // 如果证书一致，忽略错误
                }
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            if (url.toLowerCase(Locale.US).startsWith("weixin://")
                    || url.toLowerCase(Locale.US).startsWith("gomeplusapp://")) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent();
                intent.setData(uri);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            if (url.toLowerCase(Locale.US).startsWith("smartoffice://")) {
                return true;
            }

            if (onHandleOverrideUrlLoading(view, url)) {
                return true;
            }
            if (mReferers != null) {
                view.loadUrl(url, mReferers);
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {
            if (url.toLowerCase(Locale.US).startsWith("weixin://")) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent();
                intent.setData(uri);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            if (url.toLowerCase(Locale.US).startsWith("smartoffice://")) {
                return true;
            }

            if (onHandleOverrideUrlLoading(view, url)) {
                return true;
            }
            if (mReferers != null) {
                view.loadUrl(url, mReferers);
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mBinding.txtViewError.setVisibility(View.VISIBLE);
        }
    }

    private boolean onHandleOverrideUrlLoading(WebView view, String url) {
        return false;
    }


    @Override
    public void onBackPressed() {
        if (mJsNativeImpl != null) {
            if (mJsNativeImpl.onBackPressed()) {
                return;
            }
        }
        if (mBinding.webView.canGoBack()) {
            mBinding.webView.goBack();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mJsNativeImpl.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
