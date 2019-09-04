package com.bochuan.pinke.activity

import android.os.Bundle
import android.text.TextUtils
import com.bochuan.pinke.R
import com.gome.utils.ToastUtil
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.common.imageloader.ImageLoader
import com.gome.work.core.model.SearchPartnerItem
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import kotlinx.android.synthetic.main.activity_search_partner_detail.*

/**
 *  找伴读详情页
 */
class SearchPartnerDetailActivity : BaseGomeWorkActivity() {

    private var mPartnerItem: SearchPartnerItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_partner_detail)
        getCustomToolbar(titleBar).bindActivity(this, "伴读详情")
        mPartnerItem = intent.getSerializableExtra(EXTRA_DATA) as SearchPartnerItem
        initView()
    }


    override fun onBackPressed() {
        dismissInputMethod()
        super.onBackPressed()
    }

    private fun initView() {
        tv_user_name.text = mPartnerItem?.userInfo?.nickname
        tv_sex.text = mPartnerItem?.userInfo?.sexName
        tv_school.text = mPartnerItem?.school
        tv_sex.text = mPartnerItem?.sex
        tv_class_type.text = mPartnerItem?.classType
        ImageLoader.loadImage(mActivity, mPartnerItem?.userInfo?.avatar, iv_avatar)


        tvFollow.setOnClickListener {
            var userId = mPartnerItem?.userInfo?.id
            if (!TextUtils.isEmpty(userId)) {
                WebApi.getInstance().follow(userId, object : IResponseListener<String> {
                    override fun onError(code: String?, message: String?) {
                        ToastUtil.showToast(mActivity, message)
                    }

                    override fun onSuccess(result: String?) {
                        ToastUtil.showToast(mActivity, "关注成功")
                    }

                })
            }
        }

        btnBePartner.setOnClickListener {
            var userId = mPartnerItem?.userInfo?.id
            if (!TextUtils.isEmpty(userId)) {
                WebApi.getInstance().partner(userId, object : IResponseListener<String> {
                    override fun onError(code: String?, message: String?) {
                        ToastUtil.showToast(mActivity, message)
                    }

                    override fun onSuccess(result: String?) {
                        ToastUtil.showToast(mActivity, "已成为伴读")
                    }

                })
            }


        }
    }


}
