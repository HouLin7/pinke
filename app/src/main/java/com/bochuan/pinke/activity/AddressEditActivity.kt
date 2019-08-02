package com.bochuan.pinke.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.baidu.location.BDLocation
import com.bochuan.pinke.R
import com.bochuan.pinke.util.BCLocationManager
import com.gome.utils.CommonUtils
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.core.event.EventDispatcher
import com.gome.work.core.event.model.EventInfo
import kotlinx.android.synthetic.main.activity_adress_edit.*


/**
 * 地址编辑界面
 */
class AddressEditActivity : BaseGomeWorkActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adress_edit)
        getCustomToolbar(title_bar).bindActivity(this, "选择可上课地址")
        getLocation()
        layout_edit_city.setOnClickListener {
            var intent = Intent(mActivity,CitySelectActivity::class.java);
            startActivity(intent)
        }
    }

    private fun getLocation() {
        if (!CommonUtils.isGPSOpen(this)) {
            showAlertDlg("请先打开手机定位开关")
            return;
        }
        requestPermission(
            Manifest.permission.ACCESS_FINE_LOCATION
        ) { permission, isSuccess ->
            if (isSuccess) {
                val locationManager = BCLocationManager(mActivity);
                locationManager.getLocation(object : BCLocationManager.ILocationCallback {
                    override fun call(loc: BDLocation) {
                        tv_location_address.text = loc.addrStr
                        tv_city.text = loc.city
                        EventDispatcher.postEvent(EventInfo.FLAG_LOCATION_RECEIVE, loc)
                    }
                })
            } else {

            }

        }
    }

    companion object {
        /**
         * 标记在程序一次生命周期内，是否已经展示过了
         */
        private var isShowed: Boolean = false
    }


}
