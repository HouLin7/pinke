package com.gome.work.common.widget

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import com.gome.work.common.KotlinViewHolder
import com.gome.work.common.R
import com.gome.work.common.adapter.BaseRecyclerAdapter
import com.gome.work.common.adapter.BaseViewHolder
import com.gome.work.core.model.CfgDicItem
import kotlinx.android.synthetic.main.adapter_pop_select_child_item.*
import kotlinx.android.synthetic.main.adapter_pop_select_item.*
import kotlinx.android.synthetic.main.popup_menu_listview.*
import razerdp.basepopup.BasePopupWindow

class BaseMultiSelectPopWindow(private var fragmentActivity: FragmentActivity, var dataList: List<CfgDicItem>) :
    BasePopupWindow(fragmentActivity) {

    private var animationView: View? = null


    override fun onCreateContentView(): View {
        val contentView = LayoutInflater.from(fragmentActivity).inflate(R.layout.popup_slide_from_bottom_listview, null)
        val recyclerView = contentView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(fragmentActivity)
        recyclerView.itemAnimator = DefaultItemAnimator()

        var adapter: BaseRecyclerAdapter<CfgDicItem>?
        if (dataList.first().isHasChild) {
            adapter = AdapterPopWindowSelect(fragmentActivity)
        } else {
            adapter = AdapterPopWindowSelectChild(fragmentActivity)
        }
        recyclerView.adapter = adapter
        adapter.setItemList(dataList)
        animationView = recyclerView
        return contentView
    }

    override fun onCreateAnimateView(): View? {
        return animationView
    }

    override fun onCreateShowAnimation(): Animation {
        return getTranslateVerticalAnimation(0, 1, 500)
    }

    override fun onCreateDismissAnimation(): Animation {
        return getTranslateVerticalAnimation(1, 0, 500)
    }


    inner class AdapterPopWindowSelect(activity: FragmentActivity) : BaseRecyclerAdapter<CfgDicItem>(activity) {
        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<CfgDicItem>? {
            var view = LayoutInflater.from(mActivity).inflate(R.layout.adapter_pop_select_item, null);
            var holder = ViewHolderItem(view);
            holder.recyclerView.layoutManager = GridLayoutManager(context, 4)
            holder.recyclerView.adapter = AdapterPopWindowSelectChild(fragmentActivity)
            return holder
        }

        override fun onBindMyViewHolder(holder: BaseViewHolder<CfgDicItem>?, dataItem: CfgDicItem?, position: Int) {
            var myHolder = holder as ViewHolderItem
            myHolder.bind(dataItem!!, position)
        }

        inner class ViewHolderItem(view: View) : KotlinViewHolder<CfgDicItem>(view) {
            override fun bind(t: CfgDicItem, position: Int) {
                tv_category_name.text = t.name
                var adapter = grid_view.adapter as BaseRecyclerAdapter<CfgDicItem>
                adapter.setItemList(t.children)
            }
        }

    }


    inner class AdapterPopWindowSelectChild(activity: FragmentActivity) : BaseRecyclerAdapter<CfgDicItem>(activity) {
        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<CfgDicItem>? {
            var view = LayoutInflater.from(mActivity).inflate(R.layout.adapter_pop_select_child_item, null);
            return ViewHolderItemChild(view);
        }

        override fun onBindMyViewHolder(holder: BaseViewHolder<CfgDicItem>?, dataItem: CfgDicItem?, position: Int) {
            var myHolder = holder as ViewHolderItemChild
            myHolder.bind(dataItem!!, position)
        }

        inner class ViewHolderItemChild(view: View) : KotlinViewHolder<CfgDicItem>(view) {
            override fun bind(t: CfgDicItem, position: Int) {
                checkbox.text = t.name
                checkbox.isChecked = t.isSelected
            }

        }

    }

    interface OnCfgItemSelectListener {
        fun onSelect(dataList: List<CfgDicItem>)
    }

}
