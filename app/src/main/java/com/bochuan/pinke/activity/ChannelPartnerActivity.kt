package com.bochuan.pinke.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.amap.api.location.AMapLocation
import com.bochuan.pinke.R
import com.bochuan.pinke.adapter.SearchPartnerAdapter
import com.bochuan.pinke.util.AMapLocationManager
import com.gome.applibrary.activity.BaseActivity
import com.gome.utils.CommonUtils
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.core.model.SearchPartnerItem
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import kotlinx.android.synthetic.main.activity_channel_course.*


class ChannelPartnerActivity : BaseGomeWorkActivity() {

    companion object {
        const val TYPE_TEACHER = "teacher"
        const val TYPE_COURSE = "course"
        const val TYPE_ORGANIZATION = "organization"
        const val TYPE_PARTNER = "partner"
        const val PAGE_SIZE = 20
    }

    lateinit var location: AMapLocation

    var currType = TYPE_PARTNER
    var index = 0

    private lateinit var mPartnerAdapter: SearchPartnerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.hasExtra(BaseActivity.EXTRA_DATA)) {
            currType = intent.getStringExtra(BaseActivity.EXTRA_DATA)
        }
        setContentView(R.layout.activity_channel_course)
        mPartnerAdapter = SearchPartnerAdapter(this)
        initView()
        getCustomToolbar(my_tool_bar).bindActivity(this, "")
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
        recyclerView.adapter = mPartnerAdapter
        edit_search.setOnClickListener {
            var intent = Intent(mActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        smart_refresh_layout.setOnRefreshListener {
            getData(index, "")
        }

        smart_refresh_layout.setOnLoadMoreListener {
            getData(index + 1, "")

        }

        smart_refresh_layout.autoRefresh(300)
        mPartnerAdapter.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent(this, SearchPartnerDetailActivity::class.java)
            intent.putExtra(EXTRA_DATA, mPartnerAdapter.getItem(position))
            startActivity(intent)
        }
    }

    private fun getData(pageIndex: Int, courseCode: String) {
        WebApi.getInstance()
            .getSearchPartnerList(pageIndex, PAGE_SIZE, courseCode, object : IResponseListener<SearchPartnerItem.ResponseWrapper> {
                override fun onError(code: String?, message: String?) {
                    smart_refresh_layout.finishRefresh()
                    smart_refresh_layout.finishLoadMore()
                }

                override fun onSuccess(result: SearchPartnerItem.ResponseWrapper?) {
                    mPartnerAdapter.setItemList(result?.dataItems)
                    smart_refresh_layout.finishRefresh()
                    smart_refresh_layout.finishLoadMore()
                }

            })
    }


}
