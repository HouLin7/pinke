package com.bochuan.pinke.adapter

import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bochuan.pinke.R
import com.bochuan.pinke.fragment.TeacherListFragment
import com.gome.work.common.KotlinViewHolder
import com.gome.work.common.adapter.BaseRecyclerAdapter
import com.gome.work.common.adapter.BaseViewHolder
import com.gome.work.common.imageloader.ImageLoader
import com.gome.work.core.model.UserInfo
import kotlinx.android.synthetic.main.adapter_teacher_list_item.*

class TeacherAdapter(fragmentActivity: FragmentActivity) : BaseRecyclerAdapter<UserInfo>(fragmentActivity) {

    override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<UserInfo>? {
        var view: View = LayoutInflater.from(mActivity).inflate(R.layout.adapter_teacher_list_item, null);
        return MyViewHolder(view);
    }

    override fun onBindMyViewHolder(holder: BaseViewHolder<UserInfo>?, dataItem: UserInfo?, position: Int) {
        var viewHolder: MyViewHolder = holder as MyViewHolder
        viewHolder.bind(dataItem!!, position)

    }

    inner class MyViewHolder(view: View) : KotlinViewHolder<UserInfo>(view) {

        override fun bind(t: UserInfo, position: Int) {
            tv_nick_name.text = t.nickname;
            ImageLoader.loadImage(mActivity, t.avatar, iv_avatar);
        }
    }

}
