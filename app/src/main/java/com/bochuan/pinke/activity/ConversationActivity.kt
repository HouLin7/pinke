package com.bochuan.pinke.activity

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.util.SparseArray
import android.view.ViewGroup
import android.widget.Toast
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.ashokvarma.bottomnavigation.TextBadgeItem
import com.baidu.location.BDLocation
import com.bochuan.pinke.R
import com.bochuan.pinke.fragment.*
import com.bochuan.pinke.util.BCLocationManager
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.core.model.RegionItem
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


open class ConversationActivity : BaseGomeWorkActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

    }

}
