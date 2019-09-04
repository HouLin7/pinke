package com.gome.work.common.webview.jsbridge;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.gome.utils.ToastUtil;
import com.gome.work.common.activity.BaseGomeWorkActivity;
import com.gome.work.common.webview.CommonWebActivity;
import com.gome.work.common.webview.MyJavascriptInterface;
import com.gome.work.common.webview.jsbridge.runner.JsActionbarRunner;
import com.gome.work.common.webview.jsbridge.runner.JsCommonRunner;
import com.gome.work.common.webview.jsbridge.runner.JsFileRunner;
import com.gome.work.common.webview.jsbridge.runner.JsImageRunner;
import com.gome.work.common.webview.jsbridge.runner.JsPhoneStateRunner;
import com.gome.work.common.webview.jsbridge.runner.JsSecurityRunner;
import com.gome.work.common.webview.jsbridge.runner.JsUserRunner;
import com.gome.work.common.webview.jsbridge.runner.JsWindowRunner;
import com.gome.work.core.Constants;
import com.gome.work.core.event.EventDispatcher;
import com.gome.work.core.event.model.BaseChooseParamInfo;
import com.gome.work.core.event.model.EventInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JsNativeImpl implements MyJavascriptInterface, JsTaskCallbackable {

    /**
     * 用户选择
     */
    public final static int REQUEST_CODE_CHOOSE_USER = 1;

    /**
     * 文件选择
     */
    public final static int REQUEST_CODE_CHOOSE_FILE = 2;

    /**
     * 扫码
     */
    public final static int REQUEST_CODE_SCAN_QRCODE = 3;

    /**
     * 转发选择
     */
    public final static int REQUEST_CODE_FORWARDING = 4;

    /**
     * 人脸识别
     */
    public final static int REQUEST_CODE_FACE_ID = 5;

    private WebView mWebView;

    private CommonWebActivity mActivity;

    private JsTaskManager mJsTaskManager;

    private String mAppId;
    private String mAppSecret;

    private List<JsTaskRunnable> mJsTaskRunners;

    private JsFileRunner mJsFileRunner;

    private JsCommonRunner mJsCommonRunner;

    public JsNativeImpl(CommonWebActivity activity, WebView webview, String appId, String appSecret) {
        mActivity = activity;
        mWebView = webview;
        this.mAppId = appId;
        this.mAppSecret = appSecret;

        mJsTaskManager = new JsTaskManager(this);
        mJsTaskRunners = buildJsTaskRunners();
        for (JsTaskRunnable item : mJsTaskRunners) {
            mJsTaskManager.registerTaskRunner(item);
        }
    }

    public List<JsTaskRunnable> buildJsTaskRunners() {
        List<JsTaskRunnable> result = new ArrayList<>();
        result.add(new JsPhoneStateRunner(mActivity));
        result.add(new JsSecurityRunner(mActivity, mAppSecret));
        result.add(new JsActionbarRunner(mActivity));
        result.add(new JsWindowRunner(mActivity));
        result.add(new JsUserRunner(mActivity));
        result.add(new JsImageRunner(mActivity));

        mJsCommonRunner = new JsCommonRunner(mActivity, mWebView, mAppId);
        result.add(mJsCommonRunner);
        mJsFileRunner = new JsFileRunner(mActivity, mAppId);
        result.add(mJsFileRunner);
        return result;
    }


    public boolean onBackPressed() {
        if (mJsCommonRunner != null) {
            return mJsCommonRunner.onBackPressed();
        }
        return false;
    }

    public void handleFileChoose() {
        BaseChooseParamInfo info = new BaseChooseParamInfo(mActivity);
        info.requestCode = REQUEST_CODE_CHOOSE_FILE;
        info.chooseModel = Constants.MODEL_PICK_SINGLE;
        EventDispatcher.postEvent(EventInfo.FLAG_FILE_CHOOSE, info);
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_CHOOSE_FILE == requestCode) {
            if (data != null && resultCode == Activity.RESULT_OK) {
                List<File> files = (List<File>) data.getSerializableExtra(BaseGomeWorkActivity.EXTRA_DATA);
                if (!files.isEmpty()) {
                    mActivity.onFileGetResult(true, files.get(0));
                } else {
                    mActivity.onFileGetResult(false, null);
                }
            } else {
                mActivity.freeUploadCallback();
            }
            return true;
        } else {
            for (JsTaskRunnable item : mJsTaskRunners) {
                boolean result = item.onActivityResult(requestCode, resultCode, data);
                if (result) {
                    return true;
                }
            }
        }
        return false;
    }

    @JavascriptInterface
    public void invoke(String action, String params, final String callbackId) {
        if (TextUtils.isEmpty(action)) {
            ToastUtil.showToast(mActivity, "js invoke failed, action params can not be empty");
        }
        mJsTaskManager.addTask(new JsTask(action, params, callbackId));
    }


    @Override
    public void callback(JsTask task, String result) {
        handleJsCallback(task.callbackId, result);
    }

    public void handleJsCallback(String callbackId, String result) {
        String jsCode = "javascript:SmartOffice.handleCallback(" + callbackId + "," + result + ")";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.evaluateJavascript(jsCode, null);
        } else {
            mWebView.loadUrl(jsCode, null);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (JsTaskRunnable item : mJsTaskRunners) {
            boolean result = item.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (result) {
                break;
            }
        }
    }

    public void onImageGetResult(boolean isSuccess, Uri uri, File file) {
        if (mJsFileRunner != null) {
            mJsFileRunner.onImageGetResult(isSuccess, uri, file);
        }
    }
}
