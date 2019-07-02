package com.gome.work.common.webview.jsbridge.runner;

import android.content.Intent;
import android.text.TextUtils;

import com.gome.utils.GsonUtil;
import com.gome.work.common.activity.BaseGomeWorkActivity;
import com.gome.work.common.activity.PhotoViewActivity;
import com.gome.work.common.webview.jsbridge.JsActions;
import com.gome.work.common.webview.jsbridge.JsTask;
import com.gome.work.common.webview.model.JsImageWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 定义执行jsTask的接口
 */

public class JsImageRunner extends MyBaseJsRunner {

    public JsImageRunner(BaseGomeWorkActivity activity) {
        super(activity);
    }

    @Override
    public String execute(JsTask task) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        switch (task.action) {
            case JsActions.ACTION_SHOW_IMAGE:
                String content = task.param;
                JSONObject jsonObject1 = new JSONObject(content);
                String url = jsonObject1.optString("url");
                if (!TextUtils.isEmpty(url)) {
                    ArrayList<String> urls = new ArrayList<>();
                    urls.add(url);
                    Intent intent1 = new Intent(mActivity, PhotoViewActivity.class);
                    intent1.putExtra(PhotoViewActivity.EXTRA_DATA_IMAGES_INDEX, 0);
                    intent1.putExtra(PhotoViewActivity.EXTRA_DATA_IMAGES_URL, urls);
                    mActivity.startActivity(intent1);
                }
                break;
            case JsActions.ACTION_SHOW_BULK_IMAGE:
                content = task.param;
                JsImageWrapper imageWrapper = GsonUtil.jsonToObject(JsImageWrapper.class, content);
                if (imageWrapper != null && imageWrapper.images != null) {
                    Intent intent1 = new Intent(mActivity, PhotoViewActivity.class);
                    intent1.putExtra(PhotoViewActivity.EXTRA_DATA_IMAGES_INDEX, imageWrapper.index);
                    ArrayList<String> urls = new ArrayList<>();
                    for (JsImageWrapper.ImageItem item : imageWrapper.images) {
                        urls.add(item.url);
                    }
                    intent1.putExtra(PhotoViewActivity.EXTRA_DATA_IMAGES_URL, urls);
                    mActivity.startActivity(intent1);
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
        return new String[]{JsActions.ACTION_SHOW_IMAGE, JsActions.ACTION_SHOW_BULK_IMAGE};
    }
}
