package com.gome.work.common.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import com.gome.work.common.R;
import com.gome.work.core.utils.SystemJumpUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by songzhiyang on 2017/2/21.
 */

public class AuthorityUtils {
    public final static int AUTHORIRY_PHONE = 100;
    public final static int AUTHORIRY_CAMERA = 101;
    public final static int AUTHORIRY_LOCATION = 102;
    public final static int AUTHORIRY_STORAGE = 103;
    public final static int AUTHORIRY_MICROPHONE = 104;
    public final static int AUTHORIRY_STORAGE_AND_PHONE = 105;
    public final static int AUTHORIRY_MICROPHONE_AND_CAMERA = 106;

    /**
     * 单一权限申请
     *
     * @param activity                     目前的activity
     * @param tip                          该权限需要给的功能提示 如照相机、麦克风
     * @param requestCode                  权限申请码
     * @param permission                   权限
     * @param permissionsOperationCallback 取消时需要的操作
     */
    public static void checkPermission(final Activity activity, final String tip, final int requestCode, final String permission, final PermissionsOperationCallback permissionsOperationCallback) {
        Authority authority = new Authority.Builder()
                .setPermissions(permission)
                .setCommonOperation(new Authority.CommonOperation() {
                    @Override
                    public void doCommonOperation() {
                        permissionsOperationCallback.onGetAllPermission();
                    }
                }).setCommonDenyTips(new Authority.CommonDenyTips() {
                    @Override
                    public void doCommonDenyTips() {
                        showCommonAlertDialog(activity, tip, requestCode, permission, permissionsOperationCallback);
                    }
                }).setForeverDenyTips(new Authority.ForeverDenyTips() {
                    @Override
                    public void doForeverDenyTips() {
                        showDenyAlertDialog(tip, activity, permissionsOperationCallback);
                    }
                }).setRequestCode(requestCode).build();
        authority.checkPermission(activity, requestCode);
    }

    private static void showCommonAlertDialog(final Activity activity, String tip, final int requestCode, final String permission, PermissionsOperationCallback permissionsOperationCallback) {
        AlertDialog dialog = getAuthorityCommonDialog(activity, tip, requestCode, permission, permissionsOperationCallback);
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.positive_button_color));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(activity.getResources().getColor(R.color.negative_button_color));
    }

    private static AlertDialog getAuthorityCommonDialog(final Activity activity, final String tip, final int requestCode, final String permission, final PermissionsOperationCallback permissionsOperationCallback) {
        AlertDialog dialog = new AlertDialog.Builder(activity).setTitle("权限说明").setCancelable(false)
                .setMessage(getContentBody(tip, 1))
                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        checkPermission(activity, tip, requestCode, permission, permissionsOperationCallback);
                    }
                }).setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (permissionsOperationCallback != null) {
                            permissionsOperationCallback.doCancelOperation();
                        }
                    }
                }).create();
        return dialog;
    }

    private static final String sForeverContentBody = "在设置-应用-Aeromind-权限中开启%s权限，以正常使用%s相关功能";
    private static final String sCommonContentBody = "请授予%s权限，以正常使用%s相关功能";

    private static void showDenyAlertDialog(String tips, Activity activity, PermissionsOperationCallback permissionsOperationCallback) {
        AlertDialog dialog = getAuthorityForeverDialog(tips, activity, permissionsOperationCallback);
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.positive_button_color));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(activity.getResources().getColor(R.color.negative_button_color));
    }

    private static AlertDialog getAuthorityForeverDialog(String tips, final Activity activity, final PermissionsOperationCallback permissionsOperationCallback) {
        AlertDialog dialog = new AlertDialog.Builder(activity).setTitle("权限申请").setCancelable(false)
                .setMessage(getContentBody(tips, 0))
                .setPositiveButton("设置权限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SystemJumpUtils.jump2SettingsActivity(activity);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (permissionsOperationCallback != null) {
                            permissionsOperationCallback.doCancelOperation();
                        }
                    }
                }).create();
        return dialog;
    }

    public static void checkMutiPermissions(final Activity activity, final String tip, final int requestCode, final List<String> permissions, final PermissionsOperationCallback permissionsOperationCallback) {
        Authority authority = new Authority.Builder()
                .setPermissions(permissions)
                .setCommonOperation(new Authority.CommonOperation() {
                    @Override
                    public void doCommonOperation() {
                        if (permissionsOperationCallback != null) {
                            permissionsOperationCallback.onGetAllPermission();
                        }
                    }
                })
                .setCommonDenyTips(new Authority.CommonDenyTips() {
                    @Override
                    public void doCommonDenyTips() {
                        showMutiCommonAlertDialog(activity, tip, requestCode, permissions, permissionsOperationCallback);
                    }
                })
                .setForeverDenyTips(new Authority.ForeverDenyTips() {
                    @Override
                    public void doForeverDenyTips() {
                        showDenyAlertDialog(tip, activity, permissionsOperationCallback);
                    }
                })
                .setRequestCode(requestCode).build();
        authority.checkPermission(activity, requestCode);
    }

    private static void showMutiCommonAlertDialog(final Activity activity, final String tip, final int requestCode, final List<String> permissions, PermissionsOperationCallback permissionsOperationCallback) {
        AlertDialog dialog = getMutiAuthorityCommonDialog(activity, tip, requestCode, permissions, permissionsOperationCallback);
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.positive_button_color));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(activity.getResources().getColor(R.color.negative_button_color));
    }

    private static AlertDialog getMutiAuthorityCommonDialog(final Activity activity, final String tip, final int requestCode, final List<String> permissions, final PermissionsOperationCallback permissionsOperationCallback) {
        AlertDialog dialog = new AlertDialog.Builder(activity).setTitle("权限说明").setCancelable(false)
                .setMessage(getContentBody(tip, 1))
                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        checkMutiPermissions(activity, tip, requestCode, permissions, permissionsOperationCallback);
                    }
                }).setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (permissionsOperationCallback != null) {
                            permissionsOperationCallback.doCancelOperation();
                        }
                    }
                }).create();
        return dialog;
    }

    private static String getContentBody(String tips, int type) {
        return type == 0 ? String.format(sForeverContentBody, tips, tips) : String.format(sCommonContentBody, tips, tips);
    }

    public interface PermissionsOperationCallback {
        void doCancelOperation();

        void onGetAllPermission();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkSystemAlertWindowPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return false;
        }
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        Class clazz = AppOpsManager.class;
        try {
            Method checkOp = clazz.getMethod("checkOp", int.class, int.class, String.class);
            if (checkOp == null) {
                return false;
            }
            int opsCode = (int) checkOp.invoke(appOpsManager, 24, context.getApplicationInfo().uid, context.getPackageName());
            return opsCode == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressLint("NewApi")
    public static boolean canDrawOverlayViews(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return true;
        try {
            return Settings.canDrawOverlays(context);
        } catch (NoSuchMethodError e) {
            return canDrawOverlaysUsingReflection(context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static boolean canDrawOverlaysUsingReflection(Context context) {
        try {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            Class clazz = AppOpsManager.class;
            Method dispatchMethod = clazz.getMethod("checkOp", new Class[]{int.class, int.class, String.class});
            //AppOpsManager.OP_SYSTEM_ALERT_WINDOW = 24
            int mode = (Integer) dispatchMethod.invoke(manager, new Object[]{24, Binder.getCallingUid(), context.getApplicationContext().getPackageName()});

            return AppOpsManager.MODE_ALLOWED == mode;

        } catch (Exception e) {
            return false;
        }
    }

}
