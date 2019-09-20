package com.bochuan.pinke

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.gome.work.core.model.*
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.bochuan.pinke", appContext.packageName)
    }

    @Test
    fun login() {
        WebApi.getInstance().login("13141403343", "123456", "PASSWORD", object : IResponseListener<AccessTokenInfo> {
            override fun onSuccess(result: AccessTokenInfo) {

                var result1 = result;
            }

            override fun onError(code: String?, message: String?) {

            }

        })
    }

    @Test
    fun getUserInfo() {
        WebApi.getInstance().getUserInfo("24", object : IResponseListener<UserInfo> {
            override fun onSuccess(result: UserInfo) {

                var result1 = result;
            }

            override fun onError(code: String?, message: String?) {

            }

        })
    }

    @Test
    fun getScheduleList() {
        WebApi.getInstance().getScheduleList(object : IResponseListener<String> {
            override fun onSuccess(result: String?) {

                var result1 = result;
            }

            override fun onError(code: String?, message: String?) {

            }

        })
    }

//    @Test
//    fun getCity() {
//        WebApi.getInstance().getCityData(object : IResponseListener<List<RegionItem>> {
//            override fun onSuccess(result: List<RegionItem>?) {
//
//                var result1 = result;
//            }
//
//            override fun onError(code: String?, message: String?) {
//
//            }
//
//        })
//    }
//
//    @Test
//    fun getCaptcha() {
//        WebApi.getInstance().getCaptcha("13141403343", object : IResponseListener<CaptchaItem> {
//            override fun onError(code: String?, message: String?) {
//
//            }
//
//            override fun onSuccess(result: CaptchaItem?) {
//
//            }
//
//        })
//    }
//
//
//    @Test
//    fun getDataDic() {
//        WebApi.getInstance().getConfigDataDic("all", object : IResponseListener<SysCfgData> {
//            override fun onError(code: String?, message: String?) {
//
//            }
//
//            override fun onSuccess(result: SysCfgData?) {
//
//            }
//
//        })
//    }
//
//    @Test
//    fun testAPi() {
//        WebApi.getInstance().getAd(null)
//
//        WebApi.getInstance().getBanner("1", object : IResponseListener<List<BannerBean>> {
//            override fun onError(code: String?, message: String?) {
//
//            }
//
//            override fun onSuccess(result: List<BannerBean>?) {
//
//            }
//
//        })
//
//        WebApi.getInstance().getRecommendList("1", "TEACHER", object : IResponseListener<String> {
//            override fun onError(code: String?, message: String?) {
//            }
//
//            override fun onSuccess(result: String?) {
//            }
//
//        })
//
//        WebApi.getInstance().getLauncherPic(object : IResponseListener<List<AdBean>> {
//            override fun onError(code: String?, message: String?) {
//            }
//
//            override fun onSuccess(result: List<AdBean>?) {
//            }
//
//        })
//    }

}
