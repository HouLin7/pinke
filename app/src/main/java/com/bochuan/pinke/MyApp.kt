package com.bochuan.pinke

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.gome.work.common.imageloader.ImageLoader
import com.gome.work.core.Constants
import com.gome.work.core.SystemFramework
import com.gome.work.core.model.CfgDicItem
import com.gome.work.core.model.RegionItem
import com.gome.work.core.model.SysCfgData
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import com.gome.work.core.utils.GsonUtil
import com.gome.work.core.utils.SharedPreferencesHelper
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions
import com.hyphenate.easeui.EaseUI
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.umeng.commonsdk.UMConfigure

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initSDK()
        SystemFramework.getInstance().init(this, SystemFramework.Environment.valueOf(BuildConfig.SERVER_FLAG))
        intViewConfig()
        synBaseData()
    }

    private fun intViewConfig() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.bg_gray, R.color.text_gray)//全局设置主题颜色
            MaterialHeader(context)//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.bg_gray, R.color.text_gray)//全局设置主题颜色
            ClassicsFooter(context).setDrawableSize(20f)
        }
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


    private fun initSDK() {
        Logger.addLogAdapter(AndroidLogAdapter())
        // 初始化环信SDK
        val emOptions = EMOptions()
        emOptions.autoLogin = true
        emOptions.appKey = "zxad#mydemo"
        emOptions.setAutoDownloadThumbnail(true)
        EaseUI.getInstance().init(this, emOptions)

        UMConfigure.init(this, "5d10a1d04ca357fa8f000377", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "")

        //        JPushInterface.init(this)
        //        JPushInterface.setDebugMode(BuildConfig.DEBUG);

        ImageLoader.init(this, R.mipmap.ic_launcher, R.drawable.img_default)

        val environment = SystemFramework.getInstance().environment

        //        EMClient.getInstance().chatManager().loadAllConversations();
        //        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
        //            @Override
        //            public void onMessageReceived(List<EMMessage> list) {
        //
        //            }
        //
        //            @Override
        //            public void onCmdMessageReceived(List<EMMessage> list) {
        //
        //            }
        //
        //            @Override
        //            public void onMessageRead(List<EMMessage> list) {
        //
        //            }
        //
        //            @Override
        //            public void onMessageDelivered(List<EMMessage> list) {
        //
        //            }
        //
        //            @Override
        //            public void onMessageRecalled(List<EMMessage> list) {
        //
        //            }
        //
        //            @Override
        //            public void onMessageChanged(EMMessage emMessage, Object o) {
        //
        //            }
        //        });


    }

    /**
     * 同步基础数据
     */
    private fun synBaseData() {
        getCityData()
        getSysConfigData()
    }

    private fun getCityData() {
        WebApi.getInstance().getCityData(object : IResponseListener<List<RegionItem>> {
            override fun onSuccess(result: List<RegionItem>?) {
                var data = GsonUtil.GsonString(result)
                SharedPreferencesHelper.commitString(Constants.PreferKeys.CITY_DATA, data)
            }

            override fun onError(code: String?, message: String?) {

            }

        })
    }


    private fun getSysConfigData() {

        WebApi.getInstance().getConfigDataDic("all", object : IResponseListener<SysCfgData> {
            override fun onSuccess(result: SysCfgData?) {
                result?.let {
                    var data = GsonUtil.GsonString(result)
                    SharedPreferencesHelper.commitString(Constants.PreferKeys.SYS_CONFIG_DATA, data)
                }
            }

            override fun onError(code: String?, message: String?) {

            }

        })


    }


}
