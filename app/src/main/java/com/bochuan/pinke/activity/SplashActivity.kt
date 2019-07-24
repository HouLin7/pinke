package com.bochuan.pinke.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.format.DateUtils
import android.view.View
import com.bochuan.pinke.R
import com.gome.utils.CommonUtils
import com.gome.utils.GsonUtil
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.common.utils.AppUtils
import com.gome.work.core.Constants
import com.gome.work.core.SystemFramework
import com.gome.work.core.model.AdBean
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import com.gome.work.core.utils.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class SplashActivity : BaseGomeWorkActivity() {

    private val permissionResult = HashMap<String, Boolean>()

    private val isLogin: Boolean
        get() = SharedPreferencesHelper.isLogin()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (SystemFramework.getInstance().environment != SystemFramework.Environment.RELEASE) {
            tv_splash_ver_name.visibility = View.VISIBLE
            tv_splash_ver_name.text = AppUtils.getVersionName()
        }
        getAdData()
        checkPermission()
    }


    private fun checkPermission() {
        requestPermission(
            arrayOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION


            )
        ) { permission, isSuccess ->
            permissionResult[permission] = isSuccess
            if (permissionResult.containsKey(Manifest.permission.READ_PHONE_STATE)
                && permissionResult.containsKey(Manifest.permission.READ_EXTERNAL_STORAGE)
                && permissionResult.contains(Manifest.permission.ACCESS_COARSE_LOCATION)
                && permissionResult.contains(Manifest.permission.ACCESS_FINE_LOCATION)
            ) {
                if (permissionResult[Manifest.permission.READ_PHONE_STATE]!! && permissionResult[Manifest.permission.READ_PHONE_STATE]!!) {
                    showSplash()
                } else {
                    AlertDialog.Builder(this@SplashActivity).setMessage("获取基本权限失败，程序无法运行")
                        .setCancelable(false)
                        .setPositiveButton(R.string.confirm) { dialog, which -> finish() }.show()

                }
            }
        }
    }

    /**
     * 获取广告
     */
    private fun getAdData() {
        WebApi.getInstance().getLauncherPic(object : IResponseListener<List<AdBean>> {
            override fun onError(code: String, message: String) {
//                dismissProgressDlg()
            }

            override fun onSuccess(result: List<AdBean>?) {
                if (result != null) {
                    val resultStr = GsonUtil.objectToJson(result)
                    SharedPreferencesHelper.commitString(Constants.PreferKeys.AD_DATA, resultStr)
                }
            }
        })
    }

    /**
     * 版本升级时调用，只执行一次
     */
    private fun appUpgrade(oldVerCode: Int, newVerCode: Int) {
        if (oldVerCode < 45) {
            SharedPreferencesHelper.commitString(Constants.PreferKeys.CHANNEL_LIST, "")
        }
        SharedPreferencesHelper.commitBoolean(Constants.PreferKeys.IS_SHOW_GUIDE_PAGE, true)
    }

    private fun showSplash() {
        val mHandler = Handler()
        mHandler.postDelayed({
            val lastVerCode = SharedPreferencesHelper.getInt(Constants.PreferKeys.LAST_VERSION_CODE)
            val newVerCode = CommonUtils.getVersionCode(baseContext)
            if (lastVerCode == newVerCode) {
                if (isLogin) {
                    val intent = Intent(baseContext, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    //                        intent.putExtra(PushHelper.PUSH_EXTRA,imPushExtra);
                    startActivity(intent)
                } else {
                    startActivity(Intent(baseContext, LoginActivity::class.java))
                }
            } else {
                appUpgrade(lastVerCode, newVerCode)
                SharedPreferencesHelper.commitInt(Constants.PreferKeys.LAST_VERSION_CODE, newVerCode)
            }

            val isShowGuide = SharedPreferencesHelper.getBoolean(Constants.PreferKeys.IS_SHOW_GUIDE_PAGE)
            if (isShowGuide) {
                val intent = Intent(baseContext, GuidePagesActivity::class.java)
                //                    intent.putExtra(PushHelper.PUSH_EXTRA,imPushExtra);
                startActivity(intent)
            }
            finish()
        }, SPLASH_TIME_INTERVAL)
    }

    companion object {

        private val SPLASH_TIME_INTERVAL = DateUtils.SECOND_IN_MILLIS / 2
    }

}
