package com.gome.work.common.webview.jsbridge;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * js任务的执行调度类
 */
public class JsTaskManager {
    private HandlerThread thread = new HandlerThread("js.task.thread");
    private Handler handler;
    private Map<String, JsTaskRunnable> mRunner = new HashMap<>();

    private JsTaskCallbackable mJsTaskCallbackable;

    private Handler mUIHandler = new Handler();

    public JsTaskManager(JsTaskCallbackable jsTaskCallbackable) {
        this.mJsTaskCallbackable = jsTaskCallbackable;
        thread.start();
        handler = new Handler(thread.getLooper()) {

            @Override
            public void handleMessage(Message msg) {
                final JsTask task = (JsTask) msg.obj;
                handleJsTask(task);
            }
        };
    }

    private void handleJsTask(final JsTask task) {
        JsTaskRunnable obj = mRunner.get(task.action);
        if (obj != null) {
            String result = "";
            try {
                result = obj.execute(task);
            } catch (Exception e) {
                e.printStackTrace();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("errorMsg", e.getMessage());
                    result = jsonObject.toString();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            final String finalResult = result;
            mUIHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mJsTaskCallbackable != null) {
                        mJsTaskCallbackable.callback(task, finalResult);
                    }
                }
            });
        }
    }

    /**
     * 注册一个任务执行者
     *
     * @param runnable
     */
    public void registerTaskRunner(JsTaskRunnable runnable) {
        for (String item : runnable.getActionList())
            mRunner.put(item, runnable);
    }

    /**
     * 增加一个任务
     *
     * @param task
     */
    public void addTask(JsTask task) {
        Message msg = Message.obtain();
        msg.obj = task;
        handler.sendMessage(msg);
    }


}
