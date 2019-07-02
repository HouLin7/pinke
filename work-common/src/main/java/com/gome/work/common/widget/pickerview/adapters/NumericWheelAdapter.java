/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.gome.work.common.widget.pickerview.adapters;

import android.content.Context;
import android.text.TextUtils;

import com.gome.work.common.widget.pickerview.TimeWheel;
import com.gome.work.common.widget.pickerview.config.DefaultConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Numeric Wheel adapter.
 */
public class NumericWheelAdapter extends AbstractWheelTextAdapter {

    /**
     * The default min value
     */
    public static final int DEFAULT_MAX_VALUE = 9;

    /**
     * The default max value
     */
    private static final int DEFAULT_MIN_VALUE = 0;

    // Values
    private int minValue;
    private int maxValue;

    // format
    private String format;
    //unit
    private String unit;

    private static int year;
    private static int month;
    private TimeWheel timeWheel;

    /**
     * Constructor
     *
     * @param context the current context
     */
    public NumericWheelAdapter(Context context) {
        this(context, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
    }

    /**
     * Constructor
     *
     * @param context  the current context
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     */
    public NumericWheelAdapter(Context context, int minValue, int maxValue) {
        this(context, minValue, maxValue, null);
    }

    /**
     * Constructor
     *
     * @param context  the current context
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     * @param format   the format string
     */
    public NumericWheelAdapter(Context context, int minValue, int maxValue, String format) {
        this(null,context, minValue, maxValue, format, null);
    }

    /**
     * Constructor
     *
     * @param context  the current context
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     * @param format   the format string
     * @param unit     the wheel unit value
     */
    public NumericWheelAdapter(TimeWheel timeWheel,Context context, int minValue, int maxValue, String format, String unit) {
        super(context);
        this.timeWheel = timeWheel;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.format = format;
        this.unit = unit;
    }

    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < getItemsCount()) {
            int value = minValue + index;

            if (unit.equals(DefaultConfig.YEAR)){
                year = timeWheel.getCurrentYear();
            }
            if (unit.equals(DefaultConfig.MONTH)){
                month = timeWheel.getCurrentMonth();
            }
            String text = !TextUtils.isEmpty(format) ? String.format(format, value) : Integer.toString(value);
            text = TextUtils.isEmpty(unit) ? text : text + unit;
            if (unit.equals(DefaultConfig.DAY)){
                String date = year + "-" + month + "-" + value;
                text = text + getWeek(getDayOfWeek(date));
            }

            return text;
        }
        return null;
    }

    private String getWeek(int week){
        String[] arr = {"周日","周一","周二","周三","周四","周五","周六"};
        return arr[week-1];
    }
    @Override
    public int getItemsCount() {
        return maxValue - minValue + 1;
    }

    private int getDayOfWeek(String date){
        Calendar calendar = Calendar.getInstance();

        if (TextUtils.isEmpty(date)){
            calendar.setTime(new Date(System.currentTimeMillis()));
        }else {
            calendar.setTime(new Date(getDate(date).getTime()));
        }

        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    private Date getDate(String dd){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = simpleDateFormat.parse(dd);
        } catch (ParseException e) {
            date = null;
            e.printStackTrace();
        }
        return date;
    }

    public void setYear(int year){
        NumericWheelAdapter.year = year;
        notifyDataInvalidatedEvent();
    }

    public void setMonth(int month){
        NumericWheelAdapter.month = month;
        notifyDataInvalidatedEvent();
    }
}
