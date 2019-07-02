package com.gome.work.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 通讯录节点模型
 */

public class ContactsNodeItem implements Serializable {

    public final static String NODE_TYPE_USER = "1";

    public final static String NODE_TYPE_DEPT = "0";

    @Expose
    @SerializedName("node_id")
    public String nodeId;
    @Expose
    @SerializedName("im_id")
    public String imId;
    @Expose
    public String name;//名称

    @Expose
    public String avatar;// 节点icon图片地址，目前仅为用户时可用

    @Expose
    public String id;//部门id或用户id

    @Expose
    public String type;// 0为组织结构，1为用户

    /**
     * 父节点ID
     */
    @Expose
    public String parentId;

    @Expose
    @SerializedName("is_has_dept")
    public boolean isHasDept;//是否有子节点

    /**
     * 该节点下的总人数
     */
    @Expose
    public int count;


    @Expose
    @SerializedName("user_extra_info")
    public UserExtraInfo userExtraInfo;

    public List<ContactsNodeItem> childrenNode;

    /**
     * 是也否是用户
     */
    public boolean isUserNode() {
        return !"0".equals(type);
    }

    public UserInfo toUserBean() {
        UserInfo result = new UserInfo();
        result.setId(id);
        result.setName(name);
        result.setAvatar(avatar);
        result.setImId(imId);
        return result;
    }

    public String getPositionDsc() {
        if (userExtraInfo != null) {
            return userExtraInfo.positions;
        }
        return "无岗位信息";
    }

    public static class UserExtraInfo implements Serializable {
        @Expose
        public String positions;

        @Expose
        public String email;
    }
}
