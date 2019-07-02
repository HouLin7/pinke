package com.gome.work.core.model;

import java.io.Serializable;

/**
 * Create by liupeiquan on 2019/1/11
 */
public class FeedBackBean implements Serializable {
    private static final long serialVersionUID = 3480313174945189265L;
    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
