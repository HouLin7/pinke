package com.bochuan.pinke.activity

import android.os.Bundle
import android.text.TextUtils
import com.bochuan.pinke.R
import com.gome.work.common.activity.BaseGomeWorkActivity
import kotlinx.android.synthetic.main.activity_pwd_modify.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.activity_setting.title_bar


class PwdForgotActivity : BaseGomeWorkActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pwd_modify)
        getCustomToolbar(title_bar).bindActivity(this, "修改密码")
        initView()
    }


    private fun initView() {


    }

    private fun checkInput(): Boolean {
        var captcha = edit_captcha.text!!.trim()
        if (TextUtils.isEmpty(captcha)){

        }

        var pwd1 = edit_password_new.text!!.trim()

        return true;
    }


}
