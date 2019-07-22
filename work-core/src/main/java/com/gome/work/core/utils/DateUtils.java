package com.gome.work.core.utils;

import android.content.Context;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by tanxingchun on 2017/1/12.
 */

public class DateUtils {
    public static String getDate() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return sf.format(date);
    }

    public static String getDate(String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format, Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return sf.format(date);
    }

    /**
     * @param time   时间戳字符串
     * @param format 输出格式  如： yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String transformTime(String time, String format) {

        try {
            long timeLong = Long.parseLong(time);
            return transformTime(timeLong, format);
        } catch (NumberFormatException e) {
            return time;
        }
    }


    public static boolean isSameDay(long time1, long time2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time1);
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(time2);
        return isSameDay(c1, c2);
    }

    public static boolean isSameDay(Calendar c1, Calendar c2) {
        int nowDay = c1.get(Calendar.DAY_OF_MONTH);
        int nowMonth = c1.get(Calendar.MONTH);
        int nowYear = c1.get(Calendar.YEAR);

        int oldDay = c2.get(Calendar.DAY_OF_MONTH);
        int oldMonth = c2.get(Calendar.MONTH);
        int oldYear = c2.get(Calendar.YEAR);

        return nowDay == oldDay && nowMonth == oldMonth && nowYear == oldYear;
    }

    public static String getDurationTime(Context context, long timeMills) {
        if (timeMills < 0) {
            timeMills = 0;
        }
        int day = (int) (timeMills / (24 * 60 * 60 * 1000));

        timeMills -= day * 24 * 60 * 60 * 1000l;
        int hour = (int) (timeMills / (60 * 60 * 1000));

        timeMills -= hour * 60 * 60 * 1000;

        int min = (int) (timeMills / (60 * 1000));

        StringBuffer buffer = new StringBuffer();
        if (day > 0) {
            buffer.append(day);
            buffer.append("day");
        }

        buffer.append(hour);
        buffer.append("hour");

        buffer.append(min);
        buffer.append("min");
        return buffer.toString();
    }


    /**
     * @param duration 时间段格式
     * @return
     */
    public static String transformDuration(long duration) {
        long min = TimeUnit.MILLISECONDS.toMinutes(duration);

        long leftSec = min * 60;
        long sec = TimeUnit.MILLISECONDS.toSeconds(duration);
        sec -= leftSec;
        StringBuilder sb = new StringBuilder();
        sb.append(min).append("分").append(sec).append("秒");
        return sb.toString();
    }

    public static boolean isDay(Context context) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        return (hour == 6 && min > 0 || hour > 6) && hour < 18;
    }

    /**
     * 根据时间戳获取标准时间
     *
     * @param time        时间戳
     * @param formatStyle 输出的格式 如：yyyy-MM-dd HH:mm
     * @return
     */
    public static String transformTime(long time, String formatStyle) {
        SimpleDateFormat ft = new SimpleDateFormat(formatStyle);
        return ft.format(time);
    }


    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */

    public static Date parse(String strDate, String pattern) {
        if (TextUtils.isEmpty(strDate)) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param milliseconds 日期
     * @param pattern      日期格式
     * @return
     */

    public static String format(long milliseconds, String pattern) {
        return format(new Date(milliseconds), pattern);
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */

    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }


    /**
     * 获取俩个日期之间相差多少天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getDifferDay(Date date1, Date date2) {
        int days = (int) (Math.abs(date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    /**
     * 获取俩个日期之间相差多少天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getDifferMinute(long date1, long date2) {

        Long s = Math.abs(date2 - date1) / (1000 * 60);
        return s;
    }

    public static String getDatePoor(long endDate, long nowDate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = Math.abs(endDate - nowDate);
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }


    public static Calendar getCalendar(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static long getPreDay(long timeInMillis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeInMillis);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTimeInMillis();
    }

    public static long getNextDay(long timeInMillis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeInMillis);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTimeInMillis();
    }

}
