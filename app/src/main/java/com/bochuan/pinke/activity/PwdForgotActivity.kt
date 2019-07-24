package com.bochuan.pinke.activity

import android.os.Bundle
import android.text.TextUtils
import com.bochuan.pinke.R
import com.gome.utils.ToastUtil
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import kotlinx.android.synthetic.main.activity_pwd_forgot.*
import kotlinx.android.synthetic.main.activity_setting.title_bar


class PwdForgotActivity : BaseGomeWorkActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pwd_forgot)
        getCustomToolbar(title_bar).bindActivity(this, "忘记密码")
        button_confirm.setOnClickListener {
            if (checkInput()) {
                doForget()
            }
        }
    }

    private fun doForget() {
        var phone = edit_phone.text!!.trim().toString()
        var captcha = edit_captcha.text!!.trim().toString()
        var passowrd = edit_password.text!!.trim().toString()

        WebApi.getInstance().forgetPassword(phone, passowrd, captcha, object : IResponseListener<String> {
            override fun onError(code: String?, message: String?) {

            }

            override fun onSuccess(result: String?) {

            }

        })
    }

    private fun checkInput(): Boolean {
        var phone = edit_phone.text!!.trim()
        if (TextUtils.isEmpty(phone)) {
            edit_phone.requestFocus()
            ToastUtil.showToast(mActivity, "请输入手机号")
            return false;
        }

        var captcha = edit_captcha.text!!.trim()
        if (TextUtils.isEmpty(captcha)) {
            edit_captcha.requestFocus()
            ToastUtil.showToast(mActivity, "请输入验证码")
            return false;
        }

        var pwd1 = edit_password.text!!.trim()
        if (TextUtils.isEmpty(pwd1)) {
            edit_password.requestFocus()
            ToastUtil.showToast(mActivity, "请输入新密码")
            return false;
        }
        var pwd2 = edit_password_repeat.text!!.trim()

        if (TextUtils.isEmpty(pwd2)) {
            ToastUtil.showToast(mActivity, "请再次输入新密码")
            edit_password_repeat.requestFocus()
            return false;
        }

        if (!pwd1.equals(pwd2)) {
            ToastUtil.showToast(mActivity, "两次输入的密码不一致")
            edit_password_repeat.requestFocus()
            return false;
        }


        return true;
    }


}
