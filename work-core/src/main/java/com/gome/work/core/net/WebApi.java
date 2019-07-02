
package com.gome.work.core.net;


import com.gome.work.core.model.*;
import com.gome.work.core.model.im.*;
import com.gome.work.core.model.schedule.ScheduleInfo;
import com.gome.work.core.model.schedule.ScheduleRemindInfo;
import com.gome.work.core.upload.IUploadListener;

import java.io.File;
import java.util.List;

public abstract class WebApi {

    private static WebApi instance = null;

    public static WebApi getInstance() {
        if (instance == null) {
            synchronized (WebApi.class) {
                if (instance == null) {
                    instance = new WebApiImpl();
                }
            }
        }
        return instance;
    }


    public abstract void login(String account, String password, IResponseListener<AccessTokenBean> listener);


    /**
     * @param user
     * @param listener
     */
    public abstract void addFriends(UserInfo user, IResponseListener<String> listener);

    /**
     * @param users
     * @param listener
     */
    public abstract void removeFriends(UserInfo users, IResponseListener<String> listener);


    /**
     * @param userId
     * @param listener
     */
    public abstract void getUserDetail(String userId, IResponseListener<UserInfo> listener);


    /**
     * 更新用户头像
     *
     * @param file
     * @param listener
     */
    public abstract void updateUserAvatar(File file, IUploadListener<String> listener);


    /**
     * 获取待办任务
     *
     * @param listener
     */
    public abstract void getBackLogList(IResponseListener<List<BackLogItem>> listener);


    /**
     * 获取已办任务
     *
     * @param listener
     */
    public abstract void getCompleteTaskList(IResponseListener<List<BackLogItem>> listener);


    /**
     * APP请求授权
     *
     * @param listener
     */
    public abstract void getRequestGrant(String requestToken, IResponseListener<RequestGrantBean> listener);


    /**
     * APP执行授权
     *
     * @param listener
     */
    public abstract void grantAccessToken(String requestToken, String captcha, IResponseListener<RequestGrantBean> listener);


    /**
     * APP执行登录
     *
     * @param listener
     */
    public abstract void getRequestGeantLogin(String requestToken, String captcha, IResponseListener<AccessTokenBean> listener);


    /**
     * app分类
     *
     * @param listener
     */
    public abstract void getAppCategory(IResponseListener<List<CategoryBean>> listener);

    /**
     * 发表评论
     *
     * @param listener
     */
    public abstract void commitCommentData(String appId, int score, String content, IResponseListener<String> listener);

    /**
     * 收藏
     *
     * @param listener
     */
    public abstract void getAppFavoriteData(String appId, IResponseListener<String> listener);

    /**
     * 点赞
     *
     * @param listener
     */
    public abstract void getAppPraiseData(String appId, IResponseListener<String> listener);

    /**
     * 取消收藏
     *
     * @param listener
     */
    public abstract void cancelAppFavoriteData(String appId, IResponseListener<String> listener);

    /**
     * 取消点赞
     *
     * @param listener
     */
    public abstract void cancelAppPraisData(String appId, IResponseListener<String> listener);

    /**
     * 同步我的收藏排序
     *
     * @param listener
     */
    public abstract void updateFavoriteSort(List<FavoriteSortBean> list, IResponseListener<String> listener);

    /**
     * 获取“工作”tab中广告
     *
     * @param listener
     */
    public abstract void getBannerList(IResponseListener<List<BannerBean>> listener);


    /**
     * 获取开屏广告
     *
     * @param listener
     */
    public abstract void getAdData(IResponseListener<AdBean> listener);


    /**
     * 上传图片
     */
    public abstract void uploadFile(File file, IUploadListener<UploadFileResultInfo> listener);

    /**
     * 获取账号的归属数据源
     *
     * @param userName 用户账号
     */
    public abstract void getDataSource(String userName, IResponseListener<List<DataSourceItem>> listener);


