package com.gome.work.core.utils;

import com.gome.utils.UrlUtil;

/**
 * Create by liupeiquan on 2018/12/22
 */
public class MeetingUtils {

    public static String getCreateMeetingUrl(String url) {
        return UrlUtil.getHostUrl(url) + "/appMeeting/newmeeting";
//        return UrlUtil.getHostUrl(url) + "/appMeeting/wykh";
    }

    public static String getMeetingDetailUrl(String url, String meetingId) {
        return UrlUtil.getHostUrl(url) + "/appMeeting/Hyxq?meetingId=" + meetingId;
    }

    public static String getMeetingRoomIdlUrl(String url, String roomId) {
        return UrlUtil.getHostUrl(url) + "/appMeeting/electime?meetingRoomId=" + roomId;
    }



}
