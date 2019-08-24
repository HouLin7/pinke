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
import com.gome.utils.CommonUtils
import com.gome.utils.ToastUtil
import com.gome.work.common.KotlinViewHolder
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.common.adapter.BaseRecyclerAdapter
import com.gome.work.common.adapter.BaseViewHolder
import com.gome.work.core.model.UserInfo
import com.gome.work.core.model.UsersRspInfo
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import kotlinx.android.synthetic.main.activity_channel_course.*


class ChannelTeacherActivity : BaseGomeWorkActivity() {

    lateinit var location: AMapLocation
    lateinit var mAdapter: SearchTeacherAdapter

    private var index = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_course)
        mAdapter = SearchTeacherAdapter(this)
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

    private fun getData(index: Int) {
        WebApi.getInstance().getTeacherList(index, 20, "", object : IResponseListener<UsersRspInfo> {
            override fun onError(code: String?, message: String?) {
                ToastUtil.showToast(mActivity, message)
                smart_refresh_layout.finishLoadMore()
                smart_refresh_layout.finishRefresh()
            }

            override fun onSuccess(result: UsersRspInfo?) {
                mAdapter.setItemList(result?.items)
                smart_refresh_layout.finishLoadMore()
                smart_refresh_layout.finishRefresh()
            }

        })
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

        smart_refresh_layout.setOnRefreshListener { getData(index) }
        smart_refresh_layout.setOnLoadMoreListener { }
        smart_refresh_layout.autoRefresh(300)
    }


    inner class SearchTeacherAdapter(activity: FragmentActivity?) : BaseRecyclerAdapter<UserInfo>(activity) {

        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<UserInfo> {
            var view = LayoutInflater.from(mActivity).inflate(R.layout.adapter_teacher_list_item, null, false)
            return ViewHolderItem(view!!)
        }

        override fun onBindMyViewHolder(holder: BaseViewHolder<UserInfo>?, dataItem: UserInfo?, position: Int) {
            holder!!.bind(dataItem, position)
        }

        inner class ViewHolderItem(view: View) : KotlinViewHolder<UserInfo>(view) {
            override fun bind(t: UserInfo, position: Int) {


            }
        }

    }


}
