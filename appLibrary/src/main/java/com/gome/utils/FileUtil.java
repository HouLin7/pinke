package com.gome.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public static String PATH = "gome/";
    public static File updateDir = null;
    public static File updateFile = null;

    private FileUtil() {

    }

    public static boolean isSDCardExit() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    public static void createFile(String name) {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            updateDir = new File(Environment.getExternalStorageDirectory()
                    + "/" + PATH);
            updateFile = new File(updateDir + "/" + name + ".apk");

            if (!updateFile.getParentFile().exists()) {
                updateFile.getParentFile().mkdirs();
            }
            if (!updateFile.exists()) {
                try {
                    updateFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static long getSDFreeSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        // return freeBlocks * blockSize; //单位Byte
        // return (freeBlocks * blockSize)/1024; //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }


    public static String getFileExtension(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }


    public static String getMimeTypeFromExtension(String extension) {
        String mimeType = "";
        if (!TextUtils.isEmpty(extension)) {
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    extension);
        }

        return mimeType;
    }


    public static String getMimeTypeFromFile(File file) {
        String mimeType = "";
        String extension = getFileExtension(file.getAbsolutePath());
        if (!TextUtils.isEmpty(extension)) {
            mimeType = getMimeTypeFromExtension(extension);
        }
        return mimeType;
    }

    public final static String getSuffixByFileName(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }
        int index = fileName.indexOf(".");
        if (index < 0) {
            return "";
        }
        return fileName.substring(index, fileName.length());
    }


    public final static String getSuffix(String mimeType) {
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(mimeType);
    }

    public final static String getMimeType(String extension) {
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getMimeTypeFromExtension(extension);
    }

    public static boolean isTextType(String contentType) {
        return (null != contentType) && contentType.startsWith("text/");
    }

    public static boolean isImageType(String contentType) {
        return (null != contentType) && contentType.startsWith("image/");
    }

    public static boolean isAudioType(String contentType) {
        return (null != contentType) && contentType.startsWith("audio/");
    }

    public static boolean isVideoType(String contentType) {
        return (null != contentType) && contentType.startsWith("video/");
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 90 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static void deleteFile(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            int size = files.length;
            for (int i = 0; i < size; i++) {
                if (files[i].isDirectory()) {
                    deleteFile(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
    }


    public static String getPath(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


}
