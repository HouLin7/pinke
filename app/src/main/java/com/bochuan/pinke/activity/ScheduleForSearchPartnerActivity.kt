package com.bochuan.pinke.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import com.bochuan.pinke.R
import com.bochuan.pinke.fragment.ScheduleFragment
import com.gome.applibrary.activity.BaseActivity
import com.gome.utils.ToastUtil
import com.gome.work.common.activity.BaseGomeWorkActivity
import kotlinx.android.synthetic.main.activity_setting.*


class ScheduleForSearchPartnerActivity : BaseGomeWorkActivity() {

    private var mFragment: ScheduleFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_search_partner)
        getCustomToolbar(title_bar).bindActivity(this, "日程")

        var fragment = ScheduleFragment()
        var args = Bundle()
        var editModel = intent.getStringExtra(ScheduleFragment.EXTRA_VIEW_MODEL)
        if (!TextUtils.isEmpty(editModel)) {
            args.putString(ScheduleFragment.EXTRA_VIEW_MODEL, editModel)
        }
        args.putInt(ScheduleFragment.EXTRA_SCHEDULE_TYPE, ScheduleFragment.WEEK_SCHEDULE)
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
        var result = mFragment!!.selectScheduleItems
        if (result == null || result.isEmpty()) {
            ToastUtil.showToast(this, "请选择日程")
            return true
        }

        var data = Intent()
        data.putExtra(BaseActivity.EXTRA_DATA, result)
        setResult(Activity.RESULT_OK, data)
        finish()
        return true
    }

    private fun initView() {


    }


}
