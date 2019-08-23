package com.bochuan.pinke.fragment

import android.os.Bundle
import android.view.View
import com.bochuan.pinke.R
import com.gome.work.common.widget.BaseSelectPopWindow
import com.gome.work.common.widget.GridMultiSelectPopWindow
import com.gome.work.common.widget.ListSelectPopWindow
import com.gome.work.core.model.CfgDicItem
import kotlinx.android.synthetic.main.search_condition_panel.*

class SearchConditionFragment : BaseFragment() {

    private var distanceConditions = ArrayList<CfgDicItem>()

    private var distancePopWindow: ListSelectPopWindow? = null

    private var gradePopWindow: ListSelectPopWindow? = null

    private var coursePopWindow: ListSelectPopWindow? = null

    private var multiSelectPopWindow: GridMultiSelectPopWindow? = null

    init {

        distanceConditions.add(CfgDicItem("dd", "1"))

    }

    override fun refreshData() {

    }

    override fun getLayoutID(): Int {
        return R.layout.search_condition_panel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layout_condition1.setOnClickListener {
            if (distancePopWindow == null) {
                var popWindow = ListSelectPopWindow(mActivity!!, distanceConditions)
                popWindow.listener = object : BaseSelectPopWindow.OnCfgItemSelectListener {
                    override fun onSelect(dataList: List<CfgDicItem>) {

                    }
                }
                distancePopWindow = popWindow
            }
            distancePopWindow!!.showPopupWindow(it)

        }
        layout_condition2.setOnClickListener {
            if (gradePopWindow == null) {
                var popWindow = ListSelectPopWindow(mActivity!!, mActivity!!.sysCfgData.grade)
                popWindow.listener = object : BaseSelectPopWindow.OnCfgItemSelectListener {
                    override fun onSelect(dataList: List<CfgDicItem>) {

                    }
                }
                gradePopWindow = popWindow

            }
            gradePopWindow!!.showPopupWindow(it)
        }
        layout_condition3.setOnClickListener {
            if (coursePopWindow == null) {
                var popWindow = ListSelectPopWindow(mActivity!!, mActivity!!.sysCfgData.course)
                popWindow.listener = object : BaseSelectPopWindow.OnCfgItemSelectListener {
                    override fun onSelect(dataList: List<CfgDicItem>) {

                    }
                }
                coursePopWindow = popWindow
            }
            coursePopWindow!!.showPopupWindow(it)
        }
        layout_condition4.setOnClickListener {
            if (multiSelectPopWindow == null) {
                var popWindow = GridMultiSelectPopWindow(mActivity!!)
                multiSelectPopWindow = popWindow

            }
            multiSelectPopWindow!!.showPopupWindow(it)
        }

    }

}