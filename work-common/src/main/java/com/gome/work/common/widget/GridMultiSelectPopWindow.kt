package com.gome.work.common.widget

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Button
import com.gome.work.common.KotlinViewHolder
import com.gome.work.common.R
import com.gome.work.common.adapter.BaseRecyclerAdapter
import com.gome.work.common.adapter.BaseViewHolder
import com.gome.work.common.adapter.SelectRecyclerAdapter
import com.gome.work.common.divider.CustomNewsDivider
import com.gome.work.core.Constants
import com.gome.work.core.model.CfgDicItem
import com.gome.work.core.model.SysCfgData
import com.gome.work.core.utils.GsonUtil
import com.gome.work.core.utils.SharedPreferencesHelper
import kotlinx.android.synthetic.main.adapter_pop_select_item.*
import kotlinx.android.synthetic.main.adapter_simple_textview_item.*
import razerdp.basepopup.BasePopupWindow

open class GridMultiSelectPopWindow(fragmentActivity: FragmentActivity) :
    BasePopupWindow(fragmentActivity) {

    private var animationView: View? = null
    private var conditionTypeList = ArrayList<TypeItem>()
    var conditionTypes: ArrayList<ConditionType> = ArrayList()
        set(value) {
            field = value
        }

    var sysCfgData: SysCfgData

    var typeAdapter: AdapterPopWindowSelect? = null

    init {

        var rawData = SharedPreferencesHelper.getString(Constants.PreferKeys.SYS_CONFIG_DATA)
        sysCfgData = GsonUtil.jsonToBean(rawData, SysCfgData::class.java)

        var values = ArrayList<CfgDicItem>()
        values.add(CfgDicItem("是", "1"))
        values.add(CfgDicItem("否", "0"))
        var typeItem = TypeItem(ConditionType.FindSameSchool, "找同校同学", values)
        conditionTypeList.add(typeItem)

        typeItem = TypeItem(ConditionType.Sex, "性别", SysCfgData.getSexCfgItems())
        conditionTypeList.add(typeItem)


        typeItem = TypeItem(ConditionType.ClassType, "班型", sysCfgData.classType)
        conditionTypeList.add(typeItem)

        typeItem = TypeItem(ConditionType.ClassType, "年级", sysCfgData.grade)
        conditionTypeList.add(typeItem)

        typeAdapter!!.setItemList(conditionTypeList)
    }

    class TypeItem(var type: ConditionType, var name: String, var values: List<CfgDicItem>) {

    }


    enum class ConditionType {
        FindSameSchool, Sex, ClassType, Grade, Price
    }


    override fun onCreateContentView(): View {
        val contentView = LayoutInflater.from(context).inflate(R.layout.popup_multi_select_item, null)
        val recyclerView = contentView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(
            CustomNewsDivider(
                context,
                DividerItemDecoration.HORIZONTAL,
                2,
                R.color.divider_color
            )
        )
        typeAdapter = AdapterPopWindowSelect(context as FragmentActivity)
        recyclerView.adapter = typeAdapter
        animationView = recyclerView.parent as View
        var btnOk = contentView.findViewById<Button>(R.id.btn_ok)
        btnOk.setOnClickListener { dismiss() }
        var btnCancel = contentView.findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener { dismiss() }
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


    inner class AdapterPopWindowSelect(activity: FragmentActivity) : BaseRecyclerAdapter<TypeItem>(activity) {
        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<TypeItem>? {
            var view = LayoutInflater.from(mActivity).inflate(R.layout.adapter_pop_select_item, null, false);
            var holder = ViewHolderItem(view);
            return holder
        }

        override fun onBindMyViewHolder(holder: BaseViewHolder<TypeItem>?, dataItem: TypeItem?, position: Int) {
            var myHolder = holder as ViewHolderItem
            myHolder.bind(dataItem!!, position)
        }

        inner class ViewHolderItem(view: View) : KotlinViewHolder<TypeItem>(view) {
            init {
                grid_view.layoutManager = GridLayoutManager(context, 4)
                grid_view.adapter = AdapterPopWindowSelectChild(context as FragmentActivity)
            }

            override fun bind(t: TypeItem, position: Int) {
                tv_category_name.text = t.name
                var adapter = grid_view.adapter as BaseRecyclerAdapter<CfgDicItem>
                adapter.setItemList(t.values)
            }
        }

    }


    inner class AdapterPopWindowSelectChild(fragmentActivity: FragmentActivity) :
        SelectRecyclerAdapter<CfgDicItem>(fragmentActivity, CHECK_GRAVITY_LEFT, SELECT_REGION_GLOBAL, SelectModel.multi) {
        override fun onCreateSelectHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<CfgDicItem> {
            var view = LayoutInflater.from(mActivity).inflate(R.layout.adapter_simple_textview_item, null, false);
            return ViewHolderItemChild(view);
        }

        override fun onBindSelectHolder(holder: BaseViewHolder<CfgDicItem>?, dataItem: CfgDicItem?) {
            var myHolder = holder as ViewHolderItemChild
            myHolder.bind(dataItem!!, 1)
        }

        inner class ViewHolderItemChild(view: View) : KotlinViewHolder<CfgDicItem>(view) {
            override fun bind(t: CfgDicItem, position: Int) {
                textview.text = t.name
            }

        }

    }


}
