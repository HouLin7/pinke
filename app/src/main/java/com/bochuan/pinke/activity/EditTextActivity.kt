package com.bochuan.pinke.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bochuan.pinke.R
import com.gome.applibrary.activity.BaseActivity
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.core.utils.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_edit_text.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.activity_setting.title_bar


class EditTextActivity : BaseGomeWorkActivity() {


    companion object {
        const val EXTRA_TITLE = "extra.title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_text)
        var title = "设置"
        if (intent.hasExtra(EXTRA_TITLE)) {
            title = intent.getStringExtra(EXTRA_TITLE)
        }
        getCustomToolbar(title_bar).bindActivity(this, title)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val item = menu.add("确定")
//        item.setIcon(R.mipmap.ic_title_more)
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var data = Intent();
        data.putExtra(BaseActivity.EXTRA_DATA, edit_text.text.toString())
        setResult(Activity.RESULT_OK, data)
        finish()
        return super.onOptionsItemSelected(item)
    }


}
