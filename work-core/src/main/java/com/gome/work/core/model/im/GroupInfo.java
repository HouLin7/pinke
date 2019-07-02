package com.gome.work.core.model.im;

import android.support.annotation.NonNull;

import com.gome.work.core.model.ISelectableItem;
import com.gome.work.core.model.converter.GroupNoticeConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * Create by liupeiquan on 2018/11/6
 */
@Entity
public class GroupInfo implements Serializable, Comparable<GroupInfo>, ISelectableItem {

    private static final long serialVersionUID = -1732697257755251112L;
    /**
     * {
     * "groupId":String, //群ID
     * "userId":long, //群主ID
     * "groupName":String, //群名称
     * "desc":String, //群描述
     * "avatar":String, //群头像
     * "capacity":int, //群容量
     * "extraInfo": //扩展信息
     * "totalCount":long //群成员数
     */
    @Id(autoincrement = true)
    private Long id;

    @Unique
    private String groupId;
    private long userId;
    private String groupName;
    private String groupInitName;
    private String desc;
    private String avatar;
    private int capacity;
    private String extraInfo;
    private long totalCount;
    private int status;//群状态，1解散，2退群，

    private String firstLetter;//示数据拼音的首字母

    @Convert(columnType = String.class, converter = GroupNoticeConverter.class)
    private GroupNoticeBean groupNotice;

    @Generated(hash = 1114347958)
    public GroupInfo(Long id, String groupId, long userId, String groupName,
                     String groupInitName, String desc, String avatar, int capacity, String extraInfo,
                     long totalCount, int status, String firstLetter, GroupNoticeBean groupNotice) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.groupName = groupName;
        this.groupInitName = groupInitName;
        this.desc = desc;
        this.avatar = avatar;
        this.capacity = capacity;
        this.extraInfo = extraInfo;
        this.totalCount = totalCount;
        this.status = status;
        this.firstLetter = firstLetter;
        this.groupNotice = groupNotice;
    }

    @Generated(hash = 1250265142)
    public GroupInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupInitName() {
        return this.groupInitName;
    }

    public void setGroupInitName(String groupInitName) {
        this.groupInitName = groupInitName;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getExtraInfo() {
        return this.extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public long getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFirstLetter() {
        return this.firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public GroupNoticeBean getGroupNotice() {
        return this.groupNotice;
    }

    public void setGroupNotice(GroupNoticeBean groupNotice) {
        this.groupNotice = groupNotice;
    }


    @Override
    public int compareTo(@NonNull GroupInfo bean) {
        if (firstLetter.equals("#") && !bean.getFirstLetter().equals("#")) {
            return -1;
        } else if (!firstLetter.equals("#") && bean.getFirstLetter().equals("#")) {
            return 1;
        } else {
            return firstLetter.compareToIgnoreCase(bean.getFirstLetter());
        }
    }

    @Override
    public String getItemId() {
        return getGroupId();
    }

    @Override
    public String getItemAvatar() {
        return getAvatar();
    }

    @Override
    public String getItemName() {
        return getGroupName();
    }
}
