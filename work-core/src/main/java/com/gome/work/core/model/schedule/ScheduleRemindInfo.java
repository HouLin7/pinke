package com.gome.work.core.model.schedule;

import java.io.Serializable;

/**
 * Create by liupeiquan on 2018/12/10
 * 日程提醒
 */
public class ScheduleRemindInfo implements Serializable {

    private static final long serialVersionUID = 6919226100653976775L;

    private int key;
    private String value;
    private boolean isSelect;


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
