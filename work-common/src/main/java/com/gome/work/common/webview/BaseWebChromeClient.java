package com.gome.work.common.webview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.gome.work.common.R;

public abstract class BaseWebChromeClient<T> extends WebChromeClient {

    private FragmentActivity activity;

    public BaseWebChromeClient(FragmentActivity activity) {
        super();
        this.activity = activity;
    }

    protected ValueCallback<T> mUploadMessage;

    public void onActivityResult(boolean isSuccess, T data) {
        if (null == mUploadMessage)
            return;
        if (!isSuccess) {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
            }
            return;
        }

        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(data);
            mUploadMessage = null;
        }
    }

    public void freeUploadCallback() {
        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(null);
            mUploadMessage = null;
        }
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, false);
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        if (activity.isFinishing()) {
            return false;
        }
        AlertDialog.Builder b2 = new AlertDialog.Builder(activity).setMessage(message)
                .setPositiveButton("确定", new AlertDialog.OnClickListener() {
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
        if (activity.isFinishing()) {
            return false;
        }
        AlertDialog.Builder b2 = new AlertDialog.Builder(activity).setMessage(message)
                .setPositiveButton(R.string.confirm, new AlertDialog.OnClickListener() {
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

}
