package com.bochuan.pinke.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.amap.api.location.AMapLocation
import com.bochuan.pinke.R
import com.bochuan.pinke.adapter.OrganizationAdapter
import com.bochuan.pinke.util.AMapLocationManager
import com.gome.utils.CommonUtils
import com.gome.work.common.activity.BaseGomeWorkActivity
import kotlinx.android.synthetic.main.activity_channel_course.*


class ChannelOrganizationActivity : BaseGomeWorkActivity() {

    lateinit var location: AMapLocation
    lateinit var mAdapter: OrganizationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_course)
        mAdapter = OrganizationAdapter(this)
        initView()
        getLocation()
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
                var locManager = AMapLocationManager(this)
                locManager.getLocation(object : AMapLocationManager.ILocationCallback {
                    override fun call(loc: AMapLocation) {
                        tv_address.text = loc.aoiName
                        tv_city.text = loc.city
                        location = loc
                    }
                })
            } else {

            }
        }

    }

    override fun onBackPressed() {
        dismissInputMethod()
        super.onBackPressed()
    }

    private fun initView() {
        smart_refresh_layout.setEnableLoadMore(false)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter
        edit_search.setOnClickListener {
            var intent = Intent(mActivity, SearchActivity::class.java)
            startActivity(intent)

        }
    }


}
