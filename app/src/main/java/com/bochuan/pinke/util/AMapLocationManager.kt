package com.bochuan.pinke.util

import android.content.Context
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode
import com.amap.api.location.AMapLocationListener
import com.gome.utils.GsonUtil
import com.gome.work.core.Constants
import com.gome.work.core.model.AddressItem
import com.gome.work.core.model.RegionItem
import com.gome.work.core.utils.SharedPreferencesHelper


class AMapLocationManager {

    private  var context: Context

    private var locationClient: AMapLocationClient

    constructor(context: Context) {
        this.context = context
        locationClient = AMapLocationClient(context.applicationContext)
        locationClient!!.setLocationOption(createLocationOp())
    }

//    public fun getLastLocation(): AMapLocation {
//        return locationClient!!.lastKnownLocation
//    }

    private fun start() {
        locationClient.let { it.startLocation() }
    }

    private fun stop() {
        locationClient.let { it.stopLocation() }
    }


    fun getLocation(listener: ILocationCallback) {
        locationClient.lastKnownLocation?.let {
            listener.call(locationClient.lastKnownLocation)
        }
        locationClient.setLocationListener(MyLocationListener(this, listener))
        start()
    }


    private fun createLocationOp(): AMapLocationClientOption {
        var option = AMapLocationClientOption()
        option.isNeedAddress = true
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        option.locationMode = AMapLocationMode.Hight_Accuracy
        option.interval = 2000
        return option
    }


    inner class MyLocationListener(locationManager: AMapLocationManager, callback: ILocationCallback) :
        AMapLocationListener {

        override fun onLocationChanged(location: AMapLocation) {
            if (location.errorCode != 0) {
                return;
            }
            callback?.let {
                it.call(location)
            }
            locationManager?.let { it.stop() }

            saveLocation(location)
        }

        private var callback: ILocationCallback? = null
        private var locationManager: AMapLocationManager? = null;

        init {
            this.callback = callback
            this.locationManager = locationManager
        }

    }

    companion object {


        fun toAddressItem(location: AMapLocation): AddressItem {
            var addressItem = AddressItem()

            var province = RegionItem()
            province.name = location.province

            var city = RegionItem()
            city.name = location.city
            city.code = location.cityCode

            var county = RegionItem()
            county.name = location.district

            addressItem.province = province
            addressItem.city = city
            addressItem.county = county
            addressItem.street = location.street
            addressItem.address = location.poiName
            return addressItem
        }

        private fun saveLocation(location: AMapLocation) {
            var addressItem = toAddressItem(location)
            var rawData = GsonUtil.objectToJson(addressItem)
            SharedPreferencesHelper.commitString(Constants.PreferKeys.SYS_LAST_LOCATION, rawData)
        }
    }


    interface ILocationCallback {
        fun call(loc: AMapLocation)
    }


}