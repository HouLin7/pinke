package com.bochuan.pinke.activity

import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import com.bochuan.pinke.R
import com.gome.utils.ToastUtil
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.common.utils.StringUtils
import com.gome.work.core.model.CaptchaItem
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import com.gome.work.core.utils.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_setting.*
import java.util.*
import java.util.concurrent.TimeUnit


class SettingActivity : BaseGomeWorkActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initView()
    }


    private fun initView() {
        button_logout.setOnClickListener {
            SharedPreferencesHelper.clearUserInfo()
            finish()
        }


    }


}
