package com.bochuan.pinke.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bochuan.pinke.R
import com.bochuan.pinke.fragment.ScheduleFragment
import com.gome.work.common.activity.BaseGomeWorkActivity
import kotlinx.android.synthetic.main.activity_setting.*


class ScheduleForSearchPartnerActivity : BaseGomeWorkActivity() {

    var mFragment: ScheduleFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_search_partner)
        getCustomToolbar(title_bar).bindActivity(this, "日程")

        var fragment = ScheduleFragment()
        var args = Bundle()
        args.putInt(ScheduleFragment.EXTEA_VIEW_MDDEL, ScheduleFragment.WEEK_SCHEDULE)
        fragment.arguments = args
        supportFragmentManager.beginTransaction().replace(R.id.layout_content, fragment).commit()
        mFragment = fragment
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val item = menu.add("确定")
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS or MenuItem.SHOW_AS_ACTION_WITH_TEXT)

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    private fun initView() {


    }


}
