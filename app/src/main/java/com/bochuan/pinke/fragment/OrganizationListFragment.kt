package com.bochuan.pinke.fragment

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bochuan.pinke.R
import com.gome.work.common.KotlinViewHolder
import com.gome.work.common.adapter.BaseRecyclerAdapter
import com.gome.work.common.divider.CustomNewsDivider
import com.gome.work.core.model.OrganizationItem
import kotlinx.android.synthetic.main.adapter_organization_list_item.*
import kotlinx.android.synthetic.main.simple_list_view.*

class OrganizationListFragment : BaseFragment() {

    override fun refreshData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var contentView: View = View.inflate(activity, R.layout.simple_list_view, null);
        return contentView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(
            CustomNewsDivider(
                context,
                DividerItemDecoration.HORIZONTAL,
                2,
                R.color.divider_color
            )
        )
        recyclerView.adapter = Adapter(activity)
    }


    internal inner class Adapter(activity: FragmentActivity?) : BaseRecyclerAdapter<OrganizationItem>(activity) {

        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            var view: View = layoutInflater.inflate(R.layout.adapter_organization_list_item, null);
            return MyViewHolder(view);
        }

        override fun onBindMyViewHolder(holder: RecyclerView.ViewHolder?, dataItem: OrganizationItem?, position: Int) {
            var myViewholder: MyViewHolder = holder as MyViewHolder
            myViewholder.bind(dataItem!!)

        }

        inner class MyViewHolder(view: View) : KotlinViewHolder<OrganizationItem>(view) {

            override fun bind(t: OrganizationItem) {
                tv_nickname.text = t.name;
                tv_address.text = t.address
            }
        }

    }
}
