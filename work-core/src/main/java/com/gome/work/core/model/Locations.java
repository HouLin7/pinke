package com.gome.work.core.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenhang01 on 2016/5/25.
 */
public class Locations implements Parcelable{

    private String imgPath="";
    private double latitude;
    private double longtitude;
    private String urlcontent="";
    private boolean isEmpty = false; // 数据是否为空

    public Locations() {

    }

    protected Locations(Parcel in) {
        imgPath = in.readString();
        latitude = in.readDouble();
        longtitude = in.readDouble();
        urlcontent = in.readString();
        isEmpty = in.readByte() != 0;
    }

    public static final Creator<Locations> CREATOR = new Creator<Locations>() {
        @Override
        public Locations createFromParcel(Parcel in) {
            return new Locations(in);
        }

        @Override
        public Locations[] newArray(int size) {
            return new Locations[size];
        }
    };

    public String getImgPath() {
        return imgPath;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public String getUrlcontent() {
        return urlcontent;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public void setUrlcontent(String urlcontent) {
        this.urlcontent = urlcontent;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    @Override
    public String toString() {
        return "Locations{" +
                "imgPath='" + imgPath + '\'' +
                ", latitude=" + latitude +
                ", longtitude=" + longtitude +
                ", urlcontent='" + urlcontent + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(imgPath);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longtitude);
        parcel.writeString(urlcontent);
        parcel.writeByte((byte) (isEmpty ? 1 : 0));
    }


}
