package com.gome.work.core.net;

/**
 * 功能： 服务器 返回的错误码统一管理常量类
 * ＊创建者：赵然 on 2017/11/6 15:25
 * ＊
 */

public interface ServerAPIErrorConstant {
    // 部分服务端状态码
    String HAVE_ACTIVATE = "200002";//该用户已激活，请直接登录
    String NO_PERMISSION = "12012";//没有权限
    // TODO: 2017/11/6 解散企业--- BS提供后修改
    String ERROR_COMPANY_DISSOLVE = "209999";//解散企业

    String OPERATE_APPLY_DIALOG_SHOW = "202025";//审批单（同意 拒绝 撤销）不同步
    String OPERATE_APPLY_DIALOG_SHOW2 = "202016";//审批单（同意 拒绝 撤销）不同步
    String SSO = "12006";//用户的同一账号只能同时在一个移动设备上登陆
    String PASSWORD_HAVE_MOMDIFY = "12005"; // 密码已修改,需要重新登录

    String JOIN_GROUP_CHAT_REPET = "200035"; // 重复加入群组

    String PIC_CAPTCHA_INVALID = "200014";//图形验证码无效
    String PIC_CAPTCHA_ERROR = "200011";//图形验证错误
    String SMS_CAPTCHA_ERROR = "200009";//短信验证码错误
    String SMS_CAPTCHA_INVALID = "200010";//短信验证码已过期


    String ILLEGAL_OPERATION = "10001";
    String NOT_LOGGED_ON = "12011";
    String SESSION_EXPIRED = "120113";
    //目前来看 IsDimission与NoPermission 是同一个概率
    String IS_DIMISSION = "12004";//员工离职(对应的是管理员对员工账号删除了) 服务端查不到对应的用户信息

}
