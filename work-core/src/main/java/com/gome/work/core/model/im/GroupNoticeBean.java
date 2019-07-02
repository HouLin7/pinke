package com.gome.work.core.model.im;

import java.io.Serializable;

/**
 * Create by liupeiquan on 2018/11/6
 */
public class GroupNoticeBean implements Serializable {
    private static final long serialVersionUID = -403506679774097338L;
    /**
     * "groupNotice": //群公告
     * {
     * "publishUserId", //群公告的发布者
     * "noticeContent":String, //群公告内容(可以为空)
     * "noticePublishTime":long //群公告发布时间
     * }
     */

    private String publishUserId;
    private String noticeContent;
    private long noticePublishTime;

    public String getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(String publishUserId) {
        this.publishUserId = publishUserId;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public long getNoticePublishTime() {
        return noticePublishTime;
    }

    public void setNoticePublishTime(long noticePublishTime) {
        this.noticePublishTime = noticePublishTime;
    }
}
