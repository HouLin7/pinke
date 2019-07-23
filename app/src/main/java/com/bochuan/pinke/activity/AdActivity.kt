package com.bochuan.pinke.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import com.bochuan.pinke.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.gome.utils.FileCacheUtils
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.common.webview.CommonWebActivity
import com.gome.work.core.Constants
import com.gome.work.core.model.AdBean
import com.gome.work.core.utils.SharedPreferencesHelper
import com.google.gson.Gson

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


        ad_time.setOnClickListener(View.OnClickListener { finishSelf() })

        mBinding.adContent.setOnClickListener(View.OnClickListener {
            if (mAdData != null && !TextUtils.isEmpty(mAdData!!.linkUrl)) {
                val intent = Intent(baseContext, CommonWebActivity::class.java)
                intent.putExtra(CommonWebActivity.EXTRA_IS_ASSEMBLE_SIGN, false)
                intent.putExtra(CommonWebActivity.EXTRA_URL, mAdData!!.linkUrl)
                startActivity(intent)
                finishSelf()
            }
        })

        if (!isShowed) {
            initData()
            if (!TextUtils.isEmpty(mAdData!!.type)) {
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
        val uri = Uri.parse(mAdData!!.downloadUrl)
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
        Glide.with(this).load<Any>(mAdData!!.downloadUrl).asBitmap().toBytes().into(object : SimpleTarget<ByteArray>() {
            override fun onResourceReady(resource: ByteArray, glideAnimation: GlideAnimation<in ByteArray>) {
                val uri = Uri.parse(mAdData!!.downloadUrl)
                FileCacheUtils.cache(baseContext, resource, uri.lastPathSegment)
            }
        })
    }


    private fun getTypeFromUrl(url: String): String {
        if (!TextUtils.isEmpty(url)) {
            try {
                val uri = Uri.parse(mAdData!!.downloadUrl)
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
            mAdData = Gson().fromJson(jsonData, AdBean::class.java)
            if (mAdData!!.stayDuration == 0) {
                mAdData!!.stayDuration = 3
            }
            if (TextUtils.isEmpty(mAdData!!.type)) {
                mAdData!!.type = getTypeFromUrl(mAdData!!.downloadUrl)
                if (TextUtils.isEmpty(mAdData!!.type)) {
                    finish()
                    return
                }
            }
        }
    }

    private fun showAdImage() {
        when (mAdData!!.type) {
            "gif", "image" -> Glide.with(this).load<Any>(mAdData!!.downloadUrl).placeholder(R.drawable.bg_splash).into(mBinding.adContent)
            "video" -> {
            }
        }

    }

    private fun startTimer(duration: Int) {
        if (mCountDownTimer == null) {
            mCountDownTimer = object : CountDownTimer((duration * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    mBinding.adTime.setText("跳过(" + millisUntilFinished / 1000 + ")")
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
