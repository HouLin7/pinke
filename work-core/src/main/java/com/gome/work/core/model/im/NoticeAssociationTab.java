package com.gome.work.core.model.im;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Create by liupeiquan on 2018/11/16
 * 通知关系表
 * 保存所有下发通知（msgtype=99）在本地库中的基础信息
 */
@Entity(
        indexes = {
                @Index(value = "appId DESC, dataUseId DESC", unique = true)
        }
)
public class NoticeAssociationTab implements Serializable {

    private static final long serialVersionUID = 5278628328199197981L;
    @Id(autoincrement = true)
    private Long id;
    private String appId;
    private String appName;
    private int unReadCount;//未读数
    private String title;
    private String content;
    private long sendTime;
    private String icon;
    private int isShield;//免打扰
    private int isTop;//置顶
    private int msgType;
    private int groupType;
    private int isDelete;//是否删除
    private String dataUseId;//用于标记通知所属用户，数据库查询做区分

    @Generated(hash = 2106073965)
    public NoticeAssociationTab(Long id, String appId, String appName,
            int unReadCount, String title, String content, long sendTime,
            String icon, int isShield, int isTop, int msgType, int groupType,
            int isDelete, String dataUseId) {
        this.id = id;
        this.appId = appId;
        this.appName = appName;
        this.unReadCount = unReadCount;
        this.title = title;
        this.content = content;
        this.sendTime = sendTime;
        this.icon = icon;
        this.isShield = isShield;
        this.isTop = isTop;
        this.msgType = msgType;
        this.groupType = groupType;
        this.isDelete = isDelete;
        this.dataUseId = dataUseId;
    }

    @Generated(hash = 720663934)
    public NoticeAssociationTab() {
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public String getDataUseId() {
        return dataUseId;
    }

    public void setDataUseId(String dataUseId) {
        this.dataUseId = dataUseId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public boolean isShield() {
        return isShield == 1;
    }

    public void setShield(boolean shield) {
        isShield = shield ? 1 : 0;
    }

    public boolean isTop() {
        return isTop == 1;
    }

    public void setTop(boolean top) {
        isTop = top ? 1 : 0;
    }

    public boolean isDelete() {
        return isDelete == 1;
    }

    public void setDelete(boolean delete) {
        isDelete = delete ? 1 : 0;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public int getIsShield() {
        return this.isShield;
    }

    public void setIsShield(int isShield) {
        this.isShield = isShield;
    }

    public int getIsTop() {
        return this.isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }
}
