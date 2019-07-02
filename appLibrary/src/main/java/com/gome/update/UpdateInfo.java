package com.gome.update;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class UpdateInfo implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = -378609788183770820L;

    @Expose
    public int versionId;

    @Expose
    public String versionName;

    @Expose
    public String content;

    @Expose
    public String isForce;

    @Expose
    public String downloadUrl;

    @Expose
    public String fileSize;

    @Expose
    public long updateTime;


}
