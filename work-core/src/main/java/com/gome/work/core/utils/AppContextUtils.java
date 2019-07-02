package com.gome.work.core.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import com.gome.work.core.model.appmarket.AppInfo;

import java.util.ArrayList;
import java.util.List;


public class AppContextUtils {
	/**
	 * 获取系统中所有应用信息， 并将应用软件信息保存到list列表中。
	 **/
	public static List<AppInfo> getAllApps(Context context) {
		PackageManager mPackageManager = context.getPackageManager();
		List<AppInfo> list = new ArrayList<AppInfo>();
		AppInfo myAppInfo;
		// 获取到所有安装了的应用程序的信息，包括那些卸载了的，但没有清除数据的应用程序
		if (mPackageManager != null) {
			List<PackageInfo> packageInfos = mPackageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
			for (PackageInfo info : packageInfos) {
				myAppInfo = new AppInfo();
				// 拿到包名
				String packageName = info.packageName;
				// 拿到应用程序的信息
				ApplicationInfo appInfo = info.applicationInfo;
				// 拿到应用程序的图标
				Drawable icon = appInfo.loadIcon(mPackageManager);
				// 拿到应用程序的大小
				// long codesize = packageStats.codeSize;
				// Log.i("info", "-->"+codesize);
				// 拿到应用程序的程序名
				String appName = appInfo.loadLabel(mPackageManager).toString();
				myAppInfo.setPackageName(packageName);
				myAppInfo.setAppName(appName);

				if (filterApp(appInfo)) {
					myAppInfo.setSystemApp(false);
				} else {
					myAppInfo.setSystemApp(true);
				}
				list.add(myAppInfo);
			}
		}
		return list;
	}
	
	/** 根据包名获取mainActivity的名字
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static String getMainActivity(Context context , String packageName){
		if(TextUtils.isEmpty(packageName))
			return null;
		PackageManager mPackageManager = context.getPackageManager();
		 Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
      List<ResolveInfo> mApps = mPackageManager.queryIntentActivities(mainIntent, 0);
      for (int i = 0; i < mApps.size(); i++) {  
           ResolveInfo resolveInfo = mApps.get(i);
          String packagename = resolveInfo.activityInfo.packageName;
          if(packageName.equals(packagename)){
        	   String activityName = resolveInfo.activityInfo.name;
        	  return activityName;
          }
      }  
		return null;
	}
	
	/**
	 * 判断某一个应用程序是不是系统的应用程序， 如果是返回true，否则返回false。
	 */
	private static boolean filterApp(ApplicationInfo info) {
		// 有些系统应用是可以更新的，如果用户自己下载了一个系统的应用来更新了原来的，它还是系统应用，这个就是判断这种情况的
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			return true;
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {// 判断是不是系统应用
			return true;
		}
		return false;
	}
}
