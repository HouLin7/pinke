package com.gome.work.core.model.im;

import com.gome.work.core.model.UserInfo;

import java.util.List;

/**
 * Create by liupeiquan on 2018/11/6
 */
public class GroupMemberInfo {

    /**
     * {
     * "memberInfos":
     * [
     * {
     * "id":"1100102", //移动办公系统用户id
     * "imUserId":"1100102", //im系统的用户ID
     * "name":"张三",// 用户姓名
     * "avatar":"http://",// 图片地址
     * "joinTime":long, //加入（或退出）群时间（退出成员 status=2时，joinTime为退出时间）
     * "identity":int, //身份: 0-普通成员,1-群主,2-管理员（暂时未用到indentity=2）
     * "status":int, //状态，审核是否通过 0-未通过，1-通过
     * }
     * ]
     * "timestamp":long, //本次拉取的时间戳
     * "totalCount":long //自lastPullTimestamp至现在 群成员中的变化群成员数
     * }
     */

    private long timestamp;
    private long totalCount;
    private List<UserInfo> memberInfos;//群组成员列表，
    private int page;
    private int page_total;
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<UserInfo> getMemberInfos() {
        return memberInfos;
    }

    public void setMemberInfos(List<UserInfo> memberInfos) {
        this.memberInfos = memberInfos;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage_total() {
        return page_total;
    }

    public void setPage_total(int page_total) {
        this.page_total = page_total;
    }
}
