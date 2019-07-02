
package com.gome.work.common.utils;


import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.text.TextUtils;

import static com.gome.work.core.SystemFramework.getInstance;

/**
 *
 */
public class AppUtils {
    private static final long MIN_DELAY_TIME = 1000;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    private static String channel;
    /**
     * 判断是否是App的进程
     *
     * @param context App上下文
     * @return true是, false不是
     */


    /**
     * final Context context ：调用该方法的Context实例 long milliseconds ：震动的时长，单位是毫秒
     * long[] pattern ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     */

    public static void vibrate(final Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    public static void vibrate(final Context context, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }

    /**
     * 根据当前手机的情景模式进行相关操作
     */
    public static void dealRingerMode() {
        AudioManager am = (AudioManager) getInstance().getGlobalContext()
                .getSystemService(Context.AUDIO_SERVICE);
        final int ringerMode = am.getRingerMode();
        switch (ringerMode) {
            case AudioManager.RINGER_MODE_NORMAL:
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getInstance()
                        .getGlobalContext(), notification);
                r.play();
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                AppUtils.vibrate(getInstance().getGlobalContext(), 500);
                break;
            case AudioManager.RINGER_MODE_SILENT:
                break;
        }
    }

    public static String getVersionName() {
        String mVersionName = "";
        try {
            mVersionName =
                    getInstance()
                            .getGlobalContext()
                            .getPackageManager()
                            .getPackageInfo(
                                    getInstance().getGlobalContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return mVersionName;
    }

    public static String getVersionCode() {
        int code = 0;
        try {
            code =
                    getInstance()
                            .getGlobalContext()
                            .getPackageManager()
                            .getPackageInfo(
                                    getInstance().getGlobalContext().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return String.valueOf(code);
    }

    /**
     * SDK 版本号
     *
     * @return
     */
    public static String getSdkVersion() {
        String sdk = android.os.Build.VERSION.SDK;
        if (TextUtils.isEmpty(sdk)) {
            sdk = "sdk";
        }
        return sdk;
    }

    public static int getSDKVersionInt() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * android 版本号
     *
     * @return
     */
    public static String getSystemVersion() {
        String system = android.os.Build.VERSION.RELEASE;
        if (TextUtils.isEmpty(system)) {
            system = "os_info";
        }
        return system;
    }

    /**
     * 手机版本号
     *
     * @return
     */
    public static String getModelVersion() {
        String model = android.os.Build.MODEL;
        if (TextUtils.isEmpty(model)) {
            model = "model";
        }
        return model;
    }


    /**
     * 获取渠道名称
     *
     * @return
     */
    public static String getChannel() {
        if (!TextUtils.isEmpty(channel)) return channel;
        channel = "XF_Guanwang";
        return channel;
    }


    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    /**
     * 获取占位符值
     *
     * @param context
     * @param key
     * @return
     */
    public static String getManifestPlaceholders(Activity context, String key) {
        ActivityInfo activityInfo = null;
        try {
            activityInfo = context.getPackageManager().getActivityInfo(context.getComponentName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (activityInfo == null) {
            return null;
        }
        return activityInfo.metaData.getString(key);
    }

    public static String getManifestPlaceholders(Application context, String key) {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (applicationInfo == null || TextUtils.isEmpty(applicationInfo.metaData.getString(key)))
            return null;
        String value = applicationInfo.metaData.getString(key);
        if (TextUtils.isEmpty(value))
            return null;
        return value.replace("appid=", "").replace("appkey=","");
    }

}
