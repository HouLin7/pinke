package com.bochuan.pinke.fragment

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bochuan.pinke.R
import com.bochuan.pinke.adapter.TeacherAdapter
import com.gome.utils.ToastUtil
import com.gome.work.common.divider.CustomNewsDivider
import com.gome.work.core.model.UsersRspInfo
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import kotlinx.android.synthetic.main.simple_list_view.*

class TeacherListFragment : BaseFragment() {

    override fun getLayoutID(): Int = R.layout.simple_list_view

    private var mAdapter: TeacherAdapter? = null;

    private var index = 0

    override fun refreshData() {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (mAdapter == null) {
            mAdapter = TeacherAdapter(mActivity!!);
            recyclerView.adapter = mAdapter;

            recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
            recyclerView.addItemDecoration(
                CustomNewsDivider(
                    context, DividerItemDecoration.HORIZONTAL, 1, R.color.divider_color
                )
            )
            getData(index)
        }
    }


    private fun getData(index: Int) {
        WebApi.getInstance().getTeacherList(index, 20, "", object : IResponseListener<UsersRspInfo> {
            override fun onError(code: String?, message: String?) {
                ToastUtil.showToast(mActivity, message)
            }

            override fun onSuccess(result: UsersRspInfo?) {
                mAdapter!!.setItemList(result?.items)
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

}
