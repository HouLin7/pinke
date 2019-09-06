package com.bochuan.pinke.activity

import android.content.Intent
import android.os.Bundle
import com.bochuan.pinke.R
import com.gome.utils.CommonUtils
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.core.utils.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : BaseGomeWorkActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        getCustomToolbar(title_bar).bindActivity(this, "设置")
        initView()
    }


    private fun initView() {
        button_logout.setOnClickListener {
            SharedPreferencesHelper.clearUserInfo()
            finish()
        }
        layout_modify_password.setOnClickListener {
            var intent = Intent(mActivity, PwdModifyActivity::class.java)
            startActivity(intent)
        }
        tv_version_name.text = CommonUtils.getVersionName(this)
    }


}
