package com.bochuan.pinke.fragment

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.amap.api.location.AMapLocation
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.bochuan.pinke.R
import com.bochuan.pinke.activity.*
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.gome.utils.GsonUtil
import com.gome.utils.ToastUtil
import com.gome.work.common.KotlinViewHolder
import com.gome.work.common.adapter.BaseRecyclerAdapter
import com.gome.work.common.adapter.BaseViewHolder
import com.gome.work.common.imageloader.ImageLoader
import com.gome.work.common.utils.BlurUtils
import com.gome.work.core.Constants
import com.gome.work.core.event.model.EventInfo
import com.gome.work.core.model.AdBean
import com.gome.work.core.model.BannerBean
import com.gome.work.core.model.OrganizationItem
import com.gome.work.core.model.UserInfo
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import com.gome.work.core.utils.SharedPreferencesHelper
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.adapter_recommend_organization_list_item.*
import kotlinx.android.synthetic.main.adapter_recommend_user_list_item.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    companion object {
        const val REQUEST_CODE_CITY_SELECT = 1
    }

    private val mBannerList = ArrayList<BannerBean>();

    private var mBestTeacherList = ArrayList<UserInfo>()

    private var mBestOrganizationList = ArrayList<OrganizationItem>()

    private var mAdapterOrganization: AdapterOrganization? = null

    private var mAdapterTeacher: AdapterTeacher? = null;

    private var mBDLocaiton: AMapLocation? = null

    private var bitmap: Bitmap? = null


    init {
        createTestData();
        initBannerData()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeEvents(EventInfo.FLAG_LOCATION_RECEIVE)
    }

    override fun handleEvent(event: EventInfo?) {
        super.handleEvent(event)
        var location = event!!.data as AMapLocation
        if (isAdded) {
            tv_address.text = location.aoiName
            tv_city.text = location.city
        } else {
            mBDLocaiton = location
        }

    }


    override fun getLayoutID(): Int = R.layout.fragment_home

    override fun refreshData() {

    }


    private fun createTestData() {
        var teacher = UserInfo();
        teacher.avatar = "http://b-ssl.duitang.com/uploads/item/201711/09/20171109200813_2vtKE.jpeg";
        teacher.nickname = "推荐老师"
        mBestTeacherList.add(teacher)
        mBestTeacherList.add(teacher)
        mBestTeacherList.add(teacher)
        mBestTeacherList.add(teacher)

        var organize = OrganizationItem();
        organize.avatar = "http://b-ssl.duitang.com/uploads/item/201711/09/20171109200813_2vtKE.jpeg";
        organize.name = "推荐机构"
        mBestOrganizationList.add(organize)
        mBestOrganizationList.add(organize)
        mBestOrganizationList.add(organize)
        mBestOrganizationList.add(organize)
    }


    private fun initView() {
        tv_search_partner.setOnClickListener {
            var intent = Intent(mActivity, ChannelPartnerActivity::class.java);
            startActivity(intent)
        }


        edit_search.setOnClickListener {
            var intent = Intent(mActivity, SearchActivity::class.java);
            startActivity(intent)

        }
        layout_bg.visibility = View.VISIBLE
        tv_city.text = ""
        tv_address.text = ""
        tv_address.setOnClickListener {
            var intent = Intent(mActivity, AddressEditActivity::class.java)
            startActivity(intent)
        }

        iv_ad_1.setImageResource(R.mipmap.ic_launcher)
        iv_ad_2.setImageResource(R.mipmap.ic_launcher)
        iv_ad_3.setImageResource(R.mipmap.ic_launcher)
        iv_ad_4.setImageResource(R.mipmap.ic_launcher)

        recyclerView_near_best_organize.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
        recyclerView_near_best_teacher.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
        mAdapterOrganization = AdapterOrganization(activity);
        mAdapterOrganization?.setItemList(mBestOrganizationList)

        mAdapterTeacher = AdapterTeacher(activity)
        mAdapterTeacher?.setItemList(mBestTeacherList)

        recyclerView_near_best_organize.adapter = mAdapterOrganization;
        recyclerView_near_best_teacher.adapter = mAdapterTeacher;


        var conven: ConvenientBanner<BannerBean> = convenientBanner!! as ConvenientBanner<BannerBean>
        conven.setPages(object : CBViewHolderCreator {
            override fun createHolder(itemView: View): Holder<*> {
                return BannerHolder(itemView)
            }

            override fun getLayoutId(): Int {
                return R.layout.news_banner_item
            }

        }, mBannerList)
            .setPageIndicator(intArrayOf(R.drawable.ic_page_indicator_false, R.drawable.ic_page_indicator_white))
            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
            .setOnItemClickListener({ position ->


            })
        floating_action_menu.setOnFloatingActionsMenuUpdateListener(object :
            FloatingActionsMenu.OnFloatingActionsMenuUpdateListener {
            override fun onMenuCollapsed() {
                layout_bg.isClickable = false;
                dimBackground(1f, 0f)
                bitmap?.recycle()
                bitmap = null
            }


            override fun onMenuExpanded() {
                layout_bg.isClickable = true;
                if (null == bitmap) {
                    bitmap = BlurUtils.takeScreenShot(activity!!)
                }
                BlurUtils.blur(activity!!, bitmap, layout_bg)
                dimBackground(0f, 1f)

            }

        })

        view_pager.adapter = MyFragmentTabAdapter(childFragmentManager)
        tab_layout.setupWithViewPager(view_pager);

        fab_search_classmate.setOnClickListener {
            floating_action_menu.collapse()
            var intent = Intent(mActivity, PostSearchPartnerActivity::class.java)
            startActivity(intent)
        }
        fab_search_teacher.setOnClickListener {
            floating_action_menu.collapse()
            var intent = Intent(mActivity, PostSearchPartnerActivity::class.java)
            intent.putExtra(PostSearchPartnerActivity.EXTRA_MODEL, PostSearchPartnerActivity.TO_SEARCH_TEACHER)
            startActivity(intent)
        }

        layout_bg.setOnClickListener {
            floating_action_menu.collapse()
        }
        layout_bg.isClickable = false;

        mBDLocaiton?.let {
            tv_address.text = mBDLocaiton!!.aoiName
            tv_city.text = mBDLocaiton!!.city
        }


        iv_message.setOnClickListener { startActivity(Intent(mActivity, ConversationActivity::class.java)) }


        tv_city.setOnClickListener {
            var intent = Intent(mActivity, CitySelectActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_CITY_SELECT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CITY_SELECT) {
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getBanner()
        getAdList()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    private fun initBannerData() {
        var cacheData = SharedPreferencesHelper.getString(Constants.PreferKeys.BANNER_LIST)
        if (!TextUtils.isEmpty(cacheData)) {
            val token = object : TypeToken<List<BannerBean>>() {

            }
            mBannerList.addAll(GsonUtil.jsonToList(token.type, cacheData))
        }
    }

    override fun onBackPressed(): Boolean {
        if (floating_action_menu != null && floating_action_menu.isExpanded) {
            floating_action_menu.collapse()
            return true
        }
        return super.onBackPressed()
    }

    /**
     * 背景渐变动画
     */
    private fun dimBackground(from: Float, to: Float) {
        val valueAnimator = ValueAnimator.ofFloat(from, to)
        valueAnimator.duration = 300
        valueAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            //                mBinding.layoutBg.setBackgroundColor(UiUtils.changeAlpha(getResources().getColor(R.color.white), value));
            layout_bg.setAlpha(value)
        }
        valueAnimator.start()
    }


    private fun getAdList() {
        WebApi.getInstance().getAd(object : IResponseListener<List<AdBean>> {
            override fun onSuccess(result: List<AdBean>?) {
                for (index in 1 until result!!.size) {
                    when (index) {
                        0 -> ImageLoader.loadImage(activity, result[index].image, iv_ad_1)
                        1 -> ImageLoader.loadImage(activity, result[index].image, iv_ad_2)
                        2 -> ImageLoader.loadImage(activity, result[index].image, iv_ad_3)
                        3 -> ImageLoader.loadImage(activity, result[index].image, iv_ad_4)
                    }
                }

            }

            override fun onError(code: String?, message: String?) {

            }

        })
    }

    private fun getBanner() {
        WebApi.getInstance().getBanner("1", object : IResponseListener<List<BannerBean>> {
            override fun onError(code: String?, message: String?) {
            }

            override fun onSuccess(result: List<BannerBean>?) {
                if (isDetached) {
                    return;
                }
                mBannerList.clear()
                mBannerList.addAll(result!!)

                convenientBanner.notifyDataSetChanged()
                convenientBanner.invalidate()
                SharedPreferencesHelper.commitString(Constants.PreferKeys.BANNER_LIST, GsonUtil.objectToJson(result))
            }

        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    inner class AdapterTeacher(activity: FragmentActivity?) : BaseRecyclerAdapter<UserInfo>(activity) {

        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<UserInfo>? {
            var view: View = layoutInflater.inflate(R.layout.adapter_recommend_user_list_item, null);
            return MyViewHolder(view);
        }

        override fun onBindMyViewHolder(holder: BaseViewHolder<UserInfo>?, dataItem: UserInfo?, position: Int) {
            var myViewholder: MyViewHolder = holder as MyViewHolder
            myViewholder.bind(dataItem!!, position)

        }

        inner class MyViewHolder(view: View) : KotlinViewHolder<UserInfo>(view) {

            override fun bind(t: UserInfo, position: Int) {
                tv_user_nickname.text = t.nickname;
                ImageLoader.loadImage(activity, t.avatar, iv_user_avatar);
            }
        }

    }

    internal inner class AdapterOrganization(activity: FragmentActivity?) :
        BaseRecyclerAdapter<OrganizationItem>(activity) {

        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<OrganizationItem>? {
            var view: View = layoutInflater.inflate(R.layout.adapter_recommend_organization_list_item, null);
            return MyViewHolder(view);
        }

        override fun onBindMyViewHolder(holder: BaseViewHolder<OrganizationItem>?, dataItem: OrganizationItem?, position: Int) {
            var myViewholder: MyViewHolder = holder as MyViewHolder
            myViewholder.bind(dataItem!!, position)

        }

        inner class MyViewHolder(view: View) : KotlinViewHolder<OrganizationItem>(view) {

            override fun bind(t: OrganizationItem, position: Int) {
                tv_organize_name.text = t.name;
                ImageLoader.loadImage(activity, t.avatar, iv_organize_avatar);
            }
        }

    }

    /**
     * 初始化Fragment
     */
    class MyFragmentTabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private var titleList: ArrayList<String> = arrayListOf<String>("课程", "机构", "老师", "伴读");
        private var mFragmentList = SparseArray<BaseFragment>()

        override fun getItem(arg0: Int): Fragment? {
            var fragment: BaseFragment? = mFragmentList.get(arg0)
            if (fragment == null) {
                when (arg0) {
                    0 -> fragment = UserListFragment()
                    1 -> fragment = UserListFragment()
                    2 -> fragment = OrganizationListFragment()
                    3 -> fragment = OrganizationListFragment()
                }
                mFragmentList.put(arg0, fragment)
            }
            return fragment
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)
        }

        override fun getCount(): Int {
            return titleList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }
    }

    internal inner class BannerHolder(itemView: View) : Holder<BannerBean>(itemView) {

        lateinit var imgView: ImageView
        lateinit var tvTitile: TextView

        override fun initView(itemView: View) {
            imgView = itemView.findViewById(R.id.imageView)
            tvTitile = itemView.findViewById(R.id.tv_title)
        }

        override fun updateUI(data: BannerBean) {
            if (activity != null) {
                ImageLoader.loadImage<Any>(activity, data.image, imgView)
                tvTitile.setText(data.title)
            }
        }
    }


}
