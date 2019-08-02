
package com.gome.work.core.utils;

import android.text.TextUtils;
import com.gome.work.core.Constants;
import com.gome.work.core.model.AccessTokenInfo;
import com.gome.work.core.model.UserInfo;
import com.gome.work.core.utils.encrypt.EncryptedPreferencesValue;
import com.google.gson.Gson;

//SharedPreferences中写入、读取对象操作
public class SharedPreferencesHelper {

    public static String getString(String key) {
        return EncryptedPreferencesValue.getInstance().getString(key, "");
    }

    public static String getString(String key, String defaultValue) {
        return EncryptedPreferencesValue.getInstance().getString(key, defaultValue);
    }

    public static boolean commitString(String key, String value) {
        return EncryptedPreferencesValue.getInstance().getEdit().putString(key, value).commit();
    }

    public static long getLong(String key, long data) {
        return EncryptedPreferencesValue.getInstance().getLong(key, data);
    }

    public static boolean commitLong(String key, long data) {
        return EncryptedPreferencesValue.getInstance().getEdit().putLong(key, data).commit();
    }

    public static int getInt(String key) {
        return EncryptedPreferencesValue.getInstance().getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        return EncryptedPreferencesValue.getInstance().getInt(key, defaultValue);
    }

    public static boolean commitInt(String key, int value) {
        return EncryptedPreferencesValue.getInstance().getEdit().putInt(key, value).commit();
    }

    public static boolean getBoolean(String key) {
        return EncryptedPreferencesValue.getInstance().getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return EncryptedPreferencesValue.getInstance().getBoolean(key, defaultValue);
    }

    public static boolean commitBoolean(String key, boolean value) {
        return EncryptedPreferencesValue.getInstance().getEdit().putBoolean(key, value).commit();
    }

    public static void saveAccessTokenInfo(AccessTokenInfo accessTokenInfo) {
        Gson gson = new Gson();
        String data = gson.toJson(accessTokenInfo);
        commitString(Constants.PreferKeys.ACCESS_TOKEN_INFO, data);
        commitString(Constants.PreferKeys.ACCESS_TOKEN, accessTokenInfo.token);
    }

    public static AccessTokenInfo getAccessTokenInfo() {
        AccessTokenInfo result = new AccessTokenInfo();
        String data = SharedPreferencesHelper.getString(Constants.PreferKeys.ACCESS_TOKEN_INFO, "");
        if (!TextUtils.isEmpty(data)) {
            Gson gson = new Gson();
            result = gson.fromJson(data, AccessTokenInfo.class);
        }
        return result;
    }


    public static UserInfo getUserDetailInfo() {
        UserInfo result = new UserInfo();
        String data = SharedPreferencesHelper.getString(Constants.PreferKeys.LOGIN_USER_INFO, "");
        if (!TextUtils.isEmpty(data)) {
            Gson gson = new Gson();
            result = gson.fromJson(data, UserInfo.class);
        }
        return result;
    }


    public static void saveUserDetailInfo(UserInfo userDetailBean) {
        if (userDetailBean == null) {
            return;
        }
        Gson gson = new Gson();
        String data = gson.toJson(userDetailBean);
        commitString(Constants.PreferKeys.LOGIN_USER_INFO, data);
    }


    public static void clearUserInfo() {
        commitString(Constants.PreferKeys.DEVICE_PUSH_TOKEN, "");
        commitString(Constants.PreferKeys.CHANNEL_LIST, "");
        commitString(Constants.PreferKeys.NEWS_LIST, "");
        commitString(Constants.PreferKeys.BANNER_LIST, "");
        commitString(Constants.PreferKeys.LOGIN_USER_INFO, "");
        commitString(Constants.PreferKeys.ACCESS_TOKEN_INFO, "");
        commitString(Constants.PreferKeys.ACCESS_TOKEN, "");
        commitString(Constants.PreferKeys.JSON_WORK_BANNER, "");
    }


    public static String getRequestToken() {
        return getString(Constants.PreferKeys.REQUEST_TOKEN, "");
    }


    public static String getAccessToken() {
        return getString(Constants.PreferKeys.ACCESS_TOKEN, "");
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    public static boolean isLogin() {
        return !TextUtils.isEmpty(getAccessToken());
    }


    public static boolean removeKey(String key) {
        return EncryptedPreferencesValue.getInstance().getEdit().remove(key).commit();
    }

    public static void clearData() {
        EncryptedPreferencesValue.getInstance().getEdit().clear().commit();
    }


}
