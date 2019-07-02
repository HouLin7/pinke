package com.gome.work.core.model.im;

import java.io.Serializable;

/**
 * Create by liupeiquan on 2019/1/7
 */
public class BillExtraData implements Serializable {
    private static final long serialVersionUID = -8078885580474692184L;
    public String mainTitle;//主标题
    public String subTitle;//副标题
    public String billTitle;//单据标题
    public String billName;//单据名称
    public String billCode;//单据编号
    public String billTime;//单据创建时间
    public String billLinkUrl;//链接
}
