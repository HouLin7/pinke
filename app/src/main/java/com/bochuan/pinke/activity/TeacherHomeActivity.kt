package com.bochuan.pinke.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.amap.api.location.AMapLocation
import com.bochuan.pinke.R
import com.bochuan.pinke.adapter.CourseAdapter
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.core.model.UserInfo
import kotlinx.android.synthetic.main.activity_teacher_home.*


class TeacherHomeActivity : BaseGomeWorkActivity() {

    lateinit var location: AMapLocation

    private lateinit var mAdapter: CourseAdapter

    private var mTeacher: UserInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_home)
        getCustomToolbar(title_bar).bindActivity(this,"教师主页")
        mAdapter = CourseAdapter(this)
//        mTeacher = intent.getSerializableExtra(EXTRA_DATA) as UserInfo
        initView()
    }


    override fun onBackPressed() {
        dismissInputMethod()
        super.onBackPressed()
    }

    private fun initView() {
        smart_refresh_layout.setEnableLoadMore(false)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter

    }


}
