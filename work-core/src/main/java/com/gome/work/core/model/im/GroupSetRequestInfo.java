package com.gome.work.core.model.im;

import java.io.Serializable;
import java.util.List;

/**
 * Create by liupeiquan on 2018/11/19
 */
public class GroupSetRequestInfo implements Serializable {
    private static final long serialVersionUID = 5474904022899935023L;
    private String groupName;
    private String nickName;
    private String desc;
    private String extraInfo;
    private String groupId;
    private List<EditGroupInfo> memberInfos;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public List<EditGroupInfo> getMemberInfos() {
        return memberInfos;
    }

    public void setMemberInfos(List<EditGroupInfo> memberInfos) {
        this.memberInfos = memberInfos;
    }


}
