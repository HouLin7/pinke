package com.gome.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommonUtils {

    private final static String DEFAULT_VERSION = "1.0.0";

    public static int getVersionCode(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        int version = 1;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return version;
    }

    public static String getVersionName(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        String version = DEFAULT_VERSION;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return version;
    }

    public static boolean hasSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    public static double round(Double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }

        BigDecimal b = null == v ? new BigDecimal("0.0") : new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private static int sdkVersion = -1;

    public static int getSDKVersion() {
        if (sdkVersion == -1) {
            sdkVersion = Build.VERSION.SDK_INT;
        }
        return sdkVersion;
    }


    public static void installApk(Context context, Uri uri) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            String type = "application/vnd.android.package-archive";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intent.setDataAndType(uri, type);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a meta data ,which defined in the mainfest file.
     *
     * @param context
     * @param key
     * @return
     */
    public static String getMetaDataFromApp(Context context, String key) {
        String value = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString(key);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static boolean isforegroudRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            RunningTaskInfo info = runningTaskInfos.get(0);
            return context.getPackageName().equals(info.topActivity.getPackageName());
        }
        return false;
    }

    public static boolean isAppAlive(Context context) {
        return getAppTaskInfo(context) != null;
    }

    /**
     * Get a task to run my app.
     *
     * @param context
     * @return
     */
    public static RunningTaskInfo getAppTaskInfo(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(100);
        if (runningTaskInfos != null) {
            for (RunningTaskInfo info : runningTaskInfos) {
                if (context.getPackageName().equals(info.topActivity.getPackageName())) {
                    return info;
                }
            }
        }
        return null;
    }

    public static void startAppFromNotifyCenter(Context context, Class<? extends Activity> activityClass,
                                                Class<? extends Activity> mainActivityClass, Bundle data) {
        context.startActivity(getIntentFromNotifyClick(context, activityClass, mainActivityClass, data));
    }

    public static Intent getIntentFromNotifyClick(Context context, Class<? extends Activity> activityClass,
                                                  Class<? extends Activity> mainActivityClass, Bundle data) {
        boolean appAlive = CommonUtils.isAppAlive(context);
        Intent intent = null;
        if (appAlive) {
            intent = new Intent(context, activityClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            intent = new Intent(context, mainActivityClass);
            intent.putExtra("class_name", activityClass);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        }
        if (data != null) {
            intent.putExtras(data);
        }
        return intent;
    }

    public static void startAppFromNotifyCenter(Context context, Class<? extends Activity> activityClass,
                                                Class<? extends Activity> mainActivityClass) {
        startAppFromNotifyCenter(context, activityClass, mainActivityClass, null);
    }

    /**
     * @param context
     * @return
     * @function 判断应用状态，是在前台、后台或者关闭
     */
    public static ApplicationState getApplicationState(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return ApplicationState.STATE_BACKGROUND;
                } else if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return ApplicationState.STATE_FOREGROUND;
                } else if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE) {
                    return ApplicationState.STATE_BACKGROUND;
                }
            }
        }
        return ApplicationState.STATE_CLOSE;
    }

    public enum ApplicationState {
        STATE_BACKGROUND, STATE_FOREGROUND, STATE_CLOSE
    }

    public static boolean isGPSOpen(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // boolean network =
        // locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps) {
            return true;
        }
        return false;
    }

    public static void turnGPSOn(Context context) {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        context.sendBroadcast(intent);

        String provider = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { // if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }
    }


    public static PendingIntent createPendingIntent(Context context, Class<? extends Activity> mainClass, Intent data) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClass(context, mainClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (data != null) {
            intent.putExtras(data);
        }
        PendingIntent pi = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pi;
    }


    public static String encodeUrl(List<Pair<String, String>> contentValues, boolean isUrlEncode) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Pair<String, String> item : contentValues) {
            if (TextUtils.isEmpty(item.second)) {
                continue;
            }
            if (first) {
                first = false;
                // sb.append("?");
            } else {
                sb.append("&");
            }
            if (isUrlEncode) {
                try {
                    sb.append(URLEncoder.encode(item.first, "UTF-8"));
                    sb.append("=");
                    sb.append(URLEncoder.encode(item.second, "UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                sb.append(item.first);
                sb.append("=");
                sb.append(item.second);
            }
        }
        return sb.toString();
    }

    public static int dip2Px(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float px = dp * metrics.density + 0.5f;
        return (int) px;
    }

    public static List<String> getPkgName(Context context, String appName) {
        PackageManager pmManager = context.getPackageManager();
        List<String> result = new ArrayList<String>();
        List<PackageInfo> pkgInfos = pmManager.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        for (PackageInfo info : pkgInfos) {
            String name = (String) info.applicationInfo.loadLabel(pmManager);
            if (name.contains(appName)) {
                result.add(info.packageName);
            }
        }
        return result;
    }

    public static boolean startOtherApp(Context context, String packageName) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mainIntent.setPackage(packageName);
        PackageManager pmManager = context.getPackageManager();
        List<ResolveInfo> resoInfoList = pmManager.queryIntentActivities(mainIntent, 0);
        for (ResolveInfo info : resoInfoList) {
            String pkg = info.activityInfo.packageName;
            String cls = info.activityInfo.name;
            ComponentName component = new ComponentName(pkg, cls);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(component);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public static void startWebIntent(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    public static String getMIMEType(File file) {
        String result = "";
        String fileName = file.getName();
        String var3 = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
        result = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return result;
    }


    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param url
     */
    public static String getMIMEType(String url) {
        String result = "";
        Uri uri = Uri.parse(url);
        String fileName = uri.getLastPathSegment();
        String var3 = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
        result = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return result;
    }

    public static void openFile(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, getMIMEType(file));
        } else {
            intent.setDataAndType(Uri.fromFile(file), getMIMEType(file));
        }
        try {
            context.startActivity(intent);
        } catch (Exception var5) {
            var5.printStackTrace();
            Toast.makeText(context, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }
    }


    public static <T> List<T> setToList(Set<T> sets) {
        List<T> result = new ArrayList<>();
        for (T item : sets) {
            result.add(item);
        }
        return result;
    }


    public static void openAutoStartPermissionGrant(Context context) {
        String system = Build.MANUFACTURER.toLowerCase();
        Intent intent = new Intent();
        if (system.equals("huawei")) {//华为
            ComponentName componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
            intent.setComponent(componentName);
        } else if (system.equals("xiaomi")) {//小米
            ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
            intent.setComponent(componentName);
        }
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }
    }


    public static void shareFile(Context context, File file) {
        Intent share_intent = new Intent();
        Uri uri = ContentUriUtils.getOutputUriForFile(context, file);
        share_intent.setAction(Intent.ACTION_SEND);
        String mimeType = FileUtil.getMimeTypeFromFile(file);
        share_intent.setType(mimeType);
        share_intent.putExtra(Intent.EXTRA_STREAM, uri);
        share_intent = Intent.createChooser(share_intent, "分享");
        context.startActivity(share_intent);

    }

}
