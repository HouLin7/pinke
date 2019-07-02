package com.gome.work.common.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.applibrary.activity.BaseActivity;
import com.gome.utils.AesAppUtils;
import com.gome.utils.CommonUtils;
import com.gome.utils.StringUtil;
import com.gome.work.common.R;
import com.gome.work.common.widget.TitleBarView;
import com.gome.work.core.utils.DateUtils;
import com.gome.work.core.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class CommonWebFragment extends Fragment {

    public final static int REQUEST_CODE_REQUEST_PERMISSION = 0x01;

    private final static String KEY_AES = "JdAgEAAoGBAOdC92";

    private final static String KEY_SHA1 = "JdAgEAAoGBAOdC92";

    public final static String EXTRA_HAS_TITLEBAR = "is.has.titlebar";

    public final static String EXTRA_URL = "extra.url";

    public final static String EXTRA_APP_KEY = "extra.app.key";

    public final static String EXTRA_APP_SECRET = "extra.app.secret";

    public final static String EXTRA_INJECT_JS_NAME = "extra.inject.js";

    private Map<String, Set<BaseActivity.IPermissionRequestCallback>> mPermissionCallbacks = new HashMap<>();

    protected WebView mWebView;

    private TextView mTxtViewError;

    private String mInitURL;

    private ProgressBar mLoadProgress;

    private String mAppKey = "";

    private String mAppSecret = "";

    /**
     * A object name to inject to webiew.
     */
    private String mJsObjectName = "";


    private Handler mBaseHandler = new Handler();

    private View mContentView;

    private TitleBarView titleBarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mContentView != null) {
            return mContentView;
        }
        mContentView = inflater.inflate(R.layout.fragment_common_web, container, false);

        mLoadProgress = (ProgressBar) mContentView.findViewById(R.id.pb_progress);
        mInitURL = getArguments().getString(EXTRA_URL);

        mInitURL = assembleSignInfo(mInitURL);

        mWebView = (WebView) mContentView.findViewById(R.id.webView);
        setWebView(mWebView);
        mWebView.loadUrl(mInitURL);
        mTxtViewError = (TextView) mContentView.findViewById(R.id.txtViewError);
        mTxtViewError.setVisibility(View.GONE);

        mTxtViewError.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mWebView.reload();
            }
        });

        titleBarView = mContentView.findViewById(R.id.title_view);
        titleBarView.setRightTextColor(R.color.red);

        titleBarView.setMidViewText(DateUtils.format(new Date(), "yyyy年MM月dd日"));
        return mContentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAppKey = getArguments().getString(EXTRA_APP_KEY);
        mAppSecret = getArguments().getString(EXTRA_APP_SECRET);
        if (TextUtils.isEmpty(mAppKey)) {
            mAppKey = KEY_AES;
        }
        if (TextUtils.isEmpty(mAppSecret)) {
            mAppSecret = KEY_SHA1;
        }

        mJsObjectName = getArguments().getString(EXTRA_INJECT_JS_NAME);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private String assembleSignInfo(String baseUrl) {
        List<Pair<String, String>> params = new ArrayList<>();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = StringUtil.getRandomChar(10);
        params.add(new Pair<String, String>("timestamp", timestamp));
        params.add(new Pair<String, String>("nonce", nonce));

        String tempData = timestamp + mAppSecret + nonce;
        String sign = AesAppUtils.getSHA1Digest(tempData);
        params.add(new Pair<String, String>("sign", sign));

        String token = SharedPreferencesHelper.getAccessToken();
        if (!TextUtils.isEmpty(token)) {
            token = token.replace("Bearer ", "");
            params.add(new Pair<String, String>("token", token));
        }

        if (baseUrl.contains("?")) {
            return baseUrl + "&" + CommonUtils.encodeUrl(params, false);
        } else {
            return baseUrl + "?" + CommonUtils.encodeUrl(params, false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_APP_KEY, mAppKey);
        outState.putString(EXTRA_APP_SECRET, mAppSecret);
        outState.putString(EXTRA_INJECT_JS_NAME, mJsObjectName);
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    protected void setWebView(WebView webView) {
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
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
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new MySystemWebViewClient());
        webView.setWebChromeClient(new MySystemChromeClient());

        String objName = TextUtils.isEmpty(mJsObjectName) ? "SmartNative" : mJsObjectName;
//        webView.addJavascriptInterface(new JsNativeImpl((BaseGomeWorkActivity) getActivity(), mWebView), objName);
    }


    protected void doShare(String title, String content, String targetUrl, String imgUrl, final String callback) {

    }

    protected boolean onHandleOpenNewWinowWithUrl(String url, String text) {
        return false;
    }

    protected boolean onHandleOverrideUrlLoading(WebView view, final String url) {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent arg2) {
        super.onActivityResult(requestCode, resultCode, arg2);
        if (resultCode == Activity.RESULT_OK) {
            String result = arg2.getStringExtra(Intent.EXTRA_RETURN_RESULT);
//            mWebView.loadUrl("javascript:" + mJsCallback + "('" + result + "')");
        }
    }

    public void requestPermission(final String permission, final BaseActivity.IPermissionRequestCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{permission}, REQUEST_CODE_REQUEST_PERMISSION);
                if (callback != null) {
                    Set<BaseActivity.IPermissionRequestCallback> sets = mPermissionCallbacks.get(permission);
                    if (sets == null) {
                        sets = new ArraySet<>();
                        mPermissionCallbacks.put(permission, sets);
                    }
                    sets.add(callback);
                }
                return;
            }
        }

        if (callback != null) {
            // 模拟异步回调，故延迟200毫秒
            mBaseHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.onFinished(permission, true);
                }
            }, 200);
        }
    }

    public class MySystemChromeClient extends android.webkit.WebChromeClient {

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            if (isDetached()) {
                return true;
            }
            AlertDialog.Builder b2 = new AlertDialog.Builder(getActivity()).setMessage(message)
                    .setPositiveButton(getString(R.string.certain), new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    });
            b2.setCancelable(false);
            b2.create();
            b2.show();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            if (isDetached()) {
                return true;
            }
            AlertDialog.Builder b2 = new AlertDialog.Builder(getActivity()).setMessage(message)
                    .setPositiveButton(getString(R.string.certain), new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    }).setNegativeButton(R.string.cancel, new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    });
            b2.setCancelable(false);
            b2.create();
            b2.show();
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
//            if (!TextUtils.isEmpty(title)
//                    && TextUtils.isEmpty(mTitleName)) {
//                setTitle(title);
//            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                mLoadProgress.setVisibility(View.GONE);
            } else {
                if (View.GONE == mLoadProgress.getVisibility()) {
                    mLoadProgress.setVisibility(View.VISIBLE);
                }
                mLoadProgress.setProgress(newProgress);
            }
        }

        @Override
        public boolean onJsPrompt(WebView arg3, String arg4, String arg5, String arg6, JsPromptResult arg7) {
            return super.onJsPrompt(arg3, arg4, arg5, arg6, arg7);
        }

    }

    public class MySystemWebViewClient extends android.webkit.WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mTxtViewError.setVisibility(View.GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {
            if (url != null && url.toLowerCase(Locale.US).startsWith("weixin://")) {
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
            if (onHandleOverrideUrlLoading(view, url)) {
                return true;
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mTxtViewError.setVisibility(View.VISIBLE);

        }

    }


}
