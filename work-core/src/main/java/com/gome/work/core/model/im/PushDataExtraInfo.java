package com.gome.work.core.model.im;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Create by liupeiquan on 2018/11/16
 */
@Entity
public class PushDataExtraInfo implements Serializable {

    private static final long serialVersionUID = -7921370680221939565L;
    /**
     * activity :
     * content : qwe
     * extra : eyJleHRyYURhdGEiOiJxd2VxdyIsImFwcElkIjoiYTdkYTk0MjM3ZmMwZWY1NSIsImxpbmtVcmwi
     OiIifQ==
     * msgId :
     * title : qweq
     * url :
     */
    @Id(autoincrement = true)
    private Long id;
    private String activity;
    private String content;
    private String extra;
    private String msgId;
    private String title;
    private String url;
    private boolean isRead;
    private String appId;

    @Generated(hash = 1480494452)
    public PushDataExtraInfo(Long id, String activity, String content, String extra,
            String msgId, String title, String url, boolean isRead, String appId) {
        this.id = id;
        this.activity = activity;
        this.content = content;
        this.extra = extra;
        this.msgId = msgId;
        this.title = title;
        this.url = url;
        this.isRead = isRead;
        this.appId = appId;
    }

    @Generated(hash = 802723199)
    public PushDataExtraInfo() {
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
