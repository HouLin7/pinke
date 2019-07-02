package com.bochuan.pinke.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.bochuan.pinke.R
import com.gome.utils.ToastUtil
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.core.Constants
import com.gome.work.core.model.AccessTokenBean
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import com.gome.work.core.utils.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseGomeWorkActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
        checkUpdate()
    }


    private fun initView() {
        val account = SharedPreferencesHelper.getString(Constants.PreferKeys.ACCOUNT)
        var viewAccount = ""
        if (!TextUtils.isEmpty(account)) {
            val charIndex = account.indexOf("@")
            viewAccount = account.substring(0, charIndex)
        }

    }

    fun login(view: View) {
        if (checkInput()) {

        }
    }


    private fun doLogin(account: String, password: String) {
        showProgressDlg()
        WebApi.getInstance().login(account, password,
            object : IResponseListener<AccessTokenBean> {
                override fun onError(code: String, message: String) {
                    dismissProgressDlg()
                    ToastUtil.showToast(baseContext, message)
                }

                override fun onSuccess(accessTokenBean: AccessTokenBean?) {
                    dismissProgressDlg()
                    if (accessTokenBean == null || !accessTokenBean.isValid) {
                        ToastUtil.showToast(baseContext, "用户信息不完整，登录失败！")
                    } else {
                        SharedPreferencesHelper.saveAccessTokenInfo(accessTokenBean)
                        SharedPreferencesHelper.commitString(Constants.PreferKeys.ACCOUNT, account)
                        SharedPreferencesHelper.commitString(Constants.PreferKeys.PASSWORD, password)
                        val intent = Intent(baseContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        ToastUtil.showToast(baseContext, "登录成功！")
                    }
                }
            })

    }

    fun doLogin() {
        showProgressDlg("正在登陆，请稍后...", false)
        //        doLogin(account, password);
    }

    private fun checkInput(): Boolean {
        var editMobile: String = edit_account.text.toString().trim();
        var editPassword: String = edit_account.text.toString().trim();
        if (TextUtils.isEmpty(editMobile)) {
            ToastUtil.showToast(this, "请输入账号")
            edit_account.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(editPassword)) {
            ToastUtil.showToast(this, "请输入密码")
            edit_password.requestFocus()
            return false
        }

        return true
    }


}
