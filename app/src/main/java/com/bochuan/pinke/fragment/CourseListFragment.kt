package com.bochuan.pinke.fragment

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bochuan.pinke.R
import com.bochuan.pinke.adapter.CourseAdapter
import com.gome.work.common.divider.CustomNewsDivider
import kotlinx.android.synthetic.main.simple_list_view.*

class CourseListFragment : BaseFragment() {

    var adapter: CourseAdapter? = null

    override fun getLayoutID(): Int {
        return R.layout.simple_list_view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (adapter == null) {
            adapter = CourseAdapter(mActivity!!)
            recyclerView.adapter = adapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(activity);
        recyclerView.addItemDecoration(
            CustomNewsDivider(context, DividerItemDecoration.HORIZONTAL, 2, R.color.divider_color)
        )

    }

    override fun refreshData() {

    }


}
