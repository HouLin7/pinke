package com.gome.work.common.webview.jsbridge.runner;

import android.text.TextUtils;

import com.gome.work.common.activity.BaseGomeWorkActivity;
import com.gome.work.common.webview.jsbridge.JsActions;
import com.gome.work.common.webview.jsbridge.JsTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 导航栏相关处理方法
 */

public class JsActionbarRunner extends MyBaseJsRunner {

    private String[] mActionList = new String[]{JsActions.ACTION_HIDE_TITLE_BAR, JsActions.ACTION_SHOW_TITLE_BAR, JsActions.ACTION_SET_TITLE};

    public JsActionbarRunner(BaseGomeWorkActivity activity) {
        super(activity);
    }

    @Override
    public String execute(final JsTask task) throws InterruptedException, JSONException {
        JSONObject jsonObject = new JSONObject();
        switch (task.action) {
            case JsActions.ACTION_HIDE_TITLE_BAR:
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mActivity.getSupportActionBar().hide();
                    }
                });
                break;
            case JsActions.ACTION_SHOW_TITLE_BAR:
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mActivity.getSupportActionBar().show();
                    }
                });
                break;
            case JsActions.ACTION_SET_TITLE:
                if (!TextUtils.isEmpty(task.param)) {
                    JSONObject json = new JSONObject(task.param);
                    final String name = json.optString("name");
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mActivity.setTitle(name);
                        }
                    });
                }
                break;
            default:
                break;
        }
        jsonObject.put("result", "success");
        return jsonObject.toString();

    }

    @Override
    public String[] getActionList() {
        return mActionList;
    }
}
