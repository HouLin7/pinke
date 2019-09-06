package com.bochuan.pinke.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bochuan.pinke.R
import com.bochuan.pinke.activity.ChannelPartnerActivity
import com.bochuan.pinke.activity.SearchPartnerDetailActivity
import com.bochuan.pinke.adapter.SearchPartnerAdapter
import com.gome.work.common.divider.CustomNewsDivider
import com.gome.work.core.model.SearchPartnerItem
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import kotlinx.android.synthetic.main.simple_list_view.*

class SearchPartnerListFragment : BaseFragment() {

    var mAdapter: SearchPartnerAdapter? = null

    var index = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (mAdapter == null) {
            mAdapter = SearchPartnerAdapter(mActivity!!)
            recyclerView.adapter = mAdapter
            recyclerView.layoutManager = LinearLayoutManager(mActivity!!)
            recyclerView.addItemDecoration(
                CustomNewsDivider(context, DividerItemDecoration.HORIZONTAL, 2, R.color.divider_color)
            )
            getData(index, "")

            mAdapter!!.setOnItemClickListener { parent, view, position, id ->
                val item = mAdapter!!.getItem(position)
                var intent = Intent(mActivity, SearchPartnerDetailActivity::class.java)
                intent.putExtra(EXTRA_DATA, item)
                startActivity(intent)

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun getLayoutID(): Int {
        return R.layout.simple_list_view
    }

    override fun refreshData() {

    }


    private fun getData(pageIndex: Int, courseCode: String) {
        WebApi.getInstance()
            .getSearchPartnerList(pageIndex,
                ChannelPartnerActivity.PAGE_SIZE, courseCode, object : IResponseListener<SearchPartnerItem.ResponseWrapper> {
                    override fun onError(code: String?, message: String?) {

                    }

                    override fun onSuccess(result: SearchPartnerItem.ResponseWrapper?) {
                        mAdapter!!.setItemList(result?.dataItems)

                    }

                })
    }


}
