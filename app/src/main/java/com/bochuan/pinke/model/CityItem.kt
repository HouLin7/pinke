package com.bochuan.pinke.model

import com.gome.work.common.widget.indexview.indexbar.bean.BaseIndexPinyinBean
import com.gome.work.core.model.RegionItem

class CityItem(var regionItem: RegionItem) : BaseIndexPinyinBean() {


    override fun getTarget(): String {
        return regionItem.name
    }

}