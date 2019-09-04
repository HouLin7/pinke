package com.bochuan.pinke.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.bochuan.pinke.R
import com.bochuan.pinke.adapter.SearchPartnerAdapter
import com.bochuan.pinke.fragment.MySearchPartnerListFragment
import com.gome.utils.ToastUtil
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.core.model.SearchPartnerItem
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import kotlinx.android.synthetic.main.activity_my_search_partner_list.*
import kotlinx.android.synthetic.main.activity_user_list.*
import kotlinx.android.synthetic.main.activity_user_list.my_tool_bar

/**
 * 我发布的伴读
 */
class MyPostSearchPartnerListActivity : BaseGomeWorkActivity() {

    lateinit var mAdapter: SearchPartnerAdapter

    private var index = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_search_partner_list)
        mAdapter = SearchPartnerAdapter(this)
        mAdapter.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent(mActivity, TeacherHomeActivity::class.java)
            startActivity(intent)

        }
        getCustomToolbar(my_tool_bar).bindActivity(this, "我发布的伴读")
        initView()
    }


    inner class PagerAdapter : FragmentPagerAdapter(supportFragmentManager) {
        var titles = arrayListOf<String>("找老师", "找伴读")
        override fun getItem(p0: Int): Fragment {
            if (p0 == 0) {
                return MySearchPartnerListFragment()
            } else {
                return MySearchPartnerListFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }

        override fun getCount(): Int {
            return titles.size
        }

    }



    private fun initView() {
        view_pager.adapter = PagerAdapter()
        tab_layout.setupWithViewPager(view_pager, true)

    }


}
