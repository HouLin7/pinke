package com.gome.work.common.widget

import android.support.v4.app.FragmentActivity
import com.gome.work.core.model.CfgDicItem
import razerdp.basepopup.BasePopupWindow

abstract class BaseSelectPopWindow(var fragmentActivity: FragmentActivity, var dataList: List<CfgDicItem>, var selectModel: SelectModel) :
    BasePopupWindow(fragmentActivity) {

    enum class SelectModel {
        Single, Multi
    }


    interface OnCfgItemSelectListener {
        fun onSelect(dataList: List<CfgDicItem>)
    }

}
