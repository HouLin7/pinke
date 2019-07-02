package com.gome.work.common.webview.jsbridge;

import android.content.Intent;
import android.support.annotation.NonNull;

import org.json.JSONException;

/**
 * 定义执行jsTask的接口
 */

public interface JsTaskRunnable {

    /**
     * @param task 处理js请求
     * @return
     * @throws InterruptedException
     * @throws JSONException
     */
    String execute(JsTask task) throws InterruptedException, JSONException;

    /**
     * 返回可处理的action列表
     *
     * @return
     */
    String[] getActionList();

    boolean onActivityResult(int requestCode, int resultCode, Intent data);

    boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

}
