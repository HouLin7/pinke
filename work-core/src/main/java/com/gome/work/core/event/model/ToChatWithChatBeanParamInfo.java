package com.gome.work.core.event.model;

import android.app.Activity;

import java.io.Serializable;

/**
 * 打开聊天界面
 */

public class ToChatWithChatBeanParamInfo extends BaseParamInfo {

    /**
     * 聊天对象
     */
    public Serializable chatObj;

    /**
     * 透传数据
     */
    public Serializable extraData;

    public ToChatWithChatBeanParamInfo(Activity activity) {
        super(activity);
    }
}
