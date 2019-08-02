package com.bochuan.pinke.activity

import android.os.Bundle
import com.bochuan.pinke.R
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.common.widget.BaseSingleSelectPopWindow
import com.gome.work.core.Constants
import com.gome.work.core.model.CfgDicItem
import com.gome.work.core.utils.GsonUtil
import com.gome.work.core.utils.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_post_search_partner.*
import kotlinx.android.synthetic.main.activity_setting.title_bar

/**
 * 发布信息
 */

class PostSearchPartnerActivity : BaseGomeWorkActivity() {

    var courseList = ArrayList<CfgDicItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_search_partner)
        getCustomToolbar(title_bar).bindActivity(this, "找伴读")
        initData()
        initView()
    }

    private fun initData() {
        var rawData = SharedPreferencesHelper.getString(Constants.PreferKeys.SYS_CFG_COURSE)
        courseList = GsonUtil.jsonToList(rawData, CfgDicItem::class.java) as ArrayList<CfgDicItem>

    }

    override fun onBackPressed() {
        dismissInputMethod()
        super.onBackPressed()
    }

    private fun initView() {
        layout_search_input.setOnClickListener { view ->
            var popWindow = BaseSingleSelectPopWindow(mActivity, courseList)
            popWindow.showPopupWindow(view)
        }
    }


}
