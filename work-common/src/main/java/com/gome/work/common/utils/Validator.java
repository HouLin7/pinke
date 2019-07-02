
package com.gome.work.common.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenhang01 on 2016/6/16.
 */
public class Validator {
    /**
     * 正则表达式：验证用户名
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";

    /**
     * 正则表达式：验证密码
     */
    public static final String VERIFY_PASSWORD = "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,20}$";
    /**
     * 正则表达式：验证密码
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$";

    /**
     * 正则表达式：验证手机号第二种方式
     */
    public static final String REGEX_MOBILE_SIMPLE = "^(1(3|4|5|7|8))[0-9]{9}$";

//    /**
//     * 正则表达式：验证手机号
//     */
//    public static final String REGEX_MOBILE = "^((13[0-9])|17[0-9]|(15[^4,\\D])|(18[0-9]))\\d{8}$";

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

    /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 正则表达式：验证IP地址
     */
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

    /**
     * 校验用户名
     *
     * @param username
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUsername(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }

    /**
     * 校验密码
     *
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        if(TextUtils.isEmpty(mobile)) {
            return false;
        }
        return Pattern.matches(REGEX_MOBILE_SIMPLE, mobile);
    }
//    /**
//     * 校验手机号长度
//     *
//     * @param mobile
//     * @return 校验通过返回true，否则返回false
//     */
//    public static boolean isMobileSimpleMatch(String mobile) {
//        if(TextUtils.isEmpty(mobile)) {
//            return false;
//        }
//        return Pattern.matches(REGEX_MOBILE_SIMPLE, mobile);
//    }

    /**
     * 校验图片验证码
     * @param picCaptcha
     * @return
     */
    public static boolean isPicCaptcha(String picCaptcha){
        if (TextUtils.isEmpty(picCaptcha)){
            return false;
        }
        return picCaptcha.trim().length() == 4;
    }

    /**
     * 校验短信验证码
     * @param smsCaptcha
     * @return
     */
    public static boolean isSmsCaptcha(String smsCaptcha){
        if (TextUtils.isEmpty(smsCaptcha)){
            return false;
        }
        return smsCaptcha.trim().length() == 6;
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        if(TextUtils.isEmpty(email)){
            return false;
        }
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 校验汉字
     *
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

    /**
     * 校验身份证
     *
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    /**
     * 校验URL
     *
     * @param url
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(REGEX_URL, url);
    }

    /**
     * 校验IP地址
     *
     * @param ipAddr
     * @return
     */
    public static boolean isIPAddr(String ipAddr) {
        return Pattern.matches(REGEX_IP_ADDR, ipAddr);
    }

    public static boolean isBlank(String pwd) {
        Pattern pattern = Pattern.compile("(\\S)*");
        Matcher m = pattern.matcher(pwd);
        return m.matches();
    }

//    /**
//     * 检查密码格式是否正确
//     * @return
//     */
//    public static boolean validatePassword(String password) {
//        if (password.length() < 6 || password.length() > 20) {
//            UiUtils.showToast("请输入正确长度的密码");
//            return false;
//        }
//        if (password.contains(" ") || password.contains(" ")) {
//            UiUtils.showToast("密码不能包含空格");
//            return false;
//        }
//        char[] chars = password.toCharArray();
//        boolean containChar = false, containDigit = false;
//        for (char cr : chars) {
//            if (!containDigit && Character.isDigit(cr)) {
//                containDigit = true;
//            }
//            if (!containChar && Character.isLetter(cr)) {
//                containChar = true;
//            }
//        }
//        if (containChar && containDigit) {
//            return true;
//        } else {
//            UiUtils.showToast("密码必须是6-20位字母、数字组合");
//        }
//        return false;
//    }

    public static boolean newValidatePassword(String password){
        if (TextUtils.isEmpty(password)) return false;
        if (password.length() < 6 || password.length() > 20) {
            UiUtils.showToast("请输入正确长度的密码");
            return false;
        }
//        if (StringUtils.isEmpty(password)) {
//            UiUtils.showToast("密码不能包含空格");
//            return false;
//        }
        if (Pattern.matches(VERIFY_PASSWORD, password)){
            if (Pattern.matches("^[\\S]*$", password)){
                return true;
            }else {
                UiUtils.showToast("密码不能包含空格");
                return false;
            }
        }
        UiUtils.showToast("密码必须是6-20位字母、数字组合");
        return false;
    }

    /**
     * 验证密码长度
     * @return
     */
    public static boolean validatePasswordLength(String password){
        if (TextUtils.isEmpty(password)) return false;
        return password.length() > 5 && password.length() <= 20;
    }
}
