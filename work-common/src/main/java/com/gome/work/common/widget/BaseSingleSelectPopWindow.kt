package com.gome.work.common.widget

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import com.gome.work.common.KotlinViewHolder
import com.gome.work.common.R
import com.gome.work.common.adapter.BaseViewHolder
import com.gome.work.common.adapter.SelectRecyclerAdapter
import com.gome.work.core.model.CfgDicItem
import kotlinx.android.synthetic.main.adapter_simple_textview_item.*
import razerdp.basepopup.BasePopupWindow

class BaseSingleSelectPopWindow(fragmentActivity: FragmentActivity, dataList: List<CfgDicItem>) :
    BasePopupWindow(fragmentActivity, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) {

    private var adapter1: Adapter? = null;

    private var adapter2: Adapter? = null;
    var recyclerView1: RecyclerView? = null
    var recyclerView2: RecyclerView? = null

    var listener: BaseMultiSelectPopWindow.OnCfgItemSelectListener? = null

    private lateinit var animationView: View

    init {
        adapter1 = Adapter(fragmentActivity)
        adapter1!!.setItemList(dataList)
        recyclerView1!!.adapter = adapter1
        if (!dataList.first().isHasChild) {
            recyclerView2!!.visibility = View.GONE
            adapter1!!.setOnItemSelectedChangeListener { dataItem, isSelect ->
                listener!!.onSelect(arrayListOf(dataItem))
                dismiss()
            }

        } else {
            adapter1!!.setOnItemClickListener { parent, view, position, id ->
                var dataList = adapter1!!.getItem(position)
                adapter2!!.setItemList(dataList.children)
            }

            recyclerView2!!.visibility = View.VISIBLE
            adapter2 = Adapter(fragmentActivity)
            adapter2!!.setItemList(dataList.first().children)
            recyclerView2!!.adapter = adapter2
            adapter2!!.setOnItemSelectedChangeListener { dataItem, isSelect ->
                listener!!.onSelect(arrayListOf(dataItem))
                dismiss()
            }

        }
    }


//    private fun convertStringList(dataList: List<CfgDicItem>): Array<String?> {
//        var result = arrayOfNulls<String>(dataList.size)
//        var index = 0
//        for (item in dataList) {
//            result[index] = item.name
//            index++
//        }
//        return result
//    }

    override fun onCreateContentView(): View {
        val contentView = LayoutInflater.from(context).inflate(R.layout.popup_single_select, null)
        recyclerView1 = contentView.findViewById<RecyclerView>(R.id.recyclerView1)
        recyclerView2 = contentView.findViewById<RecyclerView>(R.id.recyclerView2)

        recyclerView1!!.layoutManager = LinearLayoutManager(context)
        recyclerView2!!.layoutManager = LinearLayoutManager(context)
        animationView = contentView.findViewById(R.id.layout_view)

        return contentView
    }

    override fun onCreateAnimateView(): View? {
        return animationView
    }

    override fun onCreateShowAnimation(): Animation {
        return getTranslateVerticalAnimation(-1f, 0f, 300)
    }

    override fun onCreateDismissAnimation(): Animation {
        return getTranslateVerticalAnimation(0f, -1f, 300)
    }


    inner class Adapter(fragmentActivity: FragmentActivity) :
        SelectRecyclerAdapter<CfgDicItem>(fragmentActivity, CHECK_GRAVITY_LEFT, SELECT_REGION_GLOBAL, SelectModel.single) {

        override fun onCreateSelectHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<CfgDicItem> {
            var view = LayoutInflater.from(context).inflate(R.layout.adapter_simple_textview_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindSelectHolder(holder: BaseViewHolder<CfgDicItem>?, dataItem: CfgDicItem?) {
            var myHolder = holder as ViewHolder
            myHolder.bind(dataItem!!, 0);
        }


        inner class ViewHolder(view: View) : KotlinViewHolder<CfgDicItem>(view) {
            override fun bind(t: CfgDicItem, position: Int) {
                textview.text = t.name
            }

        }
    }


}
