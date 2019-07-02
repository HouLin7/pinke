package com.gome.work.core.model.im;

import java.io.Serializable;

/**
 * Create by liupeiquan on 2018/11/16
 */
public class NoticeInfo implements Serializable {
    private static final long serialVersionUID = 2499252592572132949L;

    /**
     * appId	String	由开发者平台颁发的16位app标识
     * appName	String	开发者平台颁发的app名称
     * id	int	消息Id
     * title	String	消息标题
     * content	String	消息内容
     * extraData	JSON	透传数据
     * createTime	long	13位 UTC时间戳
     * linkUrl	String	点击跳转的网页地址
     */

    private int id;
    private String content;
    private String appId;
    private String linkUrl;
    private String extraData;
    private String dataSouceCode;
    private int userId;
    private String appName;
    private long createTime;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public String getDataSouceCode() {
        return dataSouceCode;
    }

    public void setDataSouceCode(String dataSouceCode) {
        this.dataSouceCode = dataSouceCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
