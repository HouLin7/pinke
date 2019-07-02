package com.gome.work.core.net.api;


import com.gome.work.core.model.*;
import com.gome.work.core.model.im.*;
import com.gome.work.core.model.schedule.ScheduleInfo;
import com.gome.work.core.model.schedule.ScheduleRemindInfo;
import com.gome.work.core.net.APIConstants;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

/**
 * 新OA通讯录接口
 * Created by Marlon on 2018/5/25 16:59.
 */
public interface ApiService {

    /**
     * 用户登录
     * 参数在HeaderParamsInterceptor拦截器中传递
     *
     * @return
     */
    @POST(APIConstants.GET_TOKEN)
    Call<BaseRspInfo<AccessTokenBean>> login();

    /**
     * 添加好友
     */
    @POST(APIConstants.ADD_FRIEND)
    Call<BaseRspInfo<String>> addFriend(@Body Map<String, String> params);

    /**
     * 移除（删除）好友
     */
    @POST(APIConstants.REMOVE_FRIEND)
    Call<BaseRspInfo<String>> removeFriend(@Body Map<String, String> params);


    /**
     * 获取用户详情
     */
    @GET(APIConstants.GET_USER_DETAIL)
    Call<BaseRspInfo<UserInfo>> getUserDetail(@Path("userId") String userId);


    /**
     * 更新头像
     */
    @Multipart
    @POST(APIConstants.UPDATE_USER_AVATA)
    Call<BaseRspInfo<String>> updateUserAvatar(@Part MultipartBody.Part file);

    /**
     * 获取待办任务
     */
    @GET(APIConstants.GET_BACK_LOG_LIST)
    Call<BaseRspInfo<List<BackLogItem>>> getBackLogListData();


    /**
     * 获取待办任务
     */
    @GET(APIConstants.GET_COMPLETE_TASK_LIST)
    Call<BaseRspInfo<List<BackLogItem>>> getCompleteTaskList();

    /**
     * APP请求授权
     */
    @POST(APIConstants.GET_REQUEST_GRANT)
    Call<BaseRspInfo<RequestGrantBean>> getRequestGrant(@Body Map<String, String> params);

    /**
     * APP执行授权
     */
    @POST(APIConstants.GRANT_ACCESS_TOKEN)
    Call<BaseRspInfo<RequestGrantBean>> GrantAccessToken(@Body Map<String, String> params);

    /**
     * APP执行登录
     */
    @POST(APIConstants.GET_REQUEST_GRANT_LOGIN)
    Call<BaseRspInfo<AccessTokenBean>> getRequestGeantLogin(@Body Map<String, String> params);

    /**
     * 获取应用全部分类
     */
    @GET(APIConstants.GET_APP_CATEGORY)
    Call<BaseRspInfo<List<CategoryBean>>> getAppCategory();

    /**
     * 发表评论
     */
    @POST(APIConstants.SUBMIT_COMMENT_DATA)
    Call<BaseRspInfo<String>> submitCommentData(@Body Map<String, String> params);

    /**
     * 收藏
     */
    @POST(APIConstants.GET_APP_FAVORITE_DATA)
    Call<BaseRspInfo<String>> getAppFavoriteData(@Body Map<String, String> params);

    /**
     * 点赞
     */
    @POST(APIConstants.GET_APP_PRAIS_DATA)
    Call<BaseRspInfo<String>> getAppPraisData(@Body Map<String, String> params);


    /**
     * 取消收藏
     */
    @POST(APIConstants.CANCEL_APP_FAVORITE_DATA)
    Call<BaseRspInfo<String>> cancelAppFavoriteData(@Body Map<String, String> params);

    /**
     * 取消点赞
     */
    @POST(APIConstants.CANCEL_APP_PRAIS_DATA)
    Call<BaseRspInfo<String>> cancelAppPraisData(@Body Map<String, String> params);

    /**
     * 同步我的收藏排序
     */
    @POST(APIConstants.UPDATE_FAVORITE_SORT)
    Call<BaseRspInfo<String>> updateFavoriteSort(@Body List<FavoriteSortBean> favoriteSortBeans);

    /**
     * 获取“工作”tab中广告
     */
    @GET(APIConstants.GET_BANNER_LIST)
    Call<BaseRspInfo<List<BannerBean>>> getBannerList();

    /**
     * 获取开屏广告
     */
    @GET(APIConstants.GET_AD_DATA)
    Call<BaseRspInfo<AdBean>> getAdData();

//    /**
//     * 上传文件
//     */
//    @Multipart
//    @POST(APIConstants.UPLOAD_PICTURE)
//    Call<BaseRspInfo<UploadFileResultInfo>> uploadPicture(@Part MultipartBody.Part file);

    /**
     * 上传文件
     */
    @Multipart
    @POST(APIConstants.UPLOAD_FILE)
    Call<BaseRspInfo<UploadFileResultInfo>> uploadFile(@Part MultipartBody.Part file);


    //******************************日程相关接口↓↓↓↓↓↓↓*****************************************

    /**
     * 日程-查询有日程的日期接口
     */
    @GET(APIConstants.GET_SCHEDULE_DATE_LIST)
    Call<BaseRspInfo<List<String>>> getScheduleDateList(@Query("userId") int userId, @Query("startDate") String startDate, @Query("endDate") String endDate);

