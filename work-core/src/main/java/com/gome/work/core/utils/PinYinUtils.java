package com.gome.work.core.utils;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * Create by liupeiquan on 2018/9/6
 */
public class PinYinUtils {
    /**
     * 将hanzi转成拼音
     *
     * @param hanzi 汉字或字母
     * @return 拼音
     */
    public static String getPinyin(String hanzi) {
        StringBuilder sb = new StringBuilder();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //由于不能直接对多个汉子转换，只能对单个汉子转换
        char[] arr = hanzi.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (Character.isWhitespace(arr[i])) {
                continue;
            }
            try {
                String[] pinyinArr = PinyinHelper.toHanyuPinyinStringArray(arr[i], format);
                if (pinyinArr != null) {
                    sb.append(pinyinArr[0]);
                } else {
                    sb.append(arr[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //不是正确的汉字
                sb.append(arr[i]);
            }

        }
        return sb.toString();
    }

    /**
     * 返回首字母，大写
     * @param str
     * @return
     */
    public static String getFirstLetter(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        // 得到一个字符串的拼音的大写
        String pinyinStr = getPinyin(str).toUpperCase();
        // 取拼音字符串的第一个字母
        char firstCahr = pinyinStr.charAt(0);
        // 不是A-Z字母
        if (firstCahr > 90 || firstCahr < 65) {
            return "#";
        }else{ // 代表是A-Z
            return String.valueOf(firstCahr);
        }

    }

}
