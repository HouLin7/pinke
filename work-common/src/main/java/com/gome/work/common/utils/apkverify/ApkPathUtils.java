package com.gome.work.common.utils.apkverify;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by chenhang01 on 2016/11/15.
 */

public class ApkPathUtils {

    public static String getApkPath(Context context){
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),PackageManager.GET_META_DATA);
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            return applicationInfo.publicSourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
