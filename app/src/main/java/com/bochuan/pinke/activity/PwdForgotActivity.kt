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
import kotlinx.android.synthetic.main.activity_pwd_forgot.*
import kotlinx.android.synthetic.main.activity_pwd_forgot.button_captcha_get
import kotlinx.android.synthetic.main.activity_pwd_forgot.edit_captcha
import kotlinx.android.synthetic.main.activity_pwd_forgot.edit_password
import kotlinx.android.synthetic.main.activity_pwd_forgot.edit_phone
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_setting.title_bar
import java.util.*
import java.util.concurrent.TimeUnit


class PwdForgotActivity : BaseGomeWorkActivity() {


    var mTimer = Timer()

    inner class MyTimeTask(var startTime: Long) : TimerTask() {

        override fun run() {
            var sec = TimeUnit.MILLISECONDS.toSeconds(SystemClock.elapsedRealtime() - startTime)
            if (sec >= REFRESH_CAPTCHA_INTERVAL) {
                cancel()
                runOnUiThread {
                    button_captcha_get.isEnabled = true
                    button_captcha_get.setText("获取")
                }
                return

            }
            var viewSec = REFRESH_CAPTCHA_INTERVAL - sec;
            mActivity.runOnUiThread { button_captcha_get.setText(viewSec.toString()) }
        }
    }


    private fun checkPhoneInput(): Boolean {
        var phone = edit_phone.text.toString().trim()
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast(mActivity, "请输入手机号")
            return false
        }

        if (!StringUtils.checkPhone(phone)) {
            ToastUtil.showToast(mActivity, "手机号格式有误")
            return false
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pwd_forgot)
        getCustomToolbar(title_bar).bindActivity(this, "忘记密码")

        button_captcha_get.setOnClickListener {
            if (!checkPhoneInput()) {
                return@setOnClickListener
            }
            var phone = edit_phone.text!!.toString().trim()
            button_captcha_get.isEnabled = false
            WebApi.getInstance().getCaptcha(phone, object : IResponseListener<CaptchaItem> {
                override fun onError(code: String?, message: String?) {
                    ToastUtil.showToast(mActivity, message)
                    button_captcha_get.isEnabled = true
                }

                override fun onSuccess(result: CaptchaItem?) {
                    mTimer.scheduleAtFixedRate(MyTimeTask(SystemClock.elapsedRealtime()), 0, 100)

                    result?.let {
                        edit_captcha.setText(result.code)
                    }
                }

            })

        }
        button_confirm.setOnClickListener {
            if (checkInput()) {
                doForget()
            }
        }
    }

    private fun doForget() {
        var phone = edit_phone.text!!.trim().toString()
        var captcha = edit_captcha.text!!.trim().toString()
        var password = edit_password.text!!.trim().toString()
        showProgressDlg()
        WebApi.getInstance().forgetPassword(phone, password, captcha, object : IResponseListener<String> {
            override fun onError(code: String?, message: String?) {
                ToastUtil.showToast(mActivity, message)
            }

            override fun onSuccess(result: String?) {
                ToastUtil.showToast(mActivity, "重置密码成功")
                finish()
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

        var pwd1 = edit_password.text!!.toString().trim()
        if (TextUtils.isEmpty(pwd1)) {
            edit_password.requestFocus()
            ToastUtil.showToast(mActivity, "请输入新密码")
            return false;
        }
        var pwd2 = edit_password_repeat.text!!.toString().trim()

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
