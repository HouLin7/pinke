package com.bochuan.pinke.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.amap.api.location.AMapLocation
import com.bochuan.pinke.R
import com.bochuan.pinke.adapter.UserAdapter
import com.gome.utils.ToastUtil
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.core.model.UsersRspInfo
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import kotlinx.android.synthetic.main.activity_user_list.*


class UsersListActivity : BaseGomeWorkActivity() {

    companion object {
        const val RELATION_TYPE_FRIEND = 1;
        const val RELATION_TYPE_FOLLOWER = 2;
        const val RELATION_TYPE_PARTNER = 3;
    }

    lateinit var location: AMapLocation
    lateinit var mAdapter: UserAdapter

    private var relationType = RELATION_TYPE_FRIEND

    private var index = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        relationType = intent.getIntExtra(EXTRA_DATA, RELATION_TYPE_FRIEND)
        mAdapter = UserAdapter(this)
        mAdapter.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent(mActivity, TeacherHomeActivity::class.java)
            startActivity(intent)

        }
        getCustomToolbar(my_tool_bar).bindActivity(this, getMyTitle())
        initView()
    }

    private fun getMyTitle(): String {
        when (relationType) {
            RELATION_TYPE_FRIEND -> return "好友"
            RELATION_TYPE_FOLLOWER -> return "粉丝"
            RELATION_TYPE_PARTNER -> return "伴读"
        }
        return ""

    }

    private fun getFriends(index: Int) {
        WebApi.getInstance().getFriends(index, 20, object : IResponseListener<UsersRspInfo> {
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

    private fun getFollowers(index: Int) {
        WebApi.getInstance().getFollowers(index, 20, object : IResponseListener<UsersRspInfo> {
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

    private fun getPartners(index: Int) {
        WebApi.getInstance().getPartners(index, 20, object : IResponseListener<UsersRspInfo> {
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


    private fun getData(index: Int) {
        when (index) {
            RELATION_TYPE_FRIEND -> getFriends(index)
            RELATION_TYPE_FOLLOWER -> getFollowers(index)
            RELATION_TYPE_PARTNER -> getPartners(index)
        }
    }

    private fun initView() {
        smart_refresh_layout.setEnableLoadMore(false)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter

        smart_refresh_layout.setOnRefreshListener { getData(index) }
        smart_refresh_layout.setOnLoadMoreListener { }
        smart_refresh_layout.autoRefresh(300)
    }


}
