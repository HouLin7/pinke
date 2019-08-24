package com.bochuan.pinke.adapter

import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bochuan.pinke.R
import com.gome.work.common.KotlinViewHolder
import com.gome.work.common.adapter.BaseRecyclerAdapter
import com.gome.work.common.adapter.BaseViewHolder
import com.gome.work.common.imageloader.ImageLoader
import com.gome.work.core.model.SearchPartnerItem
import com.gome.work.core.model.UserInfo
import kotlinx.android.synthetic.main.adapter_search_partner_list_item.*

class SearchPartnerAdapter(fragmentActivity: FragmentActivity) : BaseRecyclerAdapter<SearchPartnerItem>(fragmentActivity) {

    override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<SearchPartnerItem> {
        var view = LayoutInflater.from(mActivity).inflate(R.layout.adapter_search_partner_list_item, null, false)
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
