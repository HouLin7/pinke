package com.gome.work.common.webview.jsbridge.runner;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.gome.work.common.activity.BaseGomeWorkActivity;
import com.gome.work.common.webview.jsbridge.JsTask;
import com.gome.work.common.webview.jsbridge.JsTaskRunnable;

import org.json.JSONException;

/**
 * 定义执行jsTask的接口
 */

public abstract class MyBaseJsRunner implements JsTaskRunnable {

    protected AppCompatActivity mActivity;

    public MyBaseJsRunner(BaseGomeWorkActivity activity) {
        mActivity = activity;
    }

    @Override
    public String execute(JsTask task) throws JSONException, InterruptedException {
        return "";
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }

    @Override
    public boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        return false;
    }

    public static void sendMsgToJs(WebView webView, String funName, String data) {
        if (data == null) {
            data = "";
        }
        String jsCode = "javascript:" + funName + "(" + data + ")";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(jsCode, null);
        } else {
            webView.loadUrl(jsCode, null);
        }
    }
}
