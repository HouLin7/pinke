package com.bochuan.pinke.fragment

import android.os.Bundle
import android.view.View
import com.bochuan.pinke.R
import com.gome.work.common.widget.BaseMultiSelectPopWindow
import com.gome.work.common.widget.BaseSingleSelectPopWindow
import com.gome.work.core.model.CfgDicItem
import kotlinx.android.synthetic.main.search_condition_panel.*

class SearchConditionFragment : BaseFragment() {

    private var distanceConditions = ArrayList<CfgDicItem>()
    private var distancePopWindow: BaseSingleSelectPopWindow? = null

    init {

        var item = CfgDicItem();
        item.name = "ddd"
        distanceConditions.add(item)

    }

    override fun refreshData() {

    }

    override fun getLayoutID(): Int {
        return R.layout.search_condition_panel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_condition_1.setOnClickListener {
            if (distancePopWindow == null) {
                var popWindow = BaseSingleSelectPopWindow(mActivity!!, distanceConditions)
                popWindow.showPopupWindow(it)
                popWindow.listener = object : BaseMultiSelectPopWindow.OnCfgItemSelectListener {
                    override fun onSelect(dataList: List<CfgDicItem>) {

                    }
                }
                distancePopWindow = popWindow
            } else {
                distancePopWindow!!.showPopupWindow(it)
            }

        }
        tv_condition_2.setOnClickListener { }
        tv_condition_3.setOnClickListener { }
        tv_condition_4.setOnClickListener { }

    }

}