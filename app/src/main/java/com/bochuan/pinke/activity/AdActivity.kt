package com.bochuan.pinke.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import com.bochuan.pinke.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.gome.utils.FileCacheUtils
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.common.webview.CommonWebActivity
import com.gome.work.core.Constants
import com.gome.work.core.model.AdBean
import com.gome.work.core.utils.GsonUtil
import com.gome.work.core.utils.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_ad.*
import java.io.File
import java.io.IOException


/**
 * 广告页面
 */
class AdActivity : BaseGomeWorkActivity() {

    private var mAdData: AdBean? = null


    private var mCountDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //            Transition fade = TransitionInflater.from(this).inflateTransition(R.transition.fade_in);
        //            getWindow().setEnterTransition(fade);
        //            getWindow().setExitTransition(fade);
        //        }
        setContentView(R.layout.activity_ad)


        ad_time.setOnClickListener { finishSelf() }

        ad_content.setOnClickListener {
            if (mAdData != null && !TextUtils.isEmpty(mAdData!!.linkUrl)) {
                val intent = Intent(baseContext, CommonWebActivity::class.java)
                intent.putExtra(CommonWebActivity.EXTRA_IS_ASSEMBLE_SIGN, false)
                intent.putExtra(CommonWebActivity.EXTRA_URL, mAdData!!.linkUrl)
                startActivity(intent)
                finishSelf()
            }
        }

        if (!isShowed) {
            initData()
            if (!TextUtils.isEmpty(mAdData!!.mediaType)) {
                showAdImage()
                startTimer(mAdData!!.stayDuration)
            } else {
                finishSelf()
            }
            isShowed = true
        } else {
            finishSelf()
        }
    }


    private fun finishSelf() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        } else {
            super.finish()
        }
    }


    override fun onBackPressed() {
        //屏蔽back键
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mCountDownTimer != null) {
            mCountDownTimer!!.cancel()
        }
    }

    private fun createFileIfNeed(): File? {
        val uri = Uri.parse(mAdData!!.linkUrl)
        val fileName = uri.lastPathSegment
        val fileFolder = File(externalCacheDir, "welcome")
        val file = File(fileFolder, fileName)
        if (!file.exists()) {
            try {
                file.mkdirs()
                file.createNewFile()
                return null
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return file
    }


    private fun downloadBitmap() {
        Glide.with(this).load<Any>(mAdData!!.linkUrl).asBitmap().toBytes().into(object : SimpleTarget<ByteArray>() {
            override fun onResourceReady(resource: ByteArray, glideAnimation: GlideAnimation<in ByteArray>) {
                val uri = Uri.parse(mAdData!!.linkUrl)
                FileCacheUtils.cache(baseContext, resource, uri.lastPathSegment)
            }
        })
    }


    private fun getTypeFromUrl(url: String): String {
        if (!TextUtils.isEmpty(url)) {
            try {
                val uri = Uri.parse(url)
                val fileName = uri.lastPathSegment
                val suffixName = fileName!!.substring(fileName.lastIndexOf(".") + 1, fileName.length).toLowerCase()
                if ("png" == suffixName || "jpg" == suffixName) {
                    return "image"
                }
                return if ("mp4" == suffixName) {
                    "video"
                } else suffixName
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return ""
    }

    private fun initData() {
        val jsonData = SharedPreferencesHelper.getString(Constants.PreferKeys.AD_DATA)
        if (!TextUtils.isEmpty(jsonData)) {
            val beanList = GsonUtil.jsonToList(jsonData, AdBean::class.java)
            if (beanList != null && beanList.isNotEmpty()) {
                mAdData = beanList.first()
            }
            if (mAdData!!.stayDuration == 0) {
                mAdData!!.stayDuration = 2
            }
            if (TextUtils.isEmpty(mAdData!!.mediaType)) {
                mAdData!!.mediaType = getTypeFromUrl(mAdData!!.image)
                if (TextUtils.isEmpty(mAdData!!.mediaType)) {
                    finish()
                    return
                }
            }
        }
    }

    private fun showAdImage() {
        when (mAdData!!.mediaType) {
            "gif", "image" -> Glide.with(this).load<Any>(mAdData!!.mediaType)
                .placeholder(R.mipmap.ic_launcher_round).into(ad_content)
            "video" -> {
            }
        }

    }

    private fun startTimer(duration: Int) {
        if (mCountDownTimer == null) {
            mCountDownTimer = object : CountDownTimer((duration * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    ad_time.setText("跳过(" + millisUntilFinished / 1000 + ")")
                }

                override fun onFinish() {
                    if (!isFinishing) {
                        finishSelf()
                    }
                }
            }
            mCountDownTimer!!.start()
        }
    }

    companion object {
        /**
         * 标记在程序一次生命周期内，是否已经展示过了
         */
        private var isShowed: Boolean = false
    }


}
