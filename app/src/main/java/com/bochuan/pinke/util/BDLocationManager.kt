package com.bochuan.pinke.util

import android.content.Context
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption


class BDLocationManager(context: Context) {

    var locationClient: LocationClient? = null


    init {
        locationClient = LocationClient(context.applicationContext)
        locationClient!!.locOption = createLocationOp()
    }

    private fun start() {
        locationClient?.let { it.start() }
    }

    private fun stop() {
        locationClient?.let { it.stop() }
    }


    fun getLocation(listener: ILocationCallback) {
        locationClient!!.registerLocationListener(MyLocationListener(this, listener))
        start()
    }


    private fun createLocationOp(): LocationClientOption {
        val option = LocationClientOption()
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll")
//可选，设置返回经纬度坐标类型，默认GCJ02
//GCJ02：国测局坐标；
//BD09ll：百度经纬度坐标；
//BD09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(1000)
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.isOpenGps = true
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.isLocationNotify = true
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false)
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false)
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000)
//可选，V7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false)
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        option.setIsNeedAddress(true)
        return option

    }


    public class MyLocationListener(locationManager: BDLocationManager, callback: ILocationCallback) :
        BDAbstractLocationListener() {

        var callback: ILocationCallback? = null
        var locationManager: BDLocationManager? = null;

        init {
            this.callback = callback
            this.locationManager = locationManager
        }

        override fun onReceiveLocation(location: BDLocation) {

            callback?.let {
                it.call(location)
            }
            locationManager?.let { it.stop() }

        }
    }

    public interface ILocationCallback {
        fun call(loc: BDLocation)
    }


}