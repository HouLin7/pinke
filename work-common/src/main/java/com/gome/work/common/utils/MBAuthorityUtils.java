
package com.gome.work.common.utils;

import android.Manifest;
import android.app.Activity;


import com.gome.work.common.activity.BaseGomeWorkActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liuletao on 2017/2/23.
 */

public class MBAuthorityUtils {

    /**
     * @param activity
     */
    public static void autorityMicrophone(final Activity activity,  final AuthorityUtils.PermissionsOperationCallback permissionsOperationCallback){
        AuthorityUtils.checkPermission(activity, "麦克风",AuthorityUtils.AUTHORIRY_MICROPHONE, Manifest.permission.RECORD_AUDIO, permissionsOperationCallback);
    }


    /**
     * @param activity
     */
    public static void autorityReadAndWrite(final Activity activity,  final AuthorityUtils.PermissionsOperationCallback permissionsOperationCallback){
        AuthorityUtils.checkPermission(activity, "读写文件",AuthorityUtils.AUTHORIRY_MICROPHONE, Manifest.permission.RECORD_AUDIO, permissionsOperationCallback);
    }

    public static void autorityCameraAndMicrophone(final Activity activity, final AuthorityUtils.PermissionsOperationCallback permissionsOperationCallback){
        AuthorityUtils.checkMutiPermissions(activity, "摄像头、麦克风",AuthorityUtils.AUTHORIRY_MICROPHONE_AND_CAMERA, Arrays.asList(Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA), permissionsOperationCallback);
    }

    public static void authorityCamera(final Activity activity, final AuthorityUtils.PermissionsOperationCallback permissionsOperationCallback) {
        AuthorityUtils.checkPermission(activity,"照相机",AuthorityUtils.AUTHORIRY_CAMERA,Manifest.permission.CAMERA, permissionsOperationCallback);
    }

    public static void authorityLocation(final Activity activity, final AuthorityUtils.PermissionsOperationCallback permissionsOperationCallback) {
        AuthorityUtils.checkPermission(activity,"定位",AuthorityUtils.AUTHORIRY_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION, permissionsOperationCallback);
    }

    public static void authorityAppPrePermissions(final Activity activity) {
        String tip = "读写文件、手机状态";

        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.READ_PHONE_STATE);

        AuthorityUtils.PermissionsOperationCallback permissionsOperationCallback = new AuthorityUtils.PermissionsOperationCallback() {
            @Override
            public void doCancelOperation() {
                activity.finish();
            }

            @Override
            public void onGetAllPermission() {
                if (activity instanceof BaseGomeWorkActivity) {
//                    BaseGomeWorkActivity baseGomeWorkActivity = (BaseGomeWorkActivity) activity;
//                    baseGomeWorkActivity.permissionGranted();
                }
            }
        };
        AuthorityUtils.checkMutiPermissions(activity,tip,AuthorityUtils.AUTHORIRY_STORAGE_AND_PHONE,permissions, permissionsOperationCallback);
    }
}
