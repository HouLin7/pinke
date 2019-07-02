package com.gome.work.core.model.appmarket;

import com.gome.work.core.model.UserInfo;

import java.util.List;

/**
 * Create by liupeiquan on 2018/8/21
 */
public class AppCommentBean  {
    public List<AppCommentItem> items;
    public String total_number;

    public static class AppCommentItem{
        public String content;
        public int id;
        public String date;
        public int score;
        public UserInfo user;
    }
}
