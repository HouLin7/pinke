package com.bochuan.pinke.fragment

import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.bochuan.pinke.R
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.gome.utils.ToastUtil
import com.gome.work.common.KotlinViewHolder
import com.gome.work.common.adapter.BaseRecyclerAdapter
import com.gome.work.common.imageloader.ImageLoader
import com.gome.work.common.utils.BlurUtils
import com.gome.work.core.model.BannerBean
import com.gome.work.core.model.OrganizationItem
import com.gome.work.core.model.UserInfo
import kotlinx.android.synthetic.main.adapter_recommend_organization_list_item.*
import kotlinx.android.synthetic.main.adapter_recommend_user_list_item.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    private var mBannerList: MutableList<BannerBean> = mutableListOf();

    private var mBestTeacherList: MutableList<UserInfo> = mutableListOf();

    private var mBestOrganizationList: MutableList<OrganizationItem> = mutableListOf();

    private var mAdapterOrganization: AdapterOrganization? = null

    private var mAdapterTeacher: AdapterTeacher? = null;

    init {
        createTestData();
    }

    override fun refreshData() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    private fun createTestData() {
        val value = BannerBean();
        value.title = "111";
        value.small_image =
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1561640489738&di=7d758b4a7e710f7878b7acea1178b7b2&imgtype=0&src=http%3A%2F%2Fpic.rmb.bdstatic.com%2F162e189b94a5faac15a4fba25ea242e1.jpeg"
        mBannerList.add(value)
        mBannerList.add(value)
        mBannerList.add(value)
        mBannerList.add(value)

        var teacher = UserInfo();
        teacher.avatar = "http://b-ssl.duitang.com/uploads/item/201711/09/20171109200813_2vtKE.jpeg";
        teacher.name = "推荐老师"
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View? = inflater.inflate(R.layout.fragment_home, null)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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


        var conven: ConvenientBanner<BannerBean> = convenientBanner as ConvenientBanner<BannerBean>
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
            ToastUtil.showToast(activity, "找同学")
            floating_action_menu.collapse()
        }
        fab_search_teacher.setOnClickListener {
            floating_action_menu.collapse()
            ToastUtil.showToast(activity, "找老师")
        }

        layout_bg.setOnClickListener {
            floating_action_menu.collapse()
        }
        layout_bg.isClickable = false;
    }


    private var bitmap: Bitmap? = null


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

    inner class AdapterTeacher(activity: FragmentActivity?) : BaseRecyclerAdapter<UserInfo>(activity) {

        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            var view: View = layoutInflater.inflate(R.layout.adapter_recommend_user_list_item, null);
            return MyViewHolder(view);
        }

        override fun onBindMyViewHolder(holder: RecyclerView.ViewHolder?, dataItem: UserInfo?, position: Int) {
            var myViewholder: MyViewHolder = holder as MyViewHolder
            myViewholder.bind(dataItem!!)

        }

        inner class MyViewHolder(view: View) : KotlinViewHolder<UserInfo>(view) {

            override fun bind(t: UserInfo) {
                tv_user_nickname.text = t.name;
                ImageLoader.loadImage(activity, t.avatar, iv_user_avatar);
            }
        }

    }

    internal inner class AdapterOrganization(activity: FragmentActivity?) :
        BaseRecyclerAdapter<OrganizationItem>(activity) {

        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            var view: View = layoutInflater.inflate(R.layout.adapter_recommend_organization_list_item, null);
            return MyViewHolder(view);
        }

        override fun onBindMyViewHolder(holder: RecyclerView.ViewHolder?, dataItem: OrganizationItem?, position: Int) {
            var myViewholder: MyViewHolder = holder as MyViewHolder
            myViewholder.bind(dataItem!!)

        }

        inner class MyViewHolder(view: View) : KotlinViewHolder<OrganizationItem>(view) {

            override fun bind(t: OrganizationItem) {
                tv_organize_name.text = t.name;
                ImageLoader.loadImage(activity, t.avatar, iv_organize_avatar);
            }
        }

    }

    /**
     * 初始化Fragment
     */
    internal inner class MyFragmentTabAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

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
            return titleList!![position]
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
                ImageLoader.loadImage<Any>(activity, data.small_image, imgView)
                tvTitile.setText(data.title)
            }
        }
    }

}

