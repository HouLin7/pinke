package com.gome.work.common.utils;

import android.text.TextUtils;

import com.github.promeg.pinyinhelper.Pinyin;

import java.text.Collator;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class SearchFilter {
    private static Collator collator = Collator.getInstance(Locale.CHINA);
    /**
     * 直接匹配类型，用于排序
     */
    public final static int SORT_TYPE_ZHIJIE = 0;
    /**
     * 直接匹配类型，用于排序
     */
    public final static int SORT_TYPE_PINYIN = 1;
    /**
     * 直接匹配类型，用于排序
     */
    public final static int SORT_TYPE_JIANPIN = 2;
    /**
     * 直接匹配类型，用于排序
     */
    public final static int SORT_TYPE_PHONE = 3;

    /**
     * 搜索高亮位置
     *
     * @param name 被检索的内容
     * @return 检索不到显示空 ，检索到会返回一个长度为3的数组，第一位置表示开始坐标，第二个位置表示结束坐标，第三个位置表示匹配类型。
     */
    public static int[] getPosition(String name, String key) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(key))
            return null;
        name = name.toLowerCase();
       String[] array= getSearch(name);
        if (array!=null&&array.length==4){
            return getPosition(array[0], array[1], array[2], array[3], key);
        }else {
            return null;
        }

    }


    /**
     * 搜索高亮位置
     *
     * @param name   被检索的内容
     * @param p_name 内容的全拼格式
     * @param j_name 内容的简拼格式
     * @param i_name 内容的全拼对应的脚标格式
     * @param key    要高亮的关键词
     * @return 检索不到显示空 ，检索到会返回一个长度为3的数组，第一位置表示开始坐标，第二个位置表示结束坐标，第三个位置表示匹配类型。
     */

    public static int[] getPosition(String name, String p_name, String j_name, String i_name, String key) {
        if (TextUtils.isEmpty(j_name) || TextUtils.isEmpty(p_name)||TextUtils.isEmpty(i_name) || TextUtils.isEmpty(key))
            return null;
        name=name.toLowerCase();
        j_name = j_name.toLowerCase();
        p_name = p_name.toLowerCase();
        key = key.toLowerCase();
        int[] position = new int[3];

        //符合直接匹配规则
        if (name.contains(key)) {
            position[0] = name.indexOf(key);
            position[1] = position[0] + key.length();
            position[2] = SORT_TYPE_ZHIJIE;
            return position;
        }

        //符合全拼音匹配规则
        if (p_name.contains(key)) {

            //第一个拼音匹配的直接返回
            int p_start_index = p_name.indexOf(key);

            List<String> indexArray = Arrays.asList(i_name.split(","));
            if (indexArray!=null&&indexArray.size()>=key.length()&&indexArray.size()>=p_name.length()){
                if (p_start_index == 0) {

                    position[0] = 0;
                    int i_end_value = Integer.parseInt(indexArray.get(key.length()-1));
                    position[1] = i_end_value + 1;
                    position[2] = SORT_TYPE_PINYIN;
                    return position;

                } else if (p_name.length() > 1) {

                    int i_start_value = Integer.parseInt(indexArray.get(p_start_index));
                    int i_pro_value = Integer.parseInt(indexArray.get(p_start_index-1));
                    if (i_start_value != i_pro_value) {
                        position[0] = i_start_value;
                        int i_end_value = Integer.parseInt(indexArray.get(p_start_index + key.length() - 1));
                        position[1] = i_end_value + 1;
                        position[2] = SORT_TYPE_PINYIN;
                        return position;
                    }

                }
            }

        }

        //符合简拼匹配规则
        if (j_name.contains(key)) {

            int j_start = j_name.indexOf(key);
            int j_end = j_start + key.length();
            position[0] = j_start;
            position[1] = j_end;
            position[2] = SORT_TYPE_JIANPIN;
            return position;

        }
        return null;
    }


    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**获取搜索所需要的参数 0位置是搜索内容  1位置是全拼  2位置是简拼 3位置是全品脚标*/
    public static String[] getSearch(String target){
        if (TextUtils.isEmpty(target)){
            return null;
        }
        String[] ss= new String[4];
        StringBuilder pySb = new StringBuilder();
        StringBuilder initialSb = new StringBuilder();
        StringBuilder indexSb = new StringBuilder();
        for (int i = 0; i < target.length(); i++) {
            char c = target.charAt(i);
            String s = String.valueOf(c);
            if(isChinese(c)){
                s = Pinyin.toPinyin(c).toLowerCase();
                for (int j = 0; j < s.length(); j++) {
                    indexSb.append(i+",");
                }
            }else {
                indexSb.append(i+",");
            }
            if(s.length()>0){
                initialSb.append(s.substring(0,1).toLowerCase());
            }

            pySb.append(s.toLowerCase());

        }
        if (indexSb.length()>0){
            indexSb.subSequence(0,indexSb.length()-1);
        }
        ss[0]=target;
        ss[1]=pySb.toString();
        ss[2]=initialSb.toString();
        ss[3]=indexSb.toString();
        return ss;
    }

    /**
     * 判断字符串是否只包含数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

}