package com.gome.work.core.model.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.io.Serializable;

/**
 * Created by chaergongzi on 2018/8/14.
 */

@Entity(indexes = {@Index(value = "appId, dataUseId ", unique = true)})

/**
 * 我收藏的app
 */
public class MyFavoriteAppInfo implements Serializable {

    private static final long serialVersionUID = -6474695228663640437L;
    @Id(autoincrement = true)
    private Long id;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 使用者id
     */
    private String dataUseId;

    @Generated(hash = 1929687238)
    public MyFavoriteAppInfo(Long id, String appId, String dataUseId) {
        this.id = id;
        this.appId = appId;
        this.dataUseId = dataUseId;
    }

    @Generated(hash = 2035376705)
    public MyFavoriteAppInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDataUseId() {
        return this.dataUseId;
    }

    public void setDataUseId(String dataUseId) {
        this.dataUseId = dataUseId;
    }



}
