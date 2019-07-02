package com.gome.work.common.webview.jsbridge.runner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.gome.utils.PhoneStateUtils;
import com.gome.work.common.BuildConfig;
import com.gome.work.common.activity.BaseGomeWorkActivity;
import com.gome.work.common.webview.jsbridge.JsActions;
import com.gome.work.common.webview.jsbridge.JsTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * 定义执行jsTask的接口
 */

public class JsPhoneStateRunner extends MyBaseJsRunner {

    public final static int REQUEST_CODE_REQUEST_PERMISSION = 0x01;

    private String deviceId = "";
    private String mac = "";
    private boolean isGrantedPermission = false;
    private String resolution = "";
    private String language = "";


    public JsPhoneStateRunner(BaseGomeWorkActivity activity) {
        super(activity);
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        resolution = displayMetrics.widthPixels + "x" + displayMetrics.heightPixels;
        Locale locale = activity.getResources().getConfiguration().locale;
        language = locale.getLanguage();
    }

    private void assembleOtherInfo(JSONObject jsonObject) throws JSONException {
        jsonObject.put("osVersion", Build.VERSION.RELEASE);
        jsonObject.put("osName", "android");
        jsonObject.put("lang", language);
        jsonObject.put("brand", Build.BRAND);
        jsonObject.put("model", android.os.Build.MODEL);
        jsonObject.put("screen", resolution);
    }


    @Override
    public String execute(JsTask task) throws InterruptedException, JSONException {
        JSONObject jsonObject = new JSONObject();
        switch (task.action) {
            case JsActions.ACTION_GET_DEVICE:
                if (!TextUtils.isEmpty(deviceId)) {
                    jsonObject.put("mac", mac);
                    jsonObject.put("imei", deviceId);
                    assembleOtherInfo(jsonObject);
                    break;
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String permission = Manifest.permission.READ_PHONE_STATE;
                        if (ActivityCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                            mActivity.requestPermissions(new String[]{permission}, REQUEST_CODE_REQUEST_PERMISSION);
                            isGrantedPermission = false;
                            synchronized (JsPhoneStateRunner.this) {
                                JsPhoneStateRunner.this.wait();
                            }
                            if (isGrantedPermission) {
                                deviceId = PhoneStateUtils.getDeviceID(mActivity);
                                mac = PhoneStateUtils.getMacAddress(mActivity);
                            }
                        } else {
                            deviceId = PhoneStateUtils.getDeviceID(mActivity);
                            mac = PhoneStateUtils.getMacAddress(mActivity);
                        }
                    } else {
                        deviceId = PhoneStateUtils.getDeviceID(mActivity);
                        mac = PhoneStateUtils.getMacAddress(mActivity);
                    }
                }
                jsonObject.put("mac", mac);
                jsonObject.put("imei", deviceId);
                assembleOtherInfo(jsonObject);
                break;
            case JsActions.ACTION_GET_NETWORK:
                jsonObject = new JSONObject();
                jsonObject.put("ip", PhoneStateUtils.getIPAddress(mActivity));
                jsonObject.put("type", PhoneStateUtils.getNetConnectType(mActivity));
                break;
            default:
                break;
        }
        return jsonObject.toString();

    }

    @Override
    public String[] getActionList() {
        return new String[]{JsActions.ACTION_GET_DEVICE, JsActions.ACTION_GET_NETWORK};
    }

    @Override
    public boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (REQUEST_CODE_REQUEST_PERMISSION == requestCode) {
            isGrantedPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            synchronized (JsPhoneStateRunner.this) {
                JsPhoneStateRunner.this.notify();
            }
            return true;
        }
        return super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
