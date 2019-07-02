package com.gome.work.core.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chaergongzi on 2018/7/26.
 */

public class ContactsNodeWrapper implements Serializable {

    @Expose
    public List<ContactsNodeItem> nodeInfos;

    @Expose
    public String extend;
}