    /**
     * 日程-日程添加修改接口
     */
    @POST(APIConstants.SAVE_OR_UPDATE_SCHEDULE)
    Call<BaseRspInfo<String>> saveOrUpdateSchedule(@Body Map<String, Object> params);

    /**
     * 日程-日程详情接口
     */
    @GET(APIConstants.GET_SCHEDULE_DETAIL)
    Call<BaseRspInfo<ScheduleInfo>> getScheduleDetail(@Query("id") int id);

    /**
     * 日程-日程选择提前提醒时间的接口
     */
    @GET(APIConstants.GET_SCHEDULE_REMIND_MINUTE)
    Call<BaseRspInfo<List<ScheduleRemindInfo>>> getScheduleRemindMinute();

    /**
     * 日程-日程选择提前提醒时间的接口
     */
    @GET(APIConstants.DELETE_SCHEDULE)
    Call<BaseRspInfo<String>> deleteScheduleById(@Query("id") int id);


    //******************************日程相关接口↑↑↑↑↑↑↑*****************************************

    /**
     * 获取数据源名称
     */
    @GET(APIConstants.GET_DATA_SOURCE)
    Call<BaseRspInfo<List<DataSourceItem>>> getDataSource(@Query("userName") String userName);

    /**
     * 问题反馈
     */
    @POST(APIConstants.POST_FEED_BACK)
    Call<BaseRspInfo<String>> submitFeedBack(@Body RequestDataInfo params);

    //******************************IM相关接口↓↓↓↓↓↓↓*****************************************

    /**
     * 创建群聊
     */
    @POST(APIConstants.CREATE_GROUP)
    Call<BaseRspInfo<GroupInfo>> imCreateGroupChat(@Body GroupSetRequestInfo body);

    /**
     * 修改群名称
     */
    @POST(APIConstants.UPDATE_GROUP)
    Call<BaseRspInfo<GroupInfo>> imUpdateGroupChatName(@Body Map<String, String> params);

    /**
     * 获取所有群成员
     */
    @POST(APIConstants.GET_MEMBER_LIST_BY_PAGE)
    Call<BaseRspInfo<GroupMemberInfo>> getIMChatGroupMenberList(@Body Map<String, String> params);

    /**
     * 获取群信息
     */
    @GET(APIConstants.GET_GROUP_INFO)
    Call<BaseRspInfo<GroupInfo>> getImGroupInfo(@Path("groupId") String groupId);

    /**
     * 通过群二维码获取群信息
     */
    @POST(APIConstants.GET_GROUP_INFO_BY_QRCODE)
    Call<BaseRspInfo<GroupInfo>> getImGroupInfoByQrcode(@Body Map<String, String> params);

    /**
     * 邀请人入群
     */
    @POST(APIConstants.ADD_MEMBERS)
    Call<BaseRspInfo<String>> imGroupAddMembers(@Body GroupSetRequestInfo body);

    /**
     * 主动加群
     */
    @POST(APIConstants.JOIN_GROUP)
    Call<BaseRspInfo<String>> imJoinGroup(@Body Map<String, String> params);

    /**
     * 主动退群
     */
    @POST(APIConstants.QUIT_GROUP)
    Call<BaseRspInfo<String>> imQuitGroup(@Body Map<String, String> params);

    /**
     * 移除群成员
     */
    @POST(APIConstants.REMOVE_MEMBER_BY_GROUP)
    Call<BaseRspInfo<String>> imRemoveMemberByGroup(@Body GroupSetRequestInfo params);

    /**
     * 解散群
     */
    @POST(APIConstants.DISSOLVE_GROUP)
    Call<BaseRspInfo<String>> imDissolveGroup(@Body Map<String, String> params);

    /**
     * 创建群二维码
     */
    @POST(APIConstants.CREATE_GROUP_QRCODE)
    Call<BaseRspInfo<GroupQrcode>> imCreateGroupQrcode(@Body Map<String, String> params);

    /**
     * 通过群二维码加群
     */
    @POST(APIConstants.JOIN_GROUP_BY_QRCODE)
    Call<BaseRspInfo<GroupInfo>> imJoinGroupByQrcode(@Body Map<String, String> params);

    /**
     * 变更群主
     */
    @POST(APIConstants.ALTERATION_GROUP_OWNER)
    Call<BaseRspInfo<String>> imAlterationGroupOwner(@Body Map<String, String> params);

    /**
     * 发群公告
     */
    @POST(APIConstants.MODIFY_GROUP_NOTICE)
    Call<BaseRspInfo<String>> imModifyGroupNotice(@Body Map<String, String> params);

    /**
     * 获取群公告
     */
    @POST(APIConstants.GET_GROUP_NOTICE)
    Call<BaseRspInfo<GroupNoticeBean>> imGetGroupNotice(@Body Map<String, String> params);

    /**
     * 根据appid获取通知列表
     */
    @GET(APIConstants.GET_NOTICE_LIST)
    Call<BaseRspInfo<List<NoticeInfo>>> imGetNoticeList(@QueryMap Map<String, String> params);

    /**
     * 获取IMTOKEN
     */
    @POST(APIConstants.GET_IM_REFRESH_TOKEN)
    Call<BaseRspInfo<IMLoginBean>> imGetRefreshToken();


    //******************************IM相关接口↑↑↑↑↑↑↑*****************************************

}