package com.bochuan.pinke.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.View
import com.bochuan.pinke.R
import com.bochuan.pinke.fragment.BaseFragment
import com.gome.utils.ToastUtil
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.core.Constants
import com.gome.work.core.model.AccessTokenInfo
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import com.gome.work.core.utils.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_login_captcha.*
import kotlinx.android.synthetic.main.fragment_login_pwd.*

class LoginActivity : BaseGomeWorkActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
        checkUpdate()
    }

    private fun initView() {
        val adapter = Adapter(supportFragmentManager)
        view_pager.adapter = adapter
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                if (p0 == 0) {
                    tv_forget_pwd.visibility = View.VISIBLE
                } else {
                    tv_forget_pwd.visibility = View.GONE
                }
            }

        })

        tab_layout.setupWithViewPager(view_pager)
        button_login.setOnClickListener {
            var fragment = adapter.getItem(view_pager.currentItem)
            fragment?.let {
                if (it.checkInput()) {
                    it.doLogin()
                }
            }

        }

        tv_register.setOnClickListener {
            var intent = Intent(baseContext, RegisterActivity::class.java)
            startActivity(intent)
        }
        tv_forget_pwd.setOnClickListener {

        }

    }


    inner class Adapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private var fragments = HashMap<Int, LoginFragment>()

        override fun getItem(p0: Int): LoginFragment? {
            if (p0 == 0) {
                if (fragments[p0] == null) {
                    fragments.put(p0, LoginByPwdFragment())
                }
            } else {
                if (fragments[p0] == null) {
                    fragments.put(p0, LoginByCaptchaFragment())
                }
            }

            return fragments[p0]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            if (position == 0) return "密码登录" else return "短信登录"
        }

        override fun getCount(): Int = 2

    }

    abstract class LoginFragment : BaseFragment() {

        abstract fun doLogin()

        abstract fun checkInput(): Boolean


    }

    @SuppressLint("ValidFragment")
    class LoginByPwdFragment : LoginFragment() {


        override fun doLogin() {
            showProgressDlg()
            var account = edit_account.text.toString()
            var password = edit_password.text.toString()
            WebApi.getInstance().login(account, password, "PASSWORD",
                object : IResponseListener<AccessTokenInfo> {
                    override fun onError(code: String, message: String) {
                        dismissProgressDlg()
                        ToastUtil.showToast(activity, message)
                    }

                    override fun onSuccess(accessTokenInfo: AccessTokenInfo?) {
                        dismissProgressDlg()
                        if (accessTokenInfo == null || !accessTokenInfo.isValid) {
                            ToastUtil.showToast(activity, "用户信息不完整，登录失败！")
                        } else {
                            SharedPreferencesHelper.saveAccessTokenInfo(accessTokenInfo)
                            SharedPreferencesHelper.commitString(Constants.PreferKeys.ACCOUNT, account)
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                            activity!!.finish()
                            ToastUtil.showToast(activity, "登录成功！")
                        }
                    }
                })
        }

        override fun getLayoutID(): Int {
            return R.layout.fragment_login_pwd
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
        }


        override fun refreshData() {

        }

        override fun checkInput(): Boolean {
            var editMobile: String = edit_account.text.toString().trim();
            var editPassword: String = edit_account.text.toString().trim();
            if (TextUtils.isEmpty(editMobile)) {
                ToastUtil.showToast(activity, "请输入账号")
                edit_account.requestFocus()
                return false
            }

            if (TextUtils.isEmpty(editPassword)) {
                ToastUtil.showToast(activity, "请输入密码")
                edit_password.requestFocus()
                return false
            }

            return true
        }


    }


    @SuppressLint("ValidFragment")
    class LoginByCaptchaFragment : LoginFragment() {
        override fun doLogin() {
            showProgressDlg()
            var account = edit_phone.text.toString()
            var password = edit_captcha.text.toString()
            WebApi.getInstance().login(account, password, "",
                object : IResponseListener<AccessTokenInfo> {
                    override fun onError(code: String, message: String) {
                        dismissProgressDlg()
                        ToastUtil.showToast(activity, message)
                    }

                    override fun onSuccess(accessTokenInfo: AccessTokenInfo?) {
                        dismissProgressDlg()
                        if (accessTokenInfo == null || !accessTokenInfo.isValid) {
                            ToastUtil.showToast(activity, "用户信息不完整，登录失败！")
                        } else {
                            SharedPreferencesHelper.saveAccessTokenInfo(accessTokenInfo)
                            SharedPreferencesHelper.commitString(Constants.PreferKeys.ACCOUNT, account)
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                            activity!!.finish()
                            ToastUtil.showToast(activity, "登录成功！")
                        }
                    }
                })

        }

        override fun getLayoutID(): Int {
            return R.layout.fragment_login_captcha
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            button_captcha.setOnClickListener {}
        }

        override fun refreshData() {

        }

        override fun checkInput(): Boolean {
            var editMobile: String = edit_phone.text.toString().trim();
            var editPassword: String = edit_captcha.text.toString().trim();
            if (TextUtils.isEmpty(editMobile)) {
                ToastUtil.showToast(activity, "请输入手机号")
                edit_account.requestFocus()
                return false
            }

            if (TextUtils.isEmpty(editPassword)) {
                ToastUtil.showToast(activity, "请输入验证码")
                edit_password.requestFocus()
                return false
            }

            return true
        }
    }
}
