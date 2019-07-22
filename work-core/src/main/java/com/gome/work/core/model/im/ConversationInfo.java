package com.gome.work.core.model.im;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Create by liupeiquan on 2018/11/16
 */
@Entity(
        indexes = {
                @Index(value = "conversationId , dataUseId ", unique = true)
        }
)
public class ConversationInfo implements Serializable, Comparable<ConversationInfo> {

    public static final int MSG_TYPE_CHAT = 0;

    public static final int MSG_TYPE_PUSH = 1;

    private static final long serialVersionUID = 5278628328199197981L;

    @Id(autoincrement = true)
    private Long id;

    /**
     * 会话ID
     */
    @NotNull
    private String conversationId;

    @NotNull
    private String title;

    private String content;

    private int unReadCount;//未读数

    private long updateTime;

    private String icon;

    private int isShield;//免打扰

    private int isTop;//置顶

    /**
     * 区分消息类型
     */
    private int msgType;

    private String extra;

    @NotNull
    private String dataUseId;//用于标记通知所属用户，数据库查询做区分


    @Generated(hash = 95623996)
    public ConversationInfo(Long id, @NotNull String conversationId, @NotNull String title,
                            String content, int unReadCount, long updateTime, String icon, int isShield,
                            int isTop, int msgType, String extra, @NotNull String dataUseId) {
        this.id = id;
        this.conversationId = conversationId;
        this.title = title;
        this.content = content;
        this.unReadCount = unReadCount;
        this.updateTime = updateTime;
        this.icon = icon;
        this.isShield = isShield;
        this.isTop = isTop;
        this.msgType = msgType;
        this.extra = extra;
        this.dataUseId = dataUseId;
    }

    @Generated(hash = 837114692)
    public ConversationInfo() {
    }


    public String getDataUseId() {
        return dataUseId;
    }

    public void setDataUseId(String dataUseId) {
        this.dataUseId = dataUseId;
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

    public String getConversationId() {
        return this.conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public long getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public int compareTo(ConversationInfo o) {
        if (o == null) {
            return 1;
        }
        return (int) (updateTime - o.updateTime);
    }

    public int getMsgType() {
        return this.msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }


}
