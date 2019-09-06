package com.gome.work.core.net.api;


import com.gome.work.core.model.AccessTokenInfo;
import com.gome.work.core.model.AdBean;
import com.gome.work.core.model.BannerBean;
import com.gome.work.core.model.BaseRspInfo;
import com.gome.work.core.model.CaptchaItem;
import com.gome.work.core.model.CategoryBean;
import com.gome.work.core.model.PostSearchPartnerItem;
import com.gome.work.core.model.PostUserInfo;
import com.gome.work.core.model.RegionItem;
import com.gome.work.core.model.RequestDataInfo;
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
import com.gome.work.core.model.im.GroupSetRequestInfo;
import com.gome.work.core.model.schedule.ScheduleInfo;
import com.gome.work.core.model.schedule.ScheduleRemindInfo;
import com.gome.work.core.net.APIConstants;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 *
 */
public interface ApiService {

    /**
     * 用户登录
     * 参数在HeaderParamsInterceptor拦截器中传递
     *
     * @return
     */
    @POST("auth/login")
    Call<BaseRspInfo<AccessTokenInfo>> login(@Body Map<String, String> params);

    @POST("auth/register")
    Call<BaseRspInfo<UserInfo>> register(@Body Map<String, String> params);

    @POST("auth/reset/password")
    Call<BaseRspInfo<String>> resetPassword(@Body Map<String, String> params);


    @GET("communal/verify/sms")
    Call<BaseRspInfo<CaptchaItem>> getCaptcha(@Query("mobile") String phoneNum);

    /**
     * 获取城市
     */
    @GET("communal/city")
    Call<BaseRspInfo<List<RegionItem>>> getCity(@Query("level") int level);

    /**
     * 获取推荐列表
     */
    @GET("communal/recommend")
    Call<BaseRspInfo<String>> getRecommendList(@Query("position") String pos, @Query("recommend") String type);

    /**
     * 获取广告位
     */
    @GET("communal/adver")
    Call<BaseRspInfo<List<AdBean>>> getAd();

    /**
     * 获取banner数据
     */
    @GET("communal/banner")
    Call<BaseRspInfo<List<BannerBean>>> getBanner(@Query("position") String pos);

    /**
     * 获取启动页数据
     */
    @GET("communal/launcher")
    Call<BaseRspInfo<List<AdBean>>> getLauncherPic();

    /**
     * 获取推荐数据
     */
    @GET("communal/recommend")
    Call<BaseRspInfo<List<AdBean>>> getRecommend();


    /**
     * 修改密码
     */
    @POST("auth/passwd/modify")
    Call<BaseRspInfo<String>> modifyPassword(@Body Map<String, String> params);

    /**
     * 忘记密码
     */
    @POST("auth/passwd/reset")
    Call<BaseRspInfo<String>> forgetPassword(@Body Map<String, String> params);

    /**
     * 系统基础数据字典
     */
    @GET("communal/config/listSysConfig/{type}")
    Call<BaseRspInfo<SysCfgData>> getConfigDataDic(@Path("type") String type);

    /**
     * 发布伴读信息
     */
    @POST("student/seek-partner/release")
    Call<BaseRspInfo<String>> postSearchPartnerInfo(@Body PostSearchPartnerItem params);


    /**
     * 获取我发布的找伴读伴读信息
     */
    @GET("student/seek-partner")
    Call<BaseRspInfo<SearchPartnerItem.ResponseWrapper>> getMyPostSearchPartnerList(@QueryMap Map<String, Object> params);


    /**
     * 获取用户信息
     *
     * @param userId 用户id
     */
    @GET("communal/uinfo/{id}")
    Call<BaseRspInfo<UserInfo>> getUserInfo(@Path("id") String userId);

    /**
     * 修改用户信息
     *
     * @param userInfo 用户id
     */
    @POST("communal/uinfo/reset")
    Call<BaseRspInfo<String>> postUserInfo(@Body PostUserInfo userInfo);


    /**
     * 获取找伴读信息
     *
     * @param params 学科分类
     */
    @GET("student/seek-partner/search")
    Call<BaseRspInfo<SearchPartnerItem.ResponseWrapper>> getSearchPartnerList(@QueryMap Map<String, Object> params);


    /**
     * 获取老师信息列表
     *
     * @param params 学科分类
     */
    @GET("v1/teacher/list")
    Call<BaseRspInfo<UsersRspInfo>> getTeacherList(@QueryMap Map<String, Object> params);

    /**
     * 关注
     * @param params
     * @return
     */
    @POST("communal/relation/follow")
    Call<BaseRspInfo<String>> follow(@Body Map<String, Object> params);

    /**
     * 取消关注
     * @param params
     * @return
     */
    @POST("communal/relation/cancel")
    Call<BaseRspInfo<String>> followCancel(@Body Map<String, Object> params);

    /**
     * 获取粉丝列表
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GET("communal/relation/followers")
    Call<BaseRspInfo<UsersRspInfo>> getFollowers(@Query("pn") int pageIndex,@Query("ps") int pageSize);

    /**
     * 获取好友列表
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GET("communal/relation/friends")
    Call<BaseRspInfo<UsersRspInfo>> getFriends(@Query("pn") int pageIndex,@Query("ps") int pageSize);

    /**
     * 伴读某人
     * @param params
     * @return
     */
    @POST("student/partner/follow")
    Call<BaseRspInfo<String>> partner(@Body Map<String, Object> params);


    /**
     * 取消伴读关系
     * @param params
     * @return
     */
    @POST("student/partner/cancel")
    Call<BaseRspInfo<String>> partnerCancel(@Body Map<String, Object> params);


    /**
     * 获取伴读列表
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GET("student/partner")
    Call<BaseRspInfo<UsersRspInfo>> getPartners(@Query("pn") int pageIndex,@Query("ps") int pageSize);


    /**
     * 更新头像
     */
    @Multipart
    @POST(APIConstants.UPDATE_USER_AVATA)
    Call<BaseRspInfo<String>> updateUserAvatar(@Part MultipartBody.Part file);


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
    Call<BaseRspInfo<AccessTokenInfo>> getRequestGeantLogin(@Body Map<String, String> params);

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
    @POST(APIConstants.CANCEL_APP_PRAISE_DATA)
    Call<BaseRspInfo<String>> cancelAppPraisData(@Body Map<String, String> params);


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
    @POST("communal/file/upload")
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

    //******************************IM相关接口↑↑↑↑↑↑↑*****************************************

}
