package com.gome.work.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 本地SharedPreference工具类
 */
public class PreferenceUtil {
    /** preferences名称 */
    private static final String PREFERENCENAME = "preference";
	/** 静态字段 */
    private static PreferenceUtil preferenceManager = null;
    /** 静态字段 */
    private SharedPreferences preferences = null;

    /** 私有构造函数 */
    private PreferenceUtil(Context context) {
        /** 其他应用可以读取 */
        this.preferences = context.getSharedPreferences(PREFERENCENAME,Context.MODE_PRIVATE);
    }
    
    public SharedPreferences getSharedPreferences(){
    	return preferences;
    }

    /**
     * 单例获取CmPreferenceManager
     * 
     */
    public static PreferenceUtil getInstance(Context context) {
        if (preferenceManager != null) {
            return preferenceManager;
        }
        synchronized (PreferenceUtil.class) {
            if (preferenceManager == null) {
                preferenceManager = new PreferenceUtil(context);
            }
        }
        return preferenceManager;
    }

    /**
     * 获取值
     * 
     * @param enumPreference
     * @return
     */
    public String getString(String key,String defaultValue) {
    	return preferences.getString(key,defaultValue);
    }
    public int getInt(String key,int defaultValue) {
    	return preferences.getInt(key,defaultValue);
    }

	public boolean getBoolean(String key, boolean defaultValue) {
		return preferences.getBoolean(key, defaultValue);
	}

    /**
     * 设置值
     */
    public void setString(String key, String value) {
    	Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    
    public void setInt(String key, int value) {
    	Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    
    public void setBoolean(String key, boolean value) {
    	Editor editor = preferences.edit();
    	editor.putBoolean(key, value);
    	editor.commit();
    }
    
    public static String getPreferencename() {
		return PREFERENCENAME;
	}
    


}
