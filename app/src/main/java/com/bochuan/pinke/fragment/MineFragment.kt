package com.bochuan.pinke.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bochuan.pinke.R
import com.bochuan.pinke.activity.MineInfoActivity
import com.bochuan.pinke.activity.MyPostSearchPartnerListActivity
import com.bochuan.pinke.activity.SettingActivity
import com.bochuan.pinke.activity.UsersListActivity
import com.gome.work.common.imageloader.ImageLoader
import com.gome.work.core.utils.SharedPreferencesHelper
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseFragment() {

    override fun getLayoutID(): Int {
        return R.layout.fragment_mine
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var tokenInfo = SharedPreferencesHelper.getAccessTokenInfo()
        ImageLoader.loadImage(context, tokenInfo.userInfo.avatar, iv_avatar)

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


    override fun refreshData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
