
package com.gome.work.core.net;

public interface APIConstants {

    /**
     * 获取验证码
     */
    String GET_CAPTCHA = "communal/verify/sms";

    /**
     * 更新头像
     */

    String UPDATE_USER_AVATA = "app/user/headportrait.do";

    /**
     * 获取好友列表
     */
    String GET_FRIENDS = "app/user/friends.do";
    /**
     * 搜索好友列表
     */
    String GET_SEARCH_FRIENDS_LIST = "app/friends/search.do";

    /**
     * 添加好友
     */
    String ADD_FRIEND = "app/user/friends/add.do";

    /**
     * 删除好友
     */
    String REMOVE_FRIEND = "app/user/friends/remove.do";


    /**
     * 搜索用户
     */
    String SEARCH_USER = "app/user/search.do";

    /**
     * 搜索部门
     */
    String SEARCH_DEPT = "app/department/search.do";


    /**
     * 获取某一账号的所属数据源
     */
    String POST_FEED_BACK = "app/gateway/mobile/saveOrDeleteAppPersonalSuggest.do";


    /**
     * 创建群聊
     */
    String CREATE_GROUP = "app/groups/create.do";

    /**
     * 修改群信息
     */
    String UPDATE_GROUP = "app/groups/modify.do";



    /**
     * 获取某个群的群信息
     */
    String GET_GROUP_INFO = "app/groups/show/{groupId}.do";
    /**
     * 通过群二维码获取某个群的群信息
     */
    String GET_GROUP_INFO_BY_QRCODE = "app/groups/groupInfo/qrcode.do";

    /**
     * 获取群成员列表,分页
     */
    String GET_MEMBER_LIST_BY_PAGE = "app/groups/members/show.do";

    /**
     * 解散群组
     */
    String DISSOLVE_GROUP = "app/groups/dismiss.do";

    /**
     * .创建群二维码
     */
    String CREATE_GROUP_QRCODE = "app/groups/create_qrcode.do";
    /**
     * 通过群二维码加群
     */
    String JOIN_GROUP_BY_QRCODE = "app/groups/join_by_qrcode.do";

    /**
     * 退出群组
     */
    String QUIT_GROUP = "app/groups/quit.do";

    /**
     * 移除群成员
     */
    String REMOVE_MEMBER_BY_GROUP = "app/groups/remove_members.do";

    /**
     * 添加群成员
     */
    String ADD_MEMBERS = "app/groups/invite.do";

    /**
     * 主动加群
     */
    String JOIN_GROUP = "app/groups/join.do";
    /**
     * 变更群主
     */
    String ALTERATION_GROUP_OWNER = "app/groups/transfer_ownership.do";
    /**
     * 发群公告
     */
    String MODIFY_GROUP_NOTICE = "app/groups/notice/modify.do";
    /**
     * 获取群公告
     */
    String GET_GROUP_NOTICE = "app/groups/notice/show.do";



    /**
     * APP请求授权
     */
    String GET_REQUEST_GRANT = "app/oauth/request_grant.do";

    /**
     * APP执行授权
     */
    String GRANT_ACCESS_TOKEN = "app/oauth/grant_access_token.do";

    /**
     * APP执行登录
     */
    String GET_REQUEST_GRANT_LOGIN = "app/oauth/scanLogin.do";

    /**
     * 获取应用分类
     */
    String GET_APP_CATEGORY = "app_shop/category/show.do";

    /**
     * 获取用户详情
     */
    String GET_CATEGORY_APP_LIST = "app_shop/category/apps/show/{categoryCode}.do";

    /**
     * 获取全部应用
     */
    String GET_APP_MARKET_LIST = "app_shop/apps/show.do";

    /**
     * 获取应用分类排行列表
     */
    String GET_APP_SORT_LIST = "app_shop/apps/hotlist/show.do";


    /**
     * 获取应用评论
     */
    String GET_APP_COMMENT_LIST = "app_shop/comment/show.do";

    /**
     * 发表评论
     */
    String SUBMIT_COMMENT_DATA = "app_shop/comment/create.do";

    /**
     * 收藏应用
     */
    String GET_APP_FAVORITE_DATA = "app_shop/apps/favorites/add.do";

    /**
     * 点赞
     */
    String GET_APP_PRAIS_DATA = "app_shop/apps/praise.do";

    /**
     * 取消收藏应用
     */
    String CANCEL_APP_FAVORITE_DATA = "app_shop/apps/favorites/remove.do";

    /**
     * 取消点赞
     */
    String CANCEL_APP_PRAIS_DATA = "app_shop/apps/praise_cancel.do";

    /**
     * 上传文件接口
     */
    String UPLOAD_FILE = "app/gfs/uploadFileGfsNew.do";


    /**
     * 日程查询接口
     */
    String GET_SCHEDULE_LIST="app/gateway/mobile/selectScheduleList.do";
    /**
     * 日程-查询有日程的日期接口
     */
    String GET_SCHEDULE_DATE_LIST="app/gateway/mobile/selectScheduleDateList.do";
    /**
     * 日程-日程添加修改接口
     */
    String SAVE_OR_UPDATE_SCHEDULE="app/gateway/mobile/saveOrUpdateSchedule.do";
    /**
     * 日程-日程详情接口
     */
    String GET_SCHEDULE_DETAIL="app/gateway/mobile/selectScheduleById.do";
    /**
     * 日程-日程选择提前提醒时间的接口
     */
    String GET_SCHEDULE_REMIND_MINUTE="app/gateway/mobile/selectScheduleremindMinuteJson.do";
    /**
     * 日程-日程删除接口
     */
    String DELETE_SCHEDULE="app/gateway/mobile/deleteScheduleById.do";

    /**
     * 获取微应用图标角标数
     */
    String GET_APP_BADGE_COUNT="app_shop/apps/badge_count/{appId}.do";

}
