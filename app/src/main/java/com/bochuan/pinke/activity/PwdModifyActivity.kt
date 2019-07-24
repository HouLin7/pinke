package com.bochuan.pinke.activity

import android.os.Bundle
import com.bochuan.pinke.R
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.core.utils.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_setting.*


class PwdModifyActivity : BaseGomeWorkActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        getCustomToolbar(title_bar).bindActivity(this)
        initView()
    }


    private fun initView() {
        button_logout.setOnClickListener {
            SharedPreferencesHelper.clearUserInfo()
            finish()
        }
        layout_modify_password.setOnClickListener{
            
        }

    }


}
