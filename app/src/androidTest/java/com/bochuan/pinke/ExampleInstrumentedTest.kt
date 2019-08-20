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
    fun getCity() {
        WebApi.getInstance().getCityData(object : IResponseListener<List<RegionItem>> {
            override fun onSuccess(result: List<RegionItem>?) {

                var result1 = result;
            }

            override fun onError(code: String?, message: String?) {

            }

        })
    }

    @Test
    fun getCaptcha() {
        WebApi.getInstance().getCaptcha("13141403343", object : IResponseListener<CaptchaItem> {
            override fun onError(code: String?, message: String?) {

            }

            override fun onSuccess(result: CaptchaItem?) {

            }

        })
    }


    @Test
    fun getDataDic() {
        WebApi.getInstance().getConfigDataDic("all", object : IResponseListener<List<CfgDicItem>> {
            override fun onError(code: String?, message: String?) {

            }

            override fun onSuccess(result: List<CfgDicItem>?) {

            }

        })
    }

    @Test
    fun testAPi() {
        WebApi.getInstance().getAd(null)

        WebApi.getInstance().getBanner("1", object : IResponseListener<List<BannerBean>> {
            override fun onError(code: String?, message: String?) {

            }

            override fun onSuccess(result: List<BannerBean>?) {

            }

        })

        WebApi.getInstance().getRecommendList("1", "TEACHER", object : IResponseListener<String> {
            override fun onError(code: String?, message: String?) {
            }

            override fun onSuccess(result: String?) {
            }

        })

        WebApi.getInstance().getLauncherPic(object : IResponseListener<List<AdBean>> {
            override fun onError(code: String?, message: String?) {
            }

            override fun onSuccess(result: List<AdBean>?) {
            }

        })
    }

}
