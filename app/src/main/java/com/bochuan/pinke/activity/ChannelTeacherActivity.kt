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
import kotlinx.android.synthetic.main.adapter_search_partner_item.*
import kotlinx.android.synthetic.main.activity_channel_course.*


class ChannelTeacherActivity : BaseGomeWorkActivity() {

    companion object {
        const val TYPE_TEACHER = "teacher"
        const val TYPE_COURSE = "course"
        const val TYPE_ORGANIZATION = "organization"
        const val TYPE_PARTNER = "partner"

        const val VIEW_TYPE_HEADER = 0

        const val VIEW_TYPE_NOMAL = 1
    }

    lateinit var location: AMapLocation

    var currType = TYPE_PARTNER

    lateinit var mPartnerAdapter: SearchPartnerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.hasExtra(BaseActivity.EXTRA_DATA)) {
            currType = intent.getStringExtra(BaseActivity.EXTRA_DATA)
        }
        setContentView(R.layout.activity_channel_course)
        mPartnerAdapter = SearchPartnerAdapter(this)
        mPartnerAdapter.addItem(null)
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
        recyclerView.adapter = mPartnerAdapter
        edit_search.setOnClickListener {
            var intent = Intent(mActivity, SearchActivity::class.java)
            startActivity(intent)

        }
    }


    inner class SearchPartnerAdapter(activity: FragmentActivity?) : BaseRecyclerAdapter<SearchPartnerItem>(activity) {

        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<SearchPartnerItem> {
            var view: View? = null
            when (viewType) {
                VIEW_TYPE_HEADER -> {
                    view = LayoutInflater.from(mActivity).inflate(R.layout.search_condition_panel, null, false)
                    return ViewHolderHeader(view!!)
                }
                else -> {
                    view = LayoutInflater.from(mActivity).inflate(R.layout.adapter_search_partner_item, null, false)
                    return ViewHolderItem(view!!)
                }

            }
        }

        override fun onBindMyViewHolder(holder: BaseViewHolder<SearchPartnerItem>?, dataItem: SearchPartnerItem?, position: Int) {
            holder!!.bind(dataItem, position)
        }

        override fun getItemViewType(position: Int): Int {
            if (position == 0) {
                return VIEW_TYPE_HEADER
            } else {
                return VIEW_TYPE_NOMAL
            }
        }


        inner class ViewHolderItem(view: View) : KotlinViewHolder<SearchPartnerItem>(view) {
            override fun bind(t: SearchPartnerItem, position: Int) {
                tv_user_name.text = t.userInfo?.nickname
                tv_school.text = t.school
                tv_sex.text = t.sex
                tv_class_type.text = t.classType
                ImageLoader.loadImage(mActivity, t.userInfo.avatar, iv_avatar)

            }
        }

    }

    inner class ViewHolderHeader(view: View) : KotlinViewHolder<SearchPartnerItem>(view) {
        override fun bind(t: SearchPartnerItem?, position: Int) {


        }
    }


}
