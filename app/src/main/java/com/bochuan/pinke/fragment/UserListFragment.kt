package com.bochuan.pinke.fragment

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bochuan.pinke.R
import com.gome.work.common.KotlinViewHolder
import com.gome.work.common.adapter.BaseRecyclerAdapter
import com.gome.work.common.divider.CustomNewsDivider
import com.gome.work.common.imageloader.ImageLoader
import com.gome.work.core.model.UserInfo
import kotlinx.android.synthetic.main.adapter_user_list_item.*
import kotlinx.android.synthetic.main.simple_list_view.*

class UserListFragment : BaseFragment() {

    override fun getLayoutID(): Int = R.layout.simple_list_view

    private var testData: MutableList<UserInfo>? = null;

    private var mAdapter: Adapter? = null;

    init {
        testData = mutableListOf();
        var item = UserInfo()
        item.address = "11111111111111";
        item.avatar = "http://cdn.duitang.com/uploads/item/201407/24/20140724190906_MCkXs.thumb.700_0.jpeg"
        for (i in 1 until 20) {
            testData!!.add(item)
        }


    }


    override fun refreshData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        recyclerView.addItemDecoration(
            CustomNewsDivider(
                context,
                DividerItemDecoration.HORIZONTAL,
                1,
                R.color.divider_color
            )
        )

        mAdapter = Adapter(activity);
        mAdapter!!.setItemList(testData);
        recyclerView.adapter = mAdapter;

    }

    internal inner class Adapter(activity: FragmentActivity?) : BaseRecyclerAdapter<UserInfo>(activity) {

        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            var view: View = layoutInflater.inflate(R.layout.adapter_user_list_item, null);
            return MyViewHolder(view);
        }

        override fun onBindMyViewHolder(holder: RecyclerView.ViewHolder?, dataItem: UserInfo?, position: Int) {
            var viewHolder: MyViewHolder = holder as MyViewHolder
            viewHolder.bind(dataItem!!,position)

        }

        inner class MyViewHolder(view: View) : KotlinViewHolder<UserInfo>(view) {

            override fun bind(t: UserInfo, position: Int) {
                tv_user_nickname.text = t.nickname;
                tv_user_address.text = t.address
                ImageLoader.loadImage(activity, t.avatar, iv_avatar);
            }
        }

    }
}
