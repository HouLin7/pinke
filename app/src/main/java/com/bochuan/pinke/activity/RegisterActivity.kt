package com.bochuan.pinke.activity

import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import com.bochuan.pinke.R
import com.gome.utils.ToastUtil
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.common.utils.StringUtils
import com.gome.work.core.model.CaptchaItem
import com.gome.work.core.model.UserInfo
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*
import java.util.concurrent.TimeUnit

const val REFRESH_CAPTCHA_INTERVAL = 60

class RegisterActivity : BaseGomeWorkActivity() {

    var mTimer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView()
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

    private fun checkPasswordInput(): Boolean {

        if (TextUtils.isEmpty(edit_password.text.toString().trim())) {
            edit_password.requestFocus()
            ToastUtil.showToast(mActivity, "请输入密码")
            return false
        }

        if (TextUtils.isEmpty(edit_password.text.toString().trim())) {
            edit_password_confirm.requestFocus()
            ToastUtil.showToast(mActivity, "请输入确认密码")
            return false
        }

        var pwd1 = edit_password.text.toString().trim()
        var pwd2 = edit_password_confirm.text.toString().trim()

        if (!pwd1.equals(pwd2)) {
            edit_password_confirm.requestFocus()
            ToastUtil.showToast(mActivity, "两次输入密码不一致")
            return false
        }
        return true;
    }

    private fun initView() {
        button_register.setOnClickListener {
            if (!checkPhoneInput()) {
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(edit_captcha.text.toString().trim())) {
                edit_captcha.requestFocus()
                ToastUtil.showToast(mActivity, "请输入验证码")
                return@setOnClickListener
            }

            if (!checkPasswordInput()) {
                return@setOnClickListener
            }


            var account = edit_phone.text.toString().trim()
            var password = edit_password.text.toString().trim()
            var captcha = edit_captcha.text.toString().trim()

            showProgressDlg()
            WebApi.getInstance().register(account, password, captcha, object : IResponseListener<UserInfo> {
                override fun onError(code: String?, message: String?) {
                    ToastUtil.showToast(mActivity, message)
                    dismissProgressDlg()
                }

                override fun onSuccess(result: UserInfo?) {
                    dismissProgressDlg()
                    ToastUtil.showToast(mActivity, "注册成功")
                    finish()
                }
            })
        }

        button_captcha_get.setOnClickListener {
            button_captcha_get.isEnabled = false
            if (!checkPhoneInput()) {
                return@setOnClickListener
            }
            var phone = edit_phone.text.toString().trim()
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

    }

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

}
