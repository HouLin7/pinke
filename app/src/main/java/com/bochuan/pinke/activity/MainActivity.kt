package com.bochuan.pinke.activity

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.text.TextUtils
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
import com.gome.work.core.Constants
import com.gome.work.core.event.EventDispatcher
import com.gome.work.core.event.model.EventInfo
import com.gome.work.core.model.AdBean
import com.gome.work.core.utils.GsonUtil
import com.gome.work.core.utils.SharedPreferencesHelper
import com.google.gson.reflect.TypeToken
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


open class MainActivity : BaseGomeWorkActivity(), ViewPager.OnPageChangeListener {


    override fun onPageScrollStateChanged(p0: Int) {

    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

    }

    override fun onPageSelected(position: Int) {
        bottom_navigation_bar_container.selectTab(position)
    }

    private val APP_BACK_WAIT_DEFAULT_TIME: Long = 3 * 1000

    private var mOldBackPressTime: Long = 0

    private val mTextBadgeItemList = ArrayList<TextBadgeItem>()
    private var myFragmentTabAdapter: MyFragmentTabAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myFragmentTabAdapter = MyFragmentTabAdapter(supportFragmentManager)
        view_pager.adapter = myFragmentTabAdapter
        view_pager.addOnPageChangeListener(this)

        initTextBadgeItems(myFragmentTabAdapter!!.getCount())
        initNavigationBar(bottom_navigation_bar_container)
//        goLoginActivity()
        toAdActivity()
        getLocation()

    }

    private fun getLocation() {
        val locManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showAlertDlg("请先打开手机定位开关")
        }
        requestPermission(
            ACCESS_FINE_LOCATION
        ) { permission, isSuccess ->
            if (isSuccess) {
                val locationManager = BCLocationManager(mActivity);
                locationManager.getLocation(object : BCLocationManager.ILocationCallback {
                    override fun call(loc: BDLocation) {
                        EventDispatcher.postEvent(EventInfo.FLAG_LOCATION_RECEIVE, loc)
                    }
                })
            } else {

            }

        }
    }

    /**
     * 启动广告页
     */
    private fun toAdActivity() {
        val jsonData = SharedPreferencesHelper.getString(Constants.PreferKeys.AD_DATA)
        if (!TextUtils.isEmpty(jsonData)) {
//            var typeToken = object : TypeToken<List<AdBean>>() {}
            val beanList = GsonUtil.jsonToList(jsonData, AdBean::class.java)
            if (beanList != null && beanList.isNotEmpty()) {
                val intent = Intent(this, AdActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!isLogined) {
            finish()
            var intent = Intent(mActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onBackPressed() {
        val index = view_pager.currentItem
        val fragment = myFragmentTabAdapter!!.getItem(index)
        if (fragment!!.onBackPressed()) {
            return;
        }

        val diff: Long = System.currentTimeMillis() - mOldBackPressTime
        if (diff > APP_BACK_WAIT_DEFAULT_TIME) {
            mOldBackPressTime = System.currentTimeMillis()
            Toast.makeText(this, getString(R.string.application_exist), Toast.LENGTH_SHORT).show()
        } else {
            mOldBackPressTime = 0
            super.onBackPressed()
        }
    }


    private fun generateBadgeItem(): TextBadgeItem {
        val numberBadgeItem = TextBadgeItem()
        numberBadgeItem.hide(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            numberBadgeItem.setBackgroundColor(getColor(R.color.red))
        } else {
            numberBadgeItem.setBackgroundColor(resources.getColor(R.color.red))
        }
        return numberBadgeItem
    }

    private fun initTextBadgeItems(count: Int) {
        for (i in 0 until count) {
            val numberBadgeItem = generateBadgeItem()
            mTextBadgeItemList.add(numberBadgeItem)
        }
    }

    private fun initNavigationBar(naviBar: BottomNavigationBar) {
        naviBar.setMode(BottomNavigationBar.MODE_FIXED)
            .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)

        naviBar.addItem(
            BottomNavigationItem(R.mipmap.ic_launcher, "首页")
                .setInactiveIconResource(R.mipmap.ic_launcher).setBadgeItem(mTextBadgeItemList.get(0))
        )
//        naviBar.addItem(
//            BottomNavigationItem(R.mipmap.ic_launcher, "消息")
//                .setInactiveIconResource(R.mipmap.ic_launcher).setBadgeItem(mTextBadgeItemList.get(1))
//        )

        naviBar.addItem(
            BottomNavigationItem(R.mipmap.ic_launcher, "课程")
                .setInactiveIconResource(R.mipmap.ic_launcher).setBadgeItem(mTextBadgeItemList.get(2))
        )

        naviBar.addItem(
            BottomNavigationItem(R.mipmap.ic_launcher, "课表")
                .setInactiveIconResource(R.mipmap.ic_launcher).setBadgeItem(mTextBadgeItemList.get(3))
        )

        naviBar.addItem(
            BottomNavigationItem(R.mipmap.ic_launcher, "我")
                .setInactiveIconResource(R.mipmap.ic_launcher).setBadgeItem(mTextBadgeItemList.get(3))
        )



        naviBar.setFirstSelectedPosition(0).initialise()
        naviBar.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {
                view_pager.setCurrentItem(position, false)
            }

            override fun onTabUnselected(position: Int) {

            }

            override fun onTabReselected(position: Int) {
                view_pager.currentItem
                if (position == 0) {
                    val fragment = myFragmentTabAdapter?.getItem(0) as BaseFragment
                    fragment.refreshData()
                }
            }
        })

    }

    fun testShare() {
        ShareAction(this).withText("hello").withSubject("subject")
            .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
            .setCallback(object : UMShareListener {
                override fun onCancel(p0: SHARE_MEDIA?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResult(p0: SHARE_MEDIA?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onStart(p0: SHARE_MEDIA?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            }).open();
    }

    fun goLoginActivity() {
        var intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }

    /**
     * 初始化Fragment
     */
    internal class MyFragmentTabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        var mFragmentList = SparseArray<BaseFragment>()

        override fun getItem(position: Int): BaseFragment? {
            var fragment: BaseFragment? = mFragmentList[position]
            if (fragment == null) {
                when (position) {
                    0 -> fragment = HomeFragment()
                    1 -> fragment = MyCourseFragment()
                    2 -> fragment = CourseScheduleFragment()
                    3 -> fragment = MineFragment()
                }
                mFragmentList.put(position, fragment)
            }

            return fragment

        }


        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)
        }

        override fun getCount(): Int {
            return 4
        }
    }


}