    /**
     * 获取所有群成员
     *
     * @param groupId
     * @param lastPullTimestamp 上一次的拉取好友的时间戳 （默认为0 ，查询出群中所有成员（不包括离开群的成员））
     * @param status            0-查询新加入和离开的群成员，1-新加入的成员，2-删除的新成员 默认为 0
     * @param page              分页页码
     * @param pageSize          分页大小 ，默认50 （最大为100）
     */
    public abstract void getIMChatGroupMenberList(String groupId, long lastPullTimestamp, int status, int page, int pageSize, IResponseListener<GroupMemberInfo> listener);

    /**
     * 获取群信息
     */
    public abstract void getImGroupInfo(String groupId, IResponseListener<GroupInfo> listener);

    /**
     * 通过群qrcode获取群信息
     */
    public abstract void getImGroupInfoByQrCode(String qrcode, IResponseListener<GroupInfo> listener);

    /**
     * 创建群聊
     */
    public abstract void imCreateGroupChat(String groupName, List<UserInfo> list, IResponseListener<GroupInfo> listener);

    /**
     * 修改群名称
     */
    public abstract void imUpdateGroupChatName(String groupName, String groupId, IResponseListener<GroupInfo> listener);

    /**
     * 邀请人入群
     *
     * @param groupId 群ID
     * @param list    成员ID 必填 ,成员昵称（OA-姓名），用于通知消息的显示用
     */
    public abstract void imGroupAddMembers(String groupId, List<UserInfo> list, IResponseListener<String> listener);

    /**
     * 主动加群
     */
    public abstract void imJoinGroup(String groupId, String nickName, IResponseListener<String> listener);

    /**
     * 主动退群
     */
    public abstract void imQuitGroup(String groupId, IResponseListener<String> listener);

    /**
     * 移除群成员
     *
     * @param groupId  群id
     * @param listener 用户信息集合
     */
    public abstract void imRemoveMemberByGroup(String groupId, List<UserInfo> list, IResponseListener<String> listener);

    /**
     * .解散群
     */
    public abstract void imDissolveGroup(String groupId, String nickName, IResponseListener<String> listener);

    /**
     * .创建群二维码
     */
    public abstract void imCreateGroupQrcode(String groupId, IResponseListener<GroupQrcode> listener);

    /**
     * .通过群二维码加群
     */
    public abstract void imJoinGroupByQrcode(String qrCode, IResponseListener<GroupInfo> listener);

    /**
     * 变更群主
     *
     * @param groupId  群id
     * @param imUserId 新群主的IM userId
     */
    public abstract void imAlterationGroupOwner(String groupId, String imUserId, IResponseListener<String> listener);

    /**
     * 发群公告
     *
     * @param groupId
     * @param noticeContent 公告内容
     */
    public abstract void imModifyGroupNotice(String groupId, String noticeContent, IResponseListener<String> listener);

    /**
     * 获取群公告
     */
    public abstract void imGetGroupNotice(String groupId, IResponseListener<GroupNoticeBean> listener);

    /**
     * 获取通知消息列表
     */
    public abstract void imGetNoticeList(String appId, long since_id, long max_id, int count, IResponseListener<List<NoticeInfo>> listener);

    /**
     * 获取刷新imtoken
     */
    public abstract void imGetRefreshToken(IResponseListener<IMLoginBean> listener);

    /**
     * 日程-查询有日程的日期接口
     */
    public abstract void getScheduleDateList(int userId, String startDate, String endDate, IResponseListener<List<String>> listener);

    /**
     * 日程-日程添加修改接口
     */
    public abstract void saveOrUpdateSchedule(ScheduleInfo data, IResponseListener<String> listener);

    /**
     * 日程-日程详情接口
     */
    public abstract void getScheduleDetail(int id, IResponseListener<ScheduleInfo> listener);

    /**
     * 日程-日程选择提前提醒时间的接口
     */
    public abstract void getScheduleRemindMinute(IResponseListener<List<ScheduleRemindInfo>> listener);

    /**
     * 日程-删除
     */
    public abstract void deleteScheduleById(int id, IResponseListener<String> listener);

    /**
     * 问题反馈
     */
    public abstract void submitFeedback(FeedBackBean feedBackBean, IResponseListener<String> listener);




}
