
package com.gome.work.core.net;


import com.gome.work.core.model.AccessTokenInfo;
import com.gome.work.core.model.AdBean;
import com.gome.work.core.model.BannerBean;
import com.gome.work.core.model.BaseRspInfo;
import com.gome.work.core.model.CaptchaItem;
import com.gome.work.core.model.FeedBackBean;
import com.gome.work.core.model.PostSearchPartnerItem;
import com.gome.work.core.model.PostUserInfo;
import com.gome.work.core.model.RegionItem;
import com.gome.work.core.model.RequestGrantBean;
import com.gome.work.core.model.SearchPartnerItem;
import com.gome.work.core.model.SysCfgData;
import com.gome.work.core.model.UploadFileResultInfo;
import com.gome.work.core.model.UserInfo;
import com.gome.work.core.model.UsersRspInfo;
import com.gome.work.core.model.im.GroupInfo;
import com.gome.work.core.model.im.GroupMemberInfo;
import com.gome.work.core.model.im.GroupNoticeBean;
import com.gome.work.core.model.im.GroupQrcode;
import com.gome.work.core.model.schedule.ScheduleInfo;
import com.gome.work.core.model.schedule.ScheduleRemindInfo;
import com.gome.work.core.upload.IUploadListener;

import java.io.File;
import java.util.List;

import retrofit2.Response;

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

    /**
     * @param account
     * @param password
     * @param loginType
     * @param listener
     */
    public abstract void login(String account, String password, String loginType, IResponseListener<AccessTokenInfo> listener);

    public abstract void register(String account, String password, String captcha, IResponseListener<UserInfo> listener);

    public abstract void getCityData(IResponseListener<List<RegionItem>> listener);

    public abstract void getCaptcha(String phoneNum, IResponseListener<CaptchaItem> listener);

    public abstract void getRecommendTeachers(String posCode, double latitude, double longitude, IResponseListener<UsersRspInfo> listener);

    public abstract void getBanner(String pos, IResponseListener<List<BannerBean>> listener);

    public abstract void getAd(IResponseListener<List<AdBean>> listener);

    public abstract void getLauncherPic(IResponseListener<List<AdBean>> listener);

    public abstract void forgetPassword(String username, String newPwd, String captcha, IResponseListener<String> listener);

    public abstract void modifyPassword(String originalPwd, String newPwd, IResponseListener<String> listener);

    /**
     * 基础数据字典
     *
     * @param type
     * @param listener
     */
    public abstract void getConfigDataDic(String type, IResponseListener<SysCfgData> listener);


    public abstract void postUserInfo(PostUserInfo userInfo, IResponseListener<String> listener);

    /**
     * @param dataItem
     * @param listener
     */
    public abstract void postSearchPartnerItem(PostSearchPartnerItem dataItem, IResponseListener<String> listener);

    public abstract void getSearchPartnerList(int pageIndex, int pageSize, String courseCode, IResponseListener<SearchPartnerItem.ResponseWrapper> listener);


    public abstract void getTeacherList(int pageIndex, int pageSize, String courseCode, IResponseListener<UsersRspInfo> listener);


    /**
     * @param userId
     * @param listener
     */
    public abstract void getUserInfo(String userId, IResponseListener<UserInfo> listener);

    /**
     * @param userId
     */
    public abstract Response<BaseRspInfo<UserInfo>> getUserInfoSyn(String userId);


    /**
     * 关注某人
     *
     * @param userId
     * @param listener
     */
    public abstract void follow(String userId, IResponseListener<String> listener);

    /**
     * 取消关注
     *
     * @param userId
     * @param listener
     */
    public abstract void followCancel(String userId, IResponseListener<String> listener);

    /**
     * 伴读某人
     *
     * @param userId
     * @param listener
     */
    public abstract void partner(String userId, IResponseListener<String> listener);

    /**
     * 伴读关系取消
     *
     * @param userId
     * @param listener
     */
    public abstract void partnerCancel(String userId, IResponseListener<String> listener);


    /**
     * 获取伴读好友列表
     *
     * @param pageIndex
     * @param pageSize
     * @param listener
     */
    public abstract void getPartners(int pageIndex, int pageSize, IResponseListener<UsersRspInfo> listener);

    /**
     * 获取好友列表
     *
     * @param pageIndex
     * @param pageSize
     * @param listener
     */
    public abstract void getFriends(int pageIndex, int pageSize, IResponseListener<UsersRspInfo> listener);


    /**
     * @param pageIndex
     * @param pageSize
     * @param listener
     */
    public abstract void getFollowers(int pageIndex, int pageSize, IResponseListener<UsersRspInfo> listener);


    /**
     * 获取我发布的伴读信息
     *
     * @param pageIndex
     * @param pageSize
     * @param listener
     */
    public abstract void getMySearchPartnerList(int pageIndex, int pageSize, IResponseListener<SearchPartnerItem.ResponseWrapper> listener);

    /**
     *
     * @param listener
     */
    public abstract void getScheduleList(IResponseListener<String> listener);


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
    public abstract void getRequestGeantLogin(String requestToken, String captcha, IResponseListener<AccessTokenInfo> listener);


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
    public abstract void cancelAppPraiseData(String appId, IResponseListener<String> listener);


    /**
     * 上传图片
     */
    public abstract void uploadFile(File file, IUploadListener<UploadFileResultInfo> listener);

    /**
     * 获取所有群成员
     *
     * @param groupId
     * @param lastPullTimestamp 上一次的拉取好友的时间戳 （默认为0 ，查询出群中所有成员（不包括离开群的成员））
     * @param status            0-查询新加入和离开的群成员，1-新加入的成员，2-删除的新成员 默认为 0
     * @param page              分页页码
     * @param pageSize          分页大小 ，默认50 （最大为100）
     */
    public abstract void getIMChatGroupMemberList(String groupId, long lastPullTimestamp, int status, int page, int pageSize, IResponseListener<GroupMemberInfo> listener);

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
