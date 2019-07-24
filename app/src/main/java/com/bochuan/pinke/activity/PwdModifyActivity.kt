package com.bochuan.pinke.activity

import android.os.Bundle
import android.text.TextUtils
import com.bochuan.pinke.R
import com.gome.utils.ToastUtil
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import kotlinx.android.synthetic.main.activity_pwd_modify.*
import kotlinx.android.synthetic.main.activity_setting.title_bar


class PwdModifyActivity : BaseGomeWorkActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pwd_modify)
        getCustomToolbar(title_bar).bindActivity(this, "修改密码")

        button_confirm.setOnClickListener {
            if (checkInput()) {
                doModify()
            }
        }
    }

    private fun doModify() {
        var originalPwd = edit_password_original.text!!.trim().toString()
        var newPwd = edit_password_new.text!!.trim().toString()
        WebApi.getInstance().modifyPassword(originalPwd, newPwd, object : IResponseListener<String> {
            override fun onError(code: String?, message: String?) {

            }

            override fun onSuccess(result: String?) {

            }

        })
    }


    private fun checkInput(): Boolean {
        var captcha = edit_captcha.text!!.trim()

        if (TextUtils.isEmpty(captcha)) {
            edit_captcha.requestFocus()
            ToastUtil.showToast(mActivity, "请输入验证码")
            return false;
        }


        var pwd1 = edit_password_original.text!!.trim()
        if (TextUtils.isEmpty(pwd1)) {
            edit_password_original.requestFocus()
            ToastUtil.showToast(mActivity, "请输入原始密码")
            return false;
        }
        var pwd2 = edit_password_new.text!!.trim()

        if (TextUtils.isEmpty(pwd2)) {
            ToastUtil.showToast(mActivity, "请输入新密码")
            edit_password_new.requestFocus()
            return false;
        }

        return true

    }


}
