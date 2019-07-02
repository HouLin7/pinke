package com.gome.work.core.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created by tanxingchun on 2016/9/27.
 */
public class AppBaseUtils {
    public static boolean isAppProcess(Context context) {
        int pid = android.os.Process.myPid();
        String packageName = context.getPackageName();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid && info.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            //通过当前的包名获取包的信息
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static String getRunningPackageName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            //通过当前的包名获取包的信息
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isRunningForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        // 枚举进程
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName.equals(context.getApplicationInfo().processName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
