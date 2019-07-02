package com.gome.work.common.webview.jsbridge.runner;

import android.text.TextUtils;

import com.gome.utils.AesAppUtils;
import com.gome.work.common.activity.BaseGomeWorkActivity;
import com.gome.work.common.webview.jsbridge.JsActions;
import com.gome.work.common.webview.jsbridge.JsTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 定义执行jsTask的接口
 */

public class JsSecurityRunner extends MyBaseJsRunner {

    private String mSecretKey = "";

    public JsSecurityRunner(BaseGomeWorkActivity activity, String secretKey) {
        super(activity);
        mSecretKey = secretKey;
    }

    @Override
    public String execute(JsTask task) throws  JSONException {
        JSONObject jsonObject = new JSONObject();
        switch (task.action) {
            case JsActions.ACTION_SIGN:
                String content = task.param;
                if (!TextUtils.isEmpty(mSecretKey)) {
                    content = AesAppUtils.getSHA1Digest(mSecretKey + content + mSecretKey);
                }
                jsonObject.put("result", content);
                break;
            case JsActions.ACTION_ENCRYPT:
                content = task.param;
                if (!TextUtils.isEmpty(mSecretKey)) {
                    content = AesAppUtils.encryptAES(content, mSecretKey);
                }
                jsonObject.put("result", content);
                break;
            default:
                break;
        }
        return jsonObject.toString();

    }

    @Override
    public String[] getActionList() {
        return new String[]{JsActions.ACTION_SIGN, JsActions.ACTION_ENCRYPT};
    }
}
