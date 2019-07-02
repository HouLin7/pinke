package com.gome.work.common;

import android.content.Context;

import com.gome.work.core.event.BaseEventConsumer;
import com.gome.work.core.event.model.EventInfo;
import com.gome.work.core.upload.FileUploadManager;

/**
 * Created by chaergongzi on 2018/8/17.
 */

public class CommonEventConsumer extends BaseEventConsumer {

    private final static int[] EVENTS = new int[]{
            EventInfo.FLAG_EVENT_FILE_PICKER
    };

    public CommonEventConsumer(Context context) {
        super(context, EVENTS);
    }

    @Override
    public void handleEvent(EventInfo eventInfo) {
        switch (eventInfo.what) {
            case EventInfo.FLAG_EVENT_FILE_PICKER:
                if (eventInfo.data instanceof String) {
                    String filePath = (String) eventInfo.data;
                    FileUploadManager.getInstance(context).tagEvent(filePath);
                }
                break;
            default:
                break;
        }
    }


}
