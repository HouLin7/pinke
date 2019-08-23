package com.bochuan.pinke.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amap.api.location.AMapLocation
import com.bochuan.pinke.R
import com.bochuan.pinke.util.AMapLocationManager
import com.gome.applibrary.activity.BaseActivity
import com.gome.utils.CommonUtils
import com.gome.work.common.KotlinViewHolder
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.common.adapter.BaseRecyclerAdapter
import com.gome.work.common.adapter.BaseViewHolder
import com.gome.work.common.imageloader.ImageLoader
import com.gome.work.core.model.SearchPartnerItem
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_channel_course.*
import kotlinx.android.synthetic.main.adapter_search_partner_item.*


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
        iv_back.setOnClickListener {
            onBackPressed()
        }
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
    }

    private fun getData(pageIndex: Int, courseCode: String) {
        WebApi.getInstance()
            .getSearchPartnerInfo(pageIndex, PAGE_SIZE, courseCode, object : IResponseListener<SearchPartnerItem.ResponseWrapper> {
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

    inner class SearchPartnerAdapter(activity: FragmentActivity?) : BaseRecyclerAdapter<SearchPartnerItem>(activity) {

        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<SearchPartnerItem> {
            var view = LayoutInflater.from(mActivity).inflate(R.layout.adapter_search_partner_item, null, false)
            return ViewHolderItem(view!!)
        }

        override fun onBindMyViewHolder(holder: BaseViewHolder<SearchPartnerItem>?, dataItem: SearchPartnerItem?, position: Int) {
            holder!!.bind(dataItem, position)
        }


        inner class ViewHolderItem(view: View) : KotlinViewHolder<SearchPartnerItem>(view) {
            override fun bind(t: SearchPartnerItem, position: Int) {
                tv_user_name.text = t.userInfo?.nickname
                tv_school.text = t.school
                tv_sex.text = t.sex
                tv_class_type.text = t.classType

                ImageLoader.loadImage(mActivity, t.userInfo?.avatar, iv_avatar)

            }
        }

    }

}
