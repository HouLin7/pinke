package com.bochuan.pinke.activity

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import com.amap.api.location.AMapLocation
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult

import com.amap.api.services.poisearch.PoiSearch
import com.bochuan.pinke.R
import com.bochuan.pinke.util.AMapLocationManager
import com.bochuan.pinke.util.AMapLocationManager.ILocationCallback
import com.gome.utils.CommonUtils
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
 *发布找伴读信息
 */

class PostSearchPartnerActivity : BaseGomeWorkActivity() {

    companion object {
        const val REQUEST_CODE_ADDRESS_SELECT = 1
    }

    var courseList = ArrayList<CfgDicItem>()
    var gradeList = ArrayList<CfgDicItem>()
    var classTypeList = ArrayList<CfgDicItem>()

    var attendDate: Calendar? = null

    var selectGrade: CfgDicItem? = null

    var selectCourse: CfgDicItem? = null

    var selectClassType: CfgDicItem? = null

    var poiList: ArrayList<PoiItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_search_partner)
        getCustomToolbar(title_bar).bindActivity(this, "找伴读")
        initData()
        initView()
        getLocation()
    }


    fun searchPoi(lat: Double, long: Double) {
        var query = PoiSearch.Query("", "120300", "北京")

        var poiSearch = PoiSearch(this, query)
        var ad = LatLonPoint(lat, long)
        var bound = PoiSearch.SearchBound(ad, 1000)
        poiSearch.bound = bound

        poiSearch.setOnPoiSearchListener(object : PoiSearch.OnPoiSearchListener {
            override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {
            }

            override fun onPoiSearched(p0: PoiResult?, p1: Int) {
                poiList = p0!!.pois
            }

        })
        poiSearch.searchPOIAsyn()
    }


    private fun getLocation() {
        if (!CommonUtils.isGPSOpen(this)) {
            showAlertDlg("请先打开手机定位开关")
            return;
        }
        requestPermission(
            Manifest.permission.ACCESS_FINE_LOCATION
        ) { permission, isSuccess ->
            if (isSuccess) {
                var locMamanger = AMapLocationManager(this)
                locMamanger.getLocation(object : ILocationCallback {
                    override fun call(loc: AMapLocation) {
                        var name = loc.aoiName
                        tv_address.text = loc.aoiName
                        searchPoi(loc.latitude, loc.longitude)
                    }
                })
            } else {

            }

        }

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

        tv_address_modify.setOnClickListener {
            var intent = Intent(mActivity, AddressEditActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADDRESS_SELECT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            REQUEST_CODE_ADDRESS_SELECT -> {

            }
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