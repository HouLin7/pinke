package com.gome.work.core.event.model;

import java.io.Serializable;

/**
 * 通用事件模型
 */

public class EventInfo implements Serializable {

    public final static int FLAG_TITLE_BAR_HOME_ICON_CLICK = 1;

    public final static int FLAG_REQUEST_FILE_DOWNLOAD = 2;

    public final static int FLAG_REQUEST_FILE_EXPLORE = 3;

    public final static int FLAG_OPEN_FILE_DOWNLOAD_TASK_LIST = 5;

    public final static int FLAG_TITLE_BAR_SCAN_ICON_CLICK = 6;

    public final static int FLAG_TITLE_BAR_CAST_ICON_CLICK = 7;

    /**
     * 用户选择
     */
    public final static int FLAG_USER_CHOOSE = 8;

    public final static int FLAG_USER_DETAIL = 9;

    /**
     * 文件选择
     */
    public final static int FLAG_FILE_CHOOSE = 10;


    /**
     * 转发
     */
    public final static int FLAG_FORWARDING = 11;


    /**
     * 位置信息更新
     */
    public final static int FLAG_LOCATION_RECEIVE= 12;


    /**
     * 文件选取事件,做事件统计用
     */
    public final static int FLAG_EVENT_FILE_PICKER = 20;

    /**
     * 主界面tab 代办 角标更新
     */
    public final static int FLAG_EVENT_MAIN_TAB_OA_BADGE_UPDATE = 21;
    /**
     * 主界面tab 会话列表未读消息数 角标更新
     */
    public final static int FLAG_EVENT_MAIN_TAB_CHAT_COUNT_UPDATE = 22;

    /**
     * 当前登录者的用户数据发生变化，比如头像更新了
     */
    public final static int FLAG_LOGIN_USER_INFO_CHANGED = 23;

    /**
     * 用户好友发生变化事件
     */
    public final static int FLAG_USER_FRIENDS_CHANGED = 24;

    /**
     * 查看用户资料
     */
    public final static int FLAG_USER_DETAIL_VIEW = 25;

    /**
     * 选择图库
     */
    public final static int FLAG_PHOTO_CHOOSE = 26;

    /**
     * 人脸识别
     */
    public final static int FLAG_FACE_ID_REQUEST = 27;



    /**  聊天部分 ************************/

    /**
     * IM 回执消息（单聊已读未读）
     */
    public final static int FLAG_CHAT_READ_MESSAGE = 101;

    /**
     * IM 文件上传
     */
    public final static int FLAG_CHAT_FILE_MESSAGE_UPLOAD = 102;

    /**
     * IM 刷新会话列表
     */
    public final static int FLAG_CHAT_LIST_UPDATE = 103;
    /**
     * IM发送图片 相册选取
     */
    public final static int FLAG_CHAT_KEY_BROAD_PICKER = 104;
    /**
     * IM发送图片 拍照
     */
    public final static int FLAG_CHAT_KEY_BROAD_CAMERA = 105;

//    /**
//     * token错误
//     */
//    public final static int FLAG_IM_TOKEN_ERROR = 106;

    /**
     * 当token过期或者token错误时触发
     */
    public final static int FLAG_IM_TOKEN_TIME_OUT = 106;

    /**
     * IM 选择回执消息
     */
    public final static int FLAG_CHAT_MESSAGE_TYPE_RECEIPT = 107;
    /**
     * IM 关闭聊天页面
     */
    public final static int FLAG_CHAT_CLOSE_CHAT_MESSAGE_PAGE = 108;

    /**
     * IM 打开群聊
     */
    public final static int FLAG_IM_TO_CHAT_MESSAGE = 109;


    /**
     * IM 聊天界面点击文件跳转到下载页面
     */
    public final static int FLAG_CHAT_DOWN_LOAD_FILE = 110;

    /**
     * IM 选择定位
     */
    public final static int FLAG_CHAT_MESSAGE_TYPE_LOCATION = 111;

    /**
     * IM 连接状态改变后触发
     */
    public final static int FLAG_IM_CONNECT_STATUS = 113;


    /**
     * IM 监控语音播放状态改变
     */
    public final static int FLAG_VOICE_PLAYER_STATUS = 115;

    /**
     * IM 语音 听筒及扬声器
     */
    public final static int FLAG_VOICE_SENDER_EVENT = 116;
    /**
     * IM 聊天界面收到新消息时触发
     */
    public final static int FLAG_IM_NEW_MESSAGE_COMING = 117;
    /**
     * IM 聊天界面自己发消息时触发
     */
    public final static int FLAG_IM_SEND_OUT_MESSAGE = 118;
    /**
     * IM 消息成功发送到服务器后触发
     */
    public final static int FLAG_IM_SEND_MESSAGE_COMPLETE = 119;
    /**
     * IM 消息发送服务器失败后触发
     */
    public final static int FLAG_IM_SEND_MESSAGE_ERROR = 120;

    /**
     * IM 接收到推送通知后触发存库
     */
    public final static int FLAG_IM_NOTICE_SAVE_TO_DB = 121;

    /**
     * IM 会话列表通知设置全部已读
     */
    public final static int FLAG_IM_NOTICE_IS_READ_TO_DB = 123;
    /**
     * IM 会话列表通知设置置顶
     */
    public final static int FLAG_IM_NOTICE_IS_TOP_TO_DB = 124;
    /**
     * IM 会话列表通知设置免打扰
     */
    public final static int FLAG_IM_NOTICE_IS_SHIELD_TO_DB = 125;

//    /**
//     * IM 打开单聊
//     */
//    public final static int FLAG_IM_TO_CHAT_MESSAGE_SINGLE = 127;

    /**
     * 强制用户退出登录状态
     */
    public final static int FLAG_FORCE_USER_LOGOUT = 126;

    /**
     * IM 点击通知跳转到会话列表
     */
    public final static int FLAG_IM_NOTIFICATION_INFO = 128;

    /**
     * im 会话列表搜索
     */
    public final static int FLAG_IM_SEARCH_USER = 129;
    /**
     * im @输入监听
     */
    public final static int FLAG_IM_INPUT_TEXT_LISTENER = 130;
    /**
     * 跳转日程详情
     */
    public final static int FLAG_SCHEDULE_DETIAL = 131;
    /**
     * IM 底部扩展 跳转单据
     */
    public final static int FLAG_CHAT_MESSAGE_TYPE_ORDER = 132;

//    /**
//     * IM 机器人
//     */
//    public final static int FLAG_IM_TO_CHAT_MESSAGE_ROBOT = 133;

    /**
     * IM 发送单据
     */
    public final static int FLAG_IM_TO_CHAT_MESSAGE_SEND_BILL = 134;


    /**
     * IM 发送机器人消息
     */
    public final static int FLAG_IM_TO_CHAT_MESSAGE_SEND_ROBOT_MESSAGE = 135;


    /**
     * IM 打开聊天 携带 会话信息
     */
    public final static int FLAG_IM_TO_CHAT_WITH_CHATBEAN = 200;

    /**
     * IM 打开单聊 携带 用户信息
     */
    public final static int FLAG_IM_TO_CHAT_WITH_USERBEAN = 201;
    private static final long serialVersionUID = 7051242233936942299L;


    public int what;

    public Object data;

    public EventInfo(int what, Object data) {
        this.what = what;
        this.data = data;
    }

    public static EventInfo obtain(int flag) {
        return new EventInfo(flag);
    }

    public EventInfo(int what) {
        this(what, null);
    }
}
