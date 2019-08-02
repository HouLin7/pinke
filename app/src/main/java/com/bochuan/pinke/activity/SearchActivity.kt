package com.bochuan.pinke.activity

import android.os.Bundle
import com.bochuan.pinke.R
import com.gome.work.common.activity.BaseGomeWorkActivity
import kotlinx.android.synthetic.main.activity_setting.*


class SearchActivity : BaseGomeWorkActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        getCustomToolbar(title_bar).bindActivity(this, "搜索")
        initView()
    }


    override fun onBackPressed() {
        dismissInputMethod()
        super.onBackPressed()
    }
    private fun initView() {

    }



}
