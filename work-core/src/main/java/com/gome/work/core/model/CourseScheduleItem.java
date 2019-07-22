package com.gome.work.core.model;

import com.google.gson.annotations.Expose;

/**
 *
 */
public class CourseScheduleItem {

    @Expose
    public long startTime;
    @Expose
    public long endTime;


    public ActivityItem[] weeks;


    public CourseScheduleItem() {
        weeks = new ActivityItem[7];
        for (int i = 0; i < weeks.length; i++) {
            weeks[i] = new ActivityItem();
            weeks[i].state = 0;

        }
    }

    public class ActivityItem {

        public int state;

        public String content;

    }


}
