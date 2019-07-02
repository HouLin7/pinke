package com.gome.work.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by songzhiyang on 2017/2/21.
 */

public class Authority {
    private static Map<Integer,Builder> sRequestCode2AuthorityBuilder = new HashMap<>();

    public void checkPermission(Activity activity,int requestCode) {
        Builder builder = sRequestCode2AuthorityBuilder.get(requestCode);
        if (builder == null) {
            return;
        }
        List<String> permissions = builder.permissionList;
        if (permissions.size() == 0) {
            return;
        }
        //手机版本低于6.0 直接执行逻辑
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (builder.commonOperation != null) {
                builder.commonOperation.doCommonOperation();
            }
            return;
        }
        //target 6.0 以下 直接执行逻辑代码 让系统自己判断
        if (activity.getApplicationInfo().targetSdkVersion < Build.VERSION_CODES.M) {
            if (builder.commonOperation != null) {
                builder.commonOperation.doCommonOperation();
            }
            return;
        }
        for (int i=0;i<permissions.size();i++) {
            if (ContextCompat.checkSelfPermission(activity,permissions.get(i)) == PackageManager.PERMISSION_GRANTED
                    && PermissionChecker.checkSelfPermission(activity,permissions.get(i)) == PackageManager.PERMISSION_GRANTED) {
                //允许权限
            } else {
                //不允许权限
                builder.needRequestPermissionList.add(permissions.get(i));
            }
        }
        List<String> needRequestPermissionList = builder.needRequestPermissionList;
        if (needRequestPermissionList.size() == 0) {
            //当所有权限都符合的情况下 执行操作
            if (builder.commonOperation != null) {
                builder.commonOperation.doCommonOperation();
            }
            return;
        }
        String[] permissionStrs = needRequestPermissionList.toArray(new String[needRequestPermissionList.size()]);
        ActivityCompat.requestPermissions(activity,permissionStrs,requestCode);
    }

    @TargetApi(23)
    public static boolean onResultPermmison(Activity activity,int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return false;
        }
        Builder builder = sRequestCode2AuthorityBuilder.get(requestCode);
        if (builder == null) {
            return false;
        }
        if (permissions.length == 0) {
            return false;
        }
        for (int i=0;i<permissions.length;i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                //当回调为允许的情况下 需要校验AppOps是否也给权限了 没有的话GG
                AppOpsManager appOpsManager = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);
                int checkOp = appOpsManager.checkOp(AppOpsManagerCompat.permissionToOp(permissions[i]), activity.getApplicationInfo().uid, activity.getPackageName());
                if (checkOp != AppOpsManager.MODE_ALLOWED) {
                    // 权限被拒绝了
                    builder.foreverDenyPermissionList.add(permissions[i]);
                }
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,permissions[i])) {
                    //如果用户没有勾选不再提醒，返回true
                    builder.commonDenyPermissionList.add(permissions[i]);
                } else {
                    //用户勾选了不再提醒，或者直接在应用权限设置页面选择关闭权限
                    builder.foreverDenyPermissionList.add(permissions[i]);
                }
            }
        }
        if (builder.foreverDenyPermissionList.size() > 0) {
            //只要有一个权限被永久拒绝 就提示永久拒绝的文案
            if (builder.foreverDenyTips != null) {
                builder.foreverDenyTips.doForeverDenyTips();
            }
        } else {
            if (builder.commonDenyPermissionList.size() > 0) {
                //只要有一个没有赋予权限 就提示普通拒绝文案
                if (builder.commonDenyTips != null) {
                    builder.commonDenyTips.doCommonDenyTips();
                }
            } else {
                //全部都赋予了权限
                if (builder.commonOperation != null) {
                    builder.commonOperation.doCommonOperation();
                }
            }
        }
        sRequestCode2AuthorityBuilder.remove(requestCode);
        return true;
    }

    public static class Builder {
        private int requestCode;
        private CommonOperation commonOperation;
        private CommonDenyTips commonDenyTips;
        private ForeverDenyTips foreverDenyTips;
        private List<String> permissionList = new ArrayList<>();
        List<String> needRequestPermissionList = new ArrayList<>();
        List<String> commonDenyPermissionList = new ArrayList<>();
        List<String> foreverDenyPermissionList = new ArrayList<>();

        public Builder setRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        public Builder setCommonDenyTips(CommonDenyTips commonDenyTipses) {
            this.commonDenyTips = commonDenyTipses;
            return this;
        }

        public Builder setForeverDenyTips(ForeverDenyTips foreverDenyTipses) {
            this.foreverDenyTips = foreverDenyTipses;
            return this;
        }

        public Builder setPermissions(String ... permissions) {
            permissionList.addAll(Arrays.asList(permissions));
            return this;
        }

        public Builder setPermissions(List<String> permissions) {
            permissionList.addAll(permissions);
            return this;
        }

        public Builder setCommonOperation(CommonOperation commonOperations) {
            this.commonOperation = commonOperations;
            return this;
        }

        public Authority build() {
            Authority utils = new Authority();
            sRequestCode2AuthorityBuilder.put(requestCode,this);
            return utils;
        }
    }

    public interface CommonDenyTips {
        void doCommonDenyTips();
    }

    public interface ForeverDenyTips {
        void doForeverDenyTips();
    }

    public interface CommonOperation {
        void doCommonOperation();
    }

//    public static boolean setAppOpsManagerPermission(Activity activity,String permission) {
          //这里无法修改 Google进行了权限校验 只有System目录下的才有该权限
          //<uses-permission android:name="android.permission.UPDATE_APP_OPS_STATS"/>
//        try {
//            AppOpsManager appOpsManager = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);
//            Class clazz = AppOpsManager.class;
//            Method getPermissionOpsCode = clazz.getMethod("permissionToOpCode",String.class);
//            int code = (int)getPermissionOpsCode.invoke(appOpsManager,permission);
//            Method method = clazz.getMethod("setMode",int.class,int.class,String.class,int.class);
//            method.invoke(appOpsManager,code,activity.getApplicationInfo().uid,activity.getPackageName(),AppOpsManagerCompat.MODE_ALLOWED);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkAppOpsPermission(Context context,String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return false;
        }
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        String opsPermission = AppOpsManagerCompat.permissionToOp(permission);
        if (TextUtils.isEmpty(opsPermission)) {
            return false;
        }
        int ops = appOpsManager.checkOp(opsPermission,context.getApplicationInfo().uid,context.getPackageName());
        return ops == AppOpsManager.MODE_ALLOWED;
    }
}
