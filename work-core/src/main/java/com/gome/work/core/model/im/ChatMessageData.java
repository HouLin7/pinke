package com.gome.work.core.model.im;

import java.io.Serializable;

/**
 * Create by liupeiquan on 2019/1/7
 */
public class ChatMessageData implements Serializable  {
    /**
     * 1：单聊 2：群聊 3: 机器人
     */
    public int chatType;
    /**
     * 对应单聊群聊传入 UserInfo 及 groupInfo
     */
    public Serializable data;
    /**
     * 对应分享及单据 ShareExtraData 及 BillExtraData
     */
    public Serializable extraData;
}
