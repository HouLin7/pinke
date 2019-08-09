
package com.gome.work.core.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * 地址
 */
public class AddressItem implements Serializable {

    private static final long serialVersionUID = -6507871928142789829L;

    @Expose
    public RegionItem province;

    @Expose
    public RegionItem city;

    @Expose
    public RegionItem county;

    @Expose
    public String street;

    @Expose
    public String address;

    /**
     * 经度
     */
    @Expose
    public double longitude;

    /**
     *纬度
     */
    public double latitude;

}
