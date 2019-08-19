package com.bochuan.pinke.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.bochuan.pinke.R
import com.gome.applibrary.activity.BaseActivity
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.core.model.UserInfo
import com.gome.work.core.persistence.UserCacheManager
import com.gome.work.core.utils.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_mine_info.*
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * 我的资料
 */
class MineInfoActivity : BaseGomeWorkActivity(), UserCacheManager.IUserGetResultListener {

    lateinit var mUserInfo: UserInfo

    override fun onResult(result: UserInfo) {
        updateUI(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_info)
        getCustomToolbar(title_bar).bindActivity(this, "个人资料")

        mUserInfo = SharedPreferencesHelper.getAccessTokenInfo().userInfo
        if (mUserInfo == null) {
            return
        }

        layout_avatar.setOnClickListener {
            var intent = Intent(mActivity, UserAvatarActivity::class.java)
            var accessToke = SharedPreferencesHelper.getAccessTokenInfo()
            intent.putExtra(BaseActivity.EXTRA_DATA, accessToke.userInfo)
            startActivity(intent)
        }

        layout_school.setOnClickListener {
            var intent = Intent(mActivity, SchoolChooseActivity::class.java)
            startActivityForResult(intent, 1)
        }
        UserCacheManager.get(this).addListener(this)

        getUserInfo(mUserInfo!!.id)

    }

    private fun updateUI(userInfo: UserInfo) {
        if (userInfo == null) {
            return
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            tv_school.text = data!!.getStringExtra(BaseActivity.EXTRA_DATA)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        UserCacheManager.get(this).removeListener(this)
    }

    private fun getUserInfo(userId: String) {
        UserCacheManager.get(this).getUserInfo(userId)
    }


}
