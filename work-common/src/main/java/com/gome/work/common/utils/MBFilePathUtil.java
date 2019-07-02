package com.gome.work.common.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;


public class MBFilePathUtil {

    private static final String rootDirectory = "Aeromind";

    public static String getRootDirectory(Context context) {
        String status = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(status)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+ rootDirectory;
        } else {
            return context.getDir(rootDirectory, Context.MODE_PRIVATE).getAbsolutePath();
        }
    }

    public static String getSubdirectory(Context context, String directory) {
        return getRootDirectory(context) + File.separator + directory;
    }

    public static String getInnerRootDirectory(Context context) {
        return context.getDir(rootDirectory, Context.MODE_PRIVATE).getAbsolutePath();
    }

    public static String getInnerSubdirectory(Context context, String directory) {
        return context.getDir(rootDirectory, Context.MODE_PRIVATE).getAbsolutePath() + File.separator + directory;
    }

    public static String getAppPicturePath(Context context){
        return getSubdirectory(context,"saved_picture");
    }

    public static String getCachePicturePath(Context context){
        return getSubdirectory(context,".cached_picture");
    }


}
