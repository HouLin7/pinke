package com.gome.work.common.utils;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenhang01 on 2016/11/1.
 */

public class StringUtils {


    public static ArrayList<String> getStringList(List<Long> lists) {
        if (lists.isEmpty())
            return null;
        ArrayList<String> strings = new ArrayList<>();
        for (long list : lists) {
            strings.add(String.valueOf(list));
        }
        return strings;
    }

    public static ArrayList<Long> getLongList(List<String> lists) {
        if (lists == null || lists.isEmpty())
            return null;
        ArrayList<Long> longs = new ArrayList<>();
        for (String string : lists) {
            longs.add(Long.valueOf(string));
        }
        return longs;
    }

    /**
     * 得到固定长度的随机字符串
     */
    public static String getRandomChar(int length) {
        char[] chr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer.append(chr[random.nextInt(36)]);
        }
        return buffer.toString();
    }

    /**
     * Convert null to empty string ""
     *
     * @param str
     * @return
     */
    public final static String toSafeString(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str;
    }

    public static String getStrTime(String cc_time) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (!TextUtils.isEmpty(cc_time)) {
            return sDateFormat.format(Long.valueOf(cc_time));
        } else {
            return null;
        }

    }

    public static String readContent(InputStream is) {
        return readContent(is, "UTF-8");
    }

    public static String readContent(InputStream is, String charsetName) {
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, charsetName));
            String json = null;
            while ((json = br.readLine()) != null) {
                buffer.append(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return buffer.toString();
    }

    /**
     * 以M为单位，保留两位小数
     *
     * @param totalLength
     * @return
     */
    public static String getReadFileSize(long totalLength) {
        final float unitMB = 1024 * 1024;
        float valueMb = Math.round(totalLength / unitMB * 100) / 100f;
        return String.valueOf(valueMb) + "M";
    }

    /**
     * 四舍五入保留一位小数，用于评分计算
     *
     * @return
     */
    public static String getStaForSize(int score) {
        float totalLength = score / 20;
        DecimalFormat df = new DecimalFormat("0.0");
        String num3 = df.format(totalLength);
        return num3;
    }

    /**
     * 获取指定url中的某个参数
     *
     * @param url
     * @param name
     * @return
     */
    public static String getParamByUrl(String url, String name) {
        url += "&";
        String pattern = "(\\?|&){1}#{0,1}" + name + "=[a-zA-Z0-9]*(&{1})";

        Pattern r = Pattern.compile(pattern);

        Matcher m = r.matcher(url);
        if (m.find()) {
            return m.group(0).split("=")[1].replace("&", "");
        } else {
            return null;
        }
    }

    public static boolean checkEmail(String email) {
        Pattern p = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean checkPhone(String phoneNum){
        Pattern p = Pattern.compile("^(1[3-9])\\d{9}$");
        Matcher m = p.matcher(phoneNum);
        return m.matches();

    }
}
