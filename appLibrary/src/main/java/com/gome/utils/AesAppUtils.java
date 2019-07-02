package com.gome.utils;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesAppUtils {

    /**
     * 字节转换16进制字符串
     * @param buf
     * @return
     */
    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)
                strbuf.append("0");
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }


    /**
     * 字符串转换byte数组
     * @param src
     * @return
     */
    public static byte[] asBin(String src) {
        if (src.length() < 1)
            return null;
        byte[] encrypted = new byte[src.length() / 2];
        for (int i = 0; i < src.length() / 2; i++) {
            int high = Integer.parseInt(src.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(src.substring(i * 2 + 1, i * 2 + 2), 16);
            encrypted[i] = (byte) (high * 16 + low);
        }
        return encrypted;
    }


    /*
     * 解密
     */
    public static String decryptAES(String encData, String key) {
        byte[] rawKey = asBin(key);
        byte[] tmp = asBin(encData);
        SecretKeySpec sKey = new SecretKeySpec(rawKey, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, sKey, iv);
            byte[] decrypted = cipher.doFinal(tmp);
            return new String(decrypted,"UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    /*
     * 加密
     */
    public static String encryptAES(String data, String key) {
        byte[] rawKey = asBin(key);
        SecretKeySpec sKey = new SecretKeySpec(rawKey, "AES");
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, sKey, iv);
            byte[] encrypted = cipher.doFinal(data.getBytes("UTF-8"));
            return asHex(encrypted);
        } catch (Exception e) {
            return "";
        }
    }


    public static String getSHA1Digest(String data) {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            bytes = md.digest(data.getBytes("UTF-8"));
            return asHex(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
