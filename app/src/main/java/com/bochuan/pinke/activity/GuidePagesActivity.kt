package com.bochuan.pinke.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.bochuan.pinke.R
import com.bochuan.pinke.databinding.ActivityGuidePagesBinding
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.core.Constants
import com.gome.work.core.utils.SharedPreferencesHelper

class GuidePagesActivity : BaseGomeWorkActivity() {

    private var binding: ActivityGuidePagesBinding? = null
    private var pageImages: List<Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guide_pages)
        initPage()
    }


    private fun initPage() {
        pageImages = listOf(R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.bg_login)
        binding!!.convenientBanner.setPages(object : CBViewHolderCreator {
            override fun createHolder(itemView: View): Holder<*> {
                return GuideHolderView(itemView)
            }

            override fun getLayoutId(): Int {
                return R.layout.guide_pages_item
            }

        }, pageImages)
            .setPageIndicator(intArrayOf(R.drawable.ic_page_indicator_false, R.drawable.ic_page_indicator_red))
            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
            .setOnItemClickListener { }.isCanLoop = false
        binding!!.convenientBanner.setFirstItemPos(0)

    }


    inner class GuideHolderView(itemView: View) : Holder<Int>(itemView) {

        private var imageView: ImageView? = null
        private var btnEnter: View? = null

        override fun initView(itemView: View) {
            imageView = itemView.findViewById(R.id.img)
            btnEnter = itemView.findViewById(R.id.btn_enter)
            btnEnter!!.setOnClickListener {
                SharedPreferencesHelper.commitBoolean(Constants.PreferKeys.IS_SHOW_GUIDE_PAGE, false)
                val intent: Intent
                if (isLogined) {
                    intent = Intent(baseContext, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                } else {
                    intent = Intent(baseContext, LoginActivity::class.java)
                }
                startActivity(intent)
                finish()
            }
        }

        override fun updateUI(data: Int?) {
            imageView!!.setImageResource(data!!)
            if (data != pageImages!!.last()) {
                btnEnter!!.visibility = View.GONE
            } else {
                btnEnter!!.visibility = View.VISIBLE

            }
        }
    }

}
