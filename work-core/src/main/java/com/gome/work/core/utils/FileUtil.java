package com.gome.work.core.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * 文件工具类
 *
 * @author xingchun
 */
public class FileUtil {
    private static final String TAG = FileUtil.class.getSimpleName();

    public static boolean saveBitmap(Bitmap mBitmap, String path) {
        File f = new File(path);
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return false;
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean isSdCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 保存Bitmap
     *
     * @param bitmap
     * @param tempFile
     */
    public static void saveBitmap(Bitmap bitmap, File tempFile) {
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
        return degree;
    }

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }

        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();

        return data;
    }

    /**
     * @param filePath
     * @return byte[]
     * @throws FileNotFoundException
     */
    public static byte[] readFileToBytes(String filePath) throws FileNotFoundException {
        byte[] fileBytes = null;
        FileInputStream in = null;
        try {
            File file = new File(filePath);
            int length = (int) file.length();
            fileBytes = new byte[length];
            in = new FileInputStream(filePath);
            in.read(fileBytes, 0, length);
            in.close();
            in = null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                    in = null;
                } catch (Exception ex) {
                }
            }
        }
        return fileBytes;
    }

    public static Bitmap getBitmapFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null) {
            if (opts != null) {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            } else {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        }

        return null;
    }

    /**
     * 保存一个字符串到文件, 如果文件存在，则清除之前的数据，保存新的数据
     *
     * @param str
     * @param path
     */
    public static boolean saveString(String str, String path) {
        File f = new File(path);
        if (!f.getParentFile().exists())
            f.mkdirs();
        if (f.exists())
            f.delete();
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return false;
        }

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
            fOut.write(str.getBytes());
            fOut.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    /**
     * 获取String
     *
     * @param path
     * @return
     */
    public static String getString(String path) {
        if (path == null)
            return null;
        File f = new File(path);
        if (!f.exists())
            return null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            String temp = null;
            StringBuffer sb = new StringBuffer();
            temp = br.readLine();
            while (temp != null) {
                sb.append(temp);
                temp = br.readLine();
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取String
     *
     * @param f
     * @return
     */
    public static String getString(File f) {
        if(f==null){
            return "";
        }
        if (!f.exists())
            return null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            String temp = null;
            StringBuffer sb = new StringBuffer();
            temp = br.readLine();
            while (temp != null) {
                sb.append(temp);
                temp = br.readLine();
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 下载文件 语音、图片
     *
     * @param urlPath
     * @return
     */
    public static byte[] getFileByte(String urlPath) {
        InputStream in = null;
        byte[] result = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection httpURLconnection = (HttpURLConnection) url.openConnection();
            httpURLconnection.setDoInput(true);
            httpURLconnection.connect();
            if (httpURLconnection.getResponseCode() == 200) {
                in = httpURLconnection.getInputStream();
                result = readAll(in);
                in.close();
            } else {
                Log.e(TAG, "下载文件失败，状态码是：" + httpURLconnection.getResponseCode());
            }
        } catch (Exception e) {
            Log.e(TAG, "下载文件失败，原因是：" + e.toString());
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static byte[] readAll(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        byte[] buf = new byte[1024];
        int c = is.read(buf);
        while (-1 != c) {
            baos.write(buf, 0, c);
            c = is.read(buf);
        }
        baos.flush();
        baos.close();
        return baos.toByteArray();
    }


    public static boolean saveInfo2File(File file, String content) {
        FileWriter fw = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
           if (fw != null) {
               try {
                   fw.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
        }
        return true;
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void deleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                file.delete();
            }
        }
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) return null;
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = context.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        if (cursor == null) return null;
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    private static final int ERROR = -1;

    /**
     * SDCARD是否存
     */
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取手机内部剩余存储空间
     *
     * @return
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 获取手机内部总的存储空间
     *
     * @return
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 获取SDCARD剩余存储空间
     *
     * @return
     */
    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    /**
     * 获取SDCARD总的存储空间
     *
     * @return
     */
    public static long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    /**
     * 获取系统总内存
     *
     * @param context 可传入应用程序上下文。
     * @return 总内存大单位为B。
     */
    public static long getTotalMemorySize(Context context) {
        String dir = "/proc/meminfo";
        FileReader fr = null;
        try {
            fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            return Integer.parseInt(subMemoryLine.replaceAll("\\D+", "")) * 1024l;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    /**
     * 获取当前可用内存，返回数据以字节为单位。
     *
     * @param context 可传入应用程序上下文。
     * @return 当前可用内存单位为B。
     */
    public static long getAvailableMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    private static DecimalFormat fileIntegerFormat = new DecimalFormat("#0");
    private static DecimalFormat fileDecimalFormat = new DecimalFormat("#0.#");

    public static boolean copyFile(String sourceFilePath, String duplicateFilePath) {
        int length = 2097152;
        File sourceFile = new File(sourceFilePath);
        if (!sourceFile.exists()) {
            return false;
        }
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            File duplicateFile = new File(duplicateFilePath);
            if (!duplicateFile.exists()) {
                duplicateFile.getParentFile().mkdirs();
                duplicateFile.createNewFile();
            }
            in = new FileInputStream(sourceFile);
            out = new FileOutputStream(duplicateFilePath);
            byte[] buffer = new byte[length];
            while (true) {
                int ins = in.read(buffer);
                if (ins == -1) {
                    in.close();
                    out.flush();
                    out.close();
                    return true;
                } else {
                    out.write(buffer, 0, ins);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static boolean isJIG(String srcFileName) {
        File file = new File(srcFileName);
        if (!file.exists()|| !file.isFile())
            return false;
        return isJIG(file);
    }

    public static boolean isJIG(File srcFileName) {
        FileInputStream imgFile = null;
        byte[] b = new byte[10];
        int l = -1;
        try {
            imgFile = new FileInputStream(srcFileName);
            l = imgFile.read(b);
            imgFile.close();
        } catch (Exception e) {
            return false;
        } finally {
            if (imgFile != null) {
                try {
                    imgFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (l == 10) {
            byte b0 = b[0];
            byte b1 = b[1];
            byte b2 = b[2];
            if (b0 == (byte) 'G' && b1 == (byte) 'I' && b2 == (byte) 'F') {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

}
