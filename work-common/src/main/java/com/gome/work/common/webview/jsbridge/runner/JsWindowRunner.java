package com.gome.work.common.webview.jsbridge.runner;

import android.content.Intent;
import android.text.TextUtils;

import com.gome.utils.GsonUtil;
import com.gome.work.common.activity.BaseGomeWorkActivity;
import com.gome.work.common.activity.PhotoViewActivity;
import com.gome.work.common.webview.CommonWebActivity;
import com.gome.work.common.webview.jsbridge.JsActions;
import com.gome.work.common.webview.jsbridge.JsTask;
import com.gome.work.common.webview.model.JsImageWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 定义执行jsTask的接口
 */

public class JsWindowRunner extends MyBaseJsRunner {

    public JsWindowRunner(BaseGomeWorkActivity activity) {
        super(activity);
    }

    @Override
    public String execute(JsTask task) throws InterruptedException, JSONException {
        JSONObject jsonObject = new JSONObject();
        switch (task.action) {
            case JsActions.ACTION_CLOSE_WINDOW:
                mActivity.finish();
                break;
            case JsActions.ACTION_OPEN_NEW_WINDOW:
                Intent intent = new Intent(mActivity, CommonWebActivity.class);
                intent.putExtra(CommonWebActivity.Companion.getEXTRA_URL(), task.param);
                intent.putExtra(CommonWebActivity.Companion.getEXTRA_IS_ASSEMBLE_SIGN(), false);
                mActivity.startActivity(intent);
            default:
                break;
        }
        jsonObject.put("result", "success");
        return jsonObject.toString();

    }

    @Override
    public String[] getActionList() {
        return new String[]{JsActions.ACTION_OPEN_NEW_WINDOW, JsActions.ACTION_CLOSE_WINDOW};
    }
}
