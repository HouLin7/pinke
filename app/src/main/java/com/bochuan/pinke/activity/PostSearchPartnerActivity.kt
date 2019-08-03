package com.bochuan.pinke.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import com.bochuan.pinke.R
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.common.widget.BaseMultiSelectPopWindow
import com.gome.work.common.widget.BaseSingleSelectPopWindow
import com.gome.work.core.Constants
import com.gome.work.core.model.CfgDicItem
import com.gome.work.core.utils.DateUtils
import com.gome.work.core.utils.GsonUtil
import com.gome.work.core.utils.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_post_search_partner.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * 发布信息
 */

class PostSearchPartnerActivity : BaseGomeWorkActivity() {

    var courseList = ArrayList<CfgDicItem>()
    var gradeList = ArrayList<CfgDicItem>()
    var classTypeList = ArrayList<CfgDicItem>()

    var attendDate: Calendar? = null

    var selectGrade: CfgDicItem? = null
    var selectCourse: CfgDicItem? = null

    var selectClassType: CfgDicItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_search_partner)
        getCustomToolbar(title_bar).bindActivity(this, "找伴读")
        initData()
        initView()
    }

    private fun initData() {
        var rawData = SharedPreferencesHelper.getString(Constants.PreferKeys.SYS_CFG_COURSE)
        courseList = GsonUtil.jsonToList(rawData, CfgDicItem::class.java) as ArrayList<CfgDicItem>

        rawData = SharedPreferencesHelper.getString(Constants.PreferKeys.SYS_TEACH_TYPE)
        classTypeList = GsonUtil.jsonToList(rawData, CfgDicItem::class.java) as ArrayList<CfgDicItem>

        rawData = SharedPreferencesHelper.getString(Constants.PreferKeys.SYS_CFG_GRADE)
        gradeList = GsonUtil.jsonToList(rawData, CfgDicItem::class.java) as ArrayList<CfgDicItem>

    }

    override fun onBackPressed() {
        dismissInputMethod()
        super.onBackPressed()
    }

    private fun initView() {
        tv_course.setOnClickListener { view ->
            var popWindow = BaseSingleSelectPopWindow(mActivity, courseList)
            popWindow.showPopupWindow(view)
            popWindow.listener = object : BaseMultiSelectPopWindow.OnCfgItemSelectListener {
                override fun onSelect(dataList: List<CfgDicItem>) {
                    selectCourse = dataList[0]
                    tv_course.text = selectCourse!!.name
                }
            }
        }

        tv_grade.setOnClickListener {
            var popWindow = BaseSingleSelectPopWindow(mActivity, gradeList)
            popWindow.showPopupWindow(it)
            popWindow.listener = object : BaseMultiSelectPopWindow.OnCfgItemSelectListener {
                override fun onSelect(dataList: List<CfgDicItem>) {
                    selectGrade = dataList[0]
                    tv_grade.text = selectGrade!!.name
                }
            }

        }

        tv_attend_date.setOnClickListener {
            showDatePickerDlg(Calendar.getInstance(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                var cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                attendDate = cal
                var strDate = DateUtils.format(cal.timeInMillis, "yyyy-MM-dd")
                tv_attend_date.setText(strDate)

            })
        }

        tv_class_type.setOnClickListener {
            var popWindow = BaseSingleSelectPopWindow(mActivity, classTypeList)
            popWindow.showPopupWindow(it)
            popWindow.listener = object : BaseMultiSelectPopWindow.OnCfgItemSelectListener {
                override fun onSelect(dataList: List<CfgDicItem>) {
                    selectClassType = dataList[0]
                    tv_class_type.text = selectClassType!!.name
                }
            }
        }

        tv_label_schedule_attend.setOnClickListener {
            var intent = Intent(mActivity, ScheduleForSearchPartnerActivity::class.java)
            startActivity(intent)
        }

    }

    fun showDatePickerDlg(cal: Calendar, listener: DatePickerDialog.OnDateSetListener) {
        var year = cal.get(Calendar.YEAR);
        var month = cal.get(Calendar.MONTH)
        var dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(mActivity!!, listener, year, month, dayOfMonth)
        datePickerDialog.show()
    }
}