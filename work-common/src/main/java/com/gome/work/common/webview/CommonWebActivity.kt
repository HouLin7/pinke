package com.gome.work.common.webview

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.net.http.SslCertificate
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.RequiresApi
import android.support.annotation.UiThread
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Pair
import android.view.MenuItem
import android.view.View
import android.webkit.*
import android.webkit.WebSettings.RenderPriority
import com.gome.core.greendao.AppItemBeanDao
import com.gome.utils.AesAppUtils
import com.gome.utils.CommonUtils
import com.gome.utils.StringUtil
import com.gome.utils.ToastUtil
import com.gome.work.common.R
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.common.webview.jsbridge.JsNativeImpl
import com.gome.work.common.webview.model.JsMenuInfo
import com.gome.work.core.Constants
import com.gome.work.core.model.appmarket.AppItemBean
import com.gome.work.core.utils.GomeBpUtils
import com.gome.work.core.utils.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_common_web.*
import java.io.File
import java.util.*


/**
 * 通用h5容器类
 */
class CommonWebActivity : BaseGomeWorkActivity() {

    private var mInitURL: String? = null

    private var mAppKey: String? = ""

    private var mAppSecret = ""

    /**
     * A object name to inject to webiew.
     */
    private var mJsObjectName = ""

    private var mTitleName = ""

    private var isShowTitleBar: Boolean = false

    private var mReferers: Map<String, String>? = null

    var commonChromeClient: CommonChromeClient? = null

    var legacyChromeClient: LegacyChromeClient? = null

    private var mJsNativeImpl: JsNativeImpl? = null

    private var mAppItemBean: AppItemBean? = null

    private val isStoreDomainAccount: Boolean
        get() {
            val account = SharedPreferencesHelper.getString(Constants.PreferKeys.ACCOUNT)
            return account.endsWith("@gome.inc") || account.endsWith("@DQ")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_web)
        graffiti_view.visibility = View.GONE
        mAppKey = intent.getStringExtra(EXTRA_APP_KEY)
        mAppSecret = intent.getStringExtra(EXTRA_APP_SECRET)

        mJsObjectName = intent.getStringExtra(EXTRA_INJECT_JS_NAME)
        isShowTitleBar = intent.getBooleanExtra(EXTRA_HAS_TITLE_BAR, true)

        getCustomToolbar(include_toolbar).visibility = if (isShowTitleBar) View.VISIBLE else View.GONE

        mTitleName = intent.getStringExtra(EXTRA_TITLE_NAME)
        getCustomToolbar(include_toolbar).bindActivity(this)

        getCustomToolbar(include_toolbar).setMiddleTitle(mTitleName)
        getCustomToolbar(include_toolbar).showCloseIcon(this)

        mInitURL = intent.getStringExtra(EXTRA_URL)
        val flag = intent.getBooleanExtra(EXTRA_IS_ASSEMBLE_SIGN, true)
        if (flag && !TextUtils.isEmpty(mAppSecret)) {
            mInitURL = assembleSignInfo(mInitURL)
        }

        mReferers = intent.getSerializableExtra(EXTRA_REFERER) as Map<String, String>
        setWebView(webView)
        webView.loadUrl(mInitURL)

        txtViewError.visibility = View.GONE

        txtViewError.setOnClickListener { webView.reload() }

        tv_cancel.setOnClickListener { graffiti_panel.visibility = View.GONE }
        tv_ok.setOnClickListener {
            ll_control.visibility = View.GONE
            val data = graffiti_view.bitmap
            graffiti_panel.visibility = View.GONE
        }

        mAppItemBean = intent.getSerializableExtra(EXTRA_APP_ITEM_DATA) as AppItemBean

        if (mAppItemBean == null && mAppKey != null) {
            mAppItemBean = mDaoUtil.appItemBeanDao.queryBuilder()
                .where(AppItemBeanDao.Properties.AppId.eq(mAppKey)).unique()
        }
    }

    override fun onStart() {
        super.onStart()
        if (mAppItemBean != null) {
            if (isStoreDomainAccount) {
                GomeBpUtils.enterPage(mAppItemBean!!.appId, "", buildBusinessTagData())
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (mAppItemBean != null) {
            if (isStoreDomainAccount) {
                GomeBpUtils.leavePage(mAppItemBean!!.appId, "", buildBusinessTagData())
            }
        }
    }

    protected fun buildBusinessTagData(): Map<String, Any> {
        val accessTokenInfo = SharedPreferencesHelper.getAccessTokenInfo()
        val userInfoBean = SharedPreferencesHelper.getUserDetailInfo()
        val result = HashMap<String, Any>()
        if (userInfoBean != null) {
            val department = HashMap<String, Any>()
            result["department"] = department
        } else {
            if (accessTokenInfo != null) {
                //                result.put("userId", accessTokenInfo.userInfo.getEmployeeId());
            }
        }
        return result
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    private fun assembleSignInfo(baseUrl: String?): String {
        //        String timestamp1 = "1555305619863";
        //        String nonce1 = "O1HWHHJEU5O9SOMX";
        //        String tempData1 = timestamp1 + mAppSecret + nonce1;
        //        String sign1 = AesAppUtils.getSHA1Digest(tempData1);
        val params = ArrayList<Pair<String, String>>()
        val timestamp = System.currentTimeMillis().toString()
        val nonce = StringUtil.getRandomChar(16)
        params.add(Pair("timestamp", timestamp))
        params.add(Pair("nonce", nonce))

        val tempData = timestamp + mAppSecret + nonce
        val sign = AesAppUtils.getSHA1Digest(tempData)
        params.add(Pair("sign", sign))

        var token = SharedPreferencesHelper.getAccessToken()
        if (!TextUtils.isEmpty(token)) {
            token = token.replace("Bearer ", "")
            params.add(Pair("token", token))
        }
        return if (baseUrl!!.contains("?")) {
            baseUrl + "&" + CommonUtils.encodeUrl(params, false)
        } else {
            baseUrl + "?" + CommonUtils.encodeUrl(params, false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(EXTRA_APP_KEY, mAppKey)
        outState.putString(EXTRA_APP_SECRET, mAppSecret)
        outState.putBoolean(EXTRA_HAS_TITLE_BAR, isShowTitleBar)
        outState.putString(EXTRA_INJECT_JS_NAME, mJsObjectName)
    }


    /**
     * 截屏涂鸦
     */
    fun showGraffitiView() {
        ll_control.visibility = View.VISIBLE
        graffiti_panel.visibility = View.VISIBLE
        if (graffiti_view.pbList.size > 0) {
            graffiti_view.pbList.clear()
            graffiti_view.invalidate()
        }
    }

    @SuppressLint("JavascriptInterface")
    protected fun setWebView(webView: WebView) {
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        val oldUA = webView.settings.userAgentString
        val newUA = "$oldUA smartoffice"
        webView.settings.userAgentString = newUA
        webView.settings.domStorageEnabled = true
        webView.setBackgroundColor(Color.WHITE)
        webView.settings.allowFileAccess = true
        webView.settings.setAppCacheEnabled(true)
        webView.settings.setGeolocationEnabled(true)
        webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webView.settings.setRenderPriority(RenderPriority.HIGH)
        webView.settings.setSupportZoom(false)
        webView.settings.builtInZoomControls = true
        webView.settings.useWideViewPort = true
        webView.settings.allowUniversalAccessFromFileURLs = true
        webView.webViewClient = MySystemWebViewClient()
        webView.setDownloadListener(MyDownloadListener(this, mAppKey))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            commonChromeClient = CommonChromeClient(this)
            webView.webChromeClient = commonChromeClient
        } else {
            legacyChromeClient = LegacyChromeClient(this)
            webView.webChromeClient = legacyChromeClient
        }
        val objName = if (TextUtils.isEmpty(mJsObjectName)) "SmartNative" else mJsObjectName
        mJsNativeImpl = JsNativeImpl(this, webView, mAppKey, mAppSecret)

        webView.addJavascriptInterface(mJsNativeImpl, objName)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

    }


    override fun onImageGetResult(isSuccess: Boolean, uri: Uri?, file: File?) {
        if (commonChromeClient != null) {
            commonChromeClient!!.onActivityResult(isSuccess, arrayOf(uri!!))
        } else {
            legacyChromeClient!!.onActivityResult(isSuccess, uri)
        }

        if (mJsNativeImpl != null) {
            mJsNativeImpl!!.onImageGetResult(isSuccess, uri, file)
        }
    }


    fun onFileGetResult(isSuccess: Boolean, file: File) {
        val uri = getUriForFile(file)
        if (commonChromeClient != null) {
            commonChromeClient!!.onActivityResult(isSuccess, arrayOf(uri))
        } else {
            legacyChromeClient!!.onActivityResult(isSuccess, uri)
        }
    }

    override fun setTitle(title: CharSequence) {
        if (include_toolbar != null) {
            getCustomToolbar(include_toolbar).setMiddleTitle(title)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (mJsNativeImpl != null) {
            mJsNativeImpl!!.onActivityResult(requestCode, resultCode, data)
        }
    }


    fun freeUploadCallback() {
        commonChromeClient?.let {
            it.freeUploadCallback()
        }
        legacyChromeClient?.let {
            it.freeUploadCallback()
        }
    }


    @UiThread
    fun addMenuList(jsMenuInfos: List<JsMenuInfo>, listener: MenuItem.OnMenuItemClickListener?) {
        runOnUiThread {
            val menu = getCustomToolbar(include_toolbar).menu
            menu.clear()
            var index = 0
            for (item in jsMenuInfos) {
                menu.add(0, item.id, index, item.name)
                index++
            }
            if (jsMenuInfos.size == 1) {
                menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS or MenuItem.SHOW_AS_ACTION_WITH_TEXT)
            }
            getCustomToolbar(include_toolbar).setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { menuItem ->
                listener?.onMenuItemClick(menuItem)
                true
            })
        }

    }


    /**
     * 针对5.0以及以上的版本
     */
    inner class CommonChromeClient(activity: FragmentActivity) : BaseWebChromeClient<Array<Uri>>(activity) {

        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
            if (TextUtils.isEmpty(mTitleName)) {
                setTitle(title)
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        override fun onShowFileChooser(
            webView: WebView,
            filePathCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: WebChromeClient.FileChooserParams
        ): Boolean {
            mUploadMessage = filePathCallback
            val types = fileChooserParams.getAcceptTypes()
            var isSelectImage = false
            for (item in types) {
                if (item.contains("image")) {
                    isSelectImage = true
                    break
                }
            }
            if (isSelectImage) {
                showImagePickerWindow(false, DialogInterface.OnCancelListener { freeUploadCallback() })
            } else {
                mJsNativeImpl!!.handleFileChoose()
            }
            return true
        }

        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if (newProgress == 100) {
                pb_progress.visibility = View.GONE
            } else {
                if (View.GONE == pb_progress.visibility) {
                    pb_progress.visibility = View.VISIBLE
                }
                pb_progress.progress = newProgress
            }
        }
    }

    /**
     * Android API 4.1--4.4
     */
    inner class LegacyChromeClient(activity: FragmentActivity) : BaseWebChromeClient<Uri>(activity) {

        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
            if (TextUtils.isEmpty(mTitleName)) {
                setTitle(title)
            }
        }

        fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String, capture: String) {
            mUploadMessage = uploadMsg
            if (acceptType.contains("image")) {
                showImagePickerWindow(false, DialogInterface.OnCancelListener { freeUploadCallback() })
            } else {
                mJsNativeImpl!!.handleFileChoose()
            }
        }

        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if (newProgress == 100) {
                pb_progress.visibility = View.GONE
            } else {
                if (View.GONE == pb_progress.visibility) {
                    pb_progress.visibility = View.VISIBLE
                }
                pb_progress.progress = newProgress
            }
        }
    }

    inner class MySystemWebViewClient : WebViewClient() {

        private fun chkMySSLCNCert(cert: SslCertificate): Boolean {
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
            return true
        }


        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            super.onPageStarted(view, url, favicon)
            txtViewError.visibility = View.GONE
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            if (error.primaryError == SslError.SSL_DATE_INVALID  // 日期不正确

                || error.primaryError == SslError.SSL_EXPIRED // 日期不正确

                || error.primaryError == SslError.SSL_INVALID // webview BUG

                || error.primaryError == SslError.SSL_UNTRUSTED
            ) { // 根证书丢失
                if (chkMySSLCNCert(error.certificate)) {
                    handler.proceed()  // 如果证书一致，忽略错误
                }
            }
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val url = request.url.toString()
            if (url.toLowerCase(Locale.US).startsWith("weixin://") || url.toLowerCase(Locale.US).startsWith("gomeplusapp://")) {
                val uri = Uri.parse(url)
                val intent = Intent()
                intent.data = uri
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return true
            }
            if (url.toLowerCase(Locale.US).startsWith("smartoffice://")) {
                return true
            }

            if (onHandleOverrideUrlLoading(view, url)) {
                return true
            }
            if (mReferers != null) {
                view.loadUrl(url, mReferers)
            } else {
                view.loadUrl(url)
            }
            return true
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.toLowerCase(Locale.US).startsWith("weixin://")) {
                val uri = Uri.parse(url)
                val intent = Intent()
                intent.data = uri
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return true
            }
            if (url.toLowerCase(Locale.US).startsWith("smartoffice://")) {
                return true
            }

            if (onHandleOverrideUrlLoading(view, url)) {
                return true
            }
            if (mReferers != null) {
                view.loadUrl(url, mReferers)
            } else {
                view.loadUrl(url)
            }
            return true
        }

        override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
            super.onReceivedError(view, errorCode, description, failingUrl)
            txtViewError.visibility = View.VISIBLE
        }
    }

    private fun onHandleOverrideUrlLoading(view: WebView, url: String): Boolean {
        return false
    }


    override fun onBackPressed() {
        if (mJsNativeImpl != null) {
            if (mJsNativeImpl!!.onBackPressed()) {
                return
            }
        }
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        mJsNativeImpl!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {

        val EXTRA_HAS_TITLE_BAR = "is.has.title.bar"

        val EXTRA_TITLE_NAME = "extra.title.name"

        val EXTRA_URL = "extra.url"

        val EXTRA_APP_KEY = "extra.app.key"

        val EXTRA_APP_SECRET = "extra.app.secret"

        val EXTRA_REFERER = "extra.referer"

        val EXTRA_INJECT_JS_NAME = "extra.inject.js"

        val EXTRA_IS_ASSEMBLE_SIGN = "is.assemble.sign.flag"

        val EXTRA_APP_ITEM_DATA = "extra.appitem.info"

        fun startActivity(context: Context, appItemBean: AppItemBean) {
            val intent = Intent(context, CommonWebActivity::class.java)
            intent.putExtra(EXTRA_IS_ASSEMBLE_SIGN, "0" != appItemBean.isForceVerify)
            intent.putExtra(EXTRA_APP_KEY, appItemBean.appId)
            intent.putExtra(EXTRA_APP_SECRET, appItemBean.appSecret)
            intent.putExtra(EXTRA_URL, appItemBean.homePageUrl)
            intent.putExtra(EXTRA_TITLE_NAME, appItemBean.name)
            intent.putExtra(EXTRA_APP_ITEM_DATA, appItemBean)
            if (!TextUtils.isEmpty(appItemBean.homePageUrl)) {
                context.startActivity(intent)
            } else {
                ToastUtil.showToast(context, "暂不支持该应用")
            }
        }
    }
}
