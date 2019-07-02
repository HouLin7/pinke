package com.gome.utils;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileCacheUtils {

    public static String cache(Context context, InputStream is, String fileName) {
        return cache(context, readData(is), fileName);
    }

    private static byte[] readData(InputStream is) {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int index = -1;
        try {
            while ((index = is.read(buffer)) > 0) {
                bos.write(buffer, 0, index);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bos.toByteArray();
    }

    public static void cache(Context context, InputStream is, File file) {
        cache(context, readData(is), file);
    }

    public static String cache(Context context, byte[] data, String fileName) {
        try {
            File file = new File(context.getExternalCacheDir(), fileName);
            return cache(context, data, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String cache(Context context, byte[] data, File file) {
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            fos.close();
            return file.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static void delete(Context context, String fileName) {
        try {
//			fileName = getMD5(fileName);
            File file = new File(context.getExternalCacheDir(), fileName);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void cache(Context context, String text, String fileName) {
        cache(context, text.getBytes(), fileName);
    }

    public static boolean isExist(Context context, String fileName) {
        try {
//			fileName = getMD5(fileName);
            File file = new File(context.getExternalCacheDir(), fileName);
            return file.exists();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

    public static byte[] findRawData(Context context, String fileName) {
        byte[] content = null;
        try {
//			fileName = getMD5(fileName);
            File file = new File(context.getExternalCacheDir(), fileName);
            if (file.exists()) {
                FileInputStream fis;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];

                fis = new FileInputStream(file);
                int index = -1;
                while ((index = fis.read(buffer)) > -1) {
                    bos.write(buffer, 0, index);
                }
                content = bos.toByteArray();
                bos.close();
                fis.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return content;
    }

    public static String find(Context context, String fileName) {
        byte[] data = findRawData(context, fileName);
        if (data != null) {
            return new String(data);
        }
        return "";
    }

//	public static String getMD5(String val) throws NoSuchAlgorithmException {
//		MessageDigest md5 = MessageDigest.getInstance("MD5");
//		md5.update(val.getBytes());
//		byte[] m = md5.digest();
//		return byteArrayToHex(m);
//	}

    public static String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }

        return new String(resultCharArray);

    }
}
