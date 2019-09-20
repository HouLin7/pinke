package com.bochuan.pinke.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bochuan.pinke.R
import com.bochuan.pinke.activity.MineInfoActivity
import com.bochuan.pinke.activity.MyPostSearchPartnerListActivity
import com.bochuan.pinke.activity.SettingActivity
import com.bochuan.pinke.activity.UsersListActivity
import com.gome.work.common.imageloader.ImageLoader
import com.gome.work.core.event.model.EventInfo
import com.gome.work.core.model.UserInfo
import com.gome.work.core.persistence.DaoUtil
import com.gome.work.core.persistence.UserCacheManager
import com.gome.work.core.utils.SharedPreferencesHelper
import kotlinx.android.synthetic.main.fragment_mine.*
import org.greenrobot.eventbus.EventBus

class MineFragment : BaseFragment() {

    var daoUtils: DaoUtil? = null
    override fun getLayoutID(): Int {
        return R.layout.fragment_mine
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        daoUtils?.let {
            daoUtils = DaoUtil(mActivity)
            getUserInfo(mActivity!!)
            observeEvents(EventInfo.FLAG_LOGIN_USER_INFO_CHANGED)
        }
    }

    override fun handleEvent(event: EventInfo) {
        super.handleEvent(event)
        mActivity?.let {
            var userInfo = UserCacheManager.get(mActivity!!).getCacheUser(loginUserId)
            userInfo?.let {
                updateUI(userInfo)
            }
        }

    }

    private fun updateUI(user: UserInfo) {
        if (isAdded) {
            tv_nickname.text = user.nickname
            tv_username.text = user.username
            ImageLoader.loadImage(mActivity, user.avatar, iv_avatar)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var tokenInfo = SharedPreferencesHelper.getAccessTokenInfo()

        tv_nickname.text = tokenInfo.userInfo.nickname
        tv_username.text = tokenInfo.userInfo.username

        layout_setting.setOnClickListener {
            startActivity(Intent(mActivity!!, SettingActivity::class.java))
        }

        layout_user_info.setOnClickListener {
            var intent = Intent(mActivity, MineInfoActivity::class.java)
            startActivity(intent)
        }

        layout_my_friend.setOnClickListener {
            var intent = Intent(mActivity, UsersListActivity::class.java)
            intent.putExtra(EXTRA_DATA, UsersListActivity.RELATION_TYPE_FRIEND)
            startActivity(intent)
        }

        layout_my_partner.setOnClickListener {
            var intent = Intent(mActivity, UsersListActivity::class.java)
            intent.putExtra(EXTRA_DATA, UsersListActivity.RELATION_TYPE_PARTNER)
            startActivity(intent)
        }

        layout_my_post.setOnClickListener {
            var intent = Intent(mActivity, MyPostSearchPartnerListActivity::class.java)
            startActivity(intent)
        }

    }


    private fun getUserInfo(activity: Activity) {
        var userInfo = UserCacheManager.get(activity).getCacheUser(loginUserId)
        userInfo?.let {
            updateUI(userInfo)
        }
        UserCacheManager.get(activity).getLastUserInfo(loginUserId, object : UserCacheManager.IUserGetResultListener {
            override fun onResult(result: UserInfo) {
                updateUI(result)
            }

        })

    }


}
