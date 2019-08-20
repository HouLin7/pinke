package com.gome.work.common.utils.apkverify;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.gome.utils.PictureUtils;
import com.gome.work.common.utils.PropertyUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by chenhang01 on 2016/11/15.
 */

public class CheckUtils {

    /**
     * 获取当前apk的crc32值
     * @return
     */
    public static Long getCrc32(Context context){
        String apkPath = ApkPathUtils.getApkPath(context);
        ZipFile zipfile = null;
        ZipEntry dexentry = null;
        try {
            zipfile = new ZipFile(apkPath);
            dexentry = zipfile.getEntry("classes.dex");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dexentry.getCrc();
    }

    /**
     * 获取图片的hash值
     * @return
     */
    public static String getImgHash(Context context,int imgId){
        MessageDigest msgDigest = null;
        String apkPath = ApkPathUtils.getApkPath(context);
        ByteArrayInputStream fis = null;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),imgId);

        byte[] bitmapBytes = PictureUtils.Bitmap2Bytes(bitmap);
        try {
            msgDigest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = new byte[1024];
            int byteCount;
            fis= new ByteArrayInputStream(bitmapBytes);
            while ((byteCount = fis.read(bytes)) > 0)
            {
                msgDigest.update(bytes, 0, byteCount);
            }
            BigInteger bi = new BigInteger(1, msgDigest.digest());
            return bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 获取当前apk包的hash值
     * @return
     */
    public String getHash(Context context){

        MessageDigest msgDigest = null;

        String apkPath = ApkPathUtils.getApkPath(context);
        FileInputStream fis = null;
        try {
            msgDigest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = new byte[1024];
            int byteCount;
            fis= new FileInputStream(new File(apkPath));
            while ((byteCount = fis.read(bytes)) > 0)
            {
                msgDigest.update(bytes, 0, byteCount);
            }
            BigInteger bi = new BigInteger(1, msgDigest.digest());
            return bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static boolean checkAppSignature(Context context, String targetSign) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;
            Signature sign = signatures[0];
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(sign.toByteArray());
            String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            currentSignature = currentSignature.replaceAll("\\n", "");
            if (targetSign.equals(currentSignature)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isEmulator(){
        return PropertyUtils.get("ro.kernel.qemu", "").equals("1");
    }

}
