package com.bochuan.pinke.activity

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import com.amap.api.location.AMapLocation
import com.bochuan.pinke.R
import com.bochuan.pinke.util.AMapLocationManager
import com.bochuan.pinke.util.AMapLocationManager.ILocationCallback
import com.gome.applibrary.activity.BaseActivity
import com.gome.utils.CommonUtils
import com.gome.utils.ToastUtil
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.common.widget.BaseSelectPopWindow
import com.gome.work.common.widget.ListSelectPopWindow
import com.gome.work.core.model.*
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import com.gome.work.core.utils.DateUtils
import kotlinx.android.synthetic.main.activity_post_search_partner.*
import java.util.*
import kotlin.collections.ArrayList

/**
 *发布找伴读信息
 */

class PostSearchPartnerActivity : BaseGomeWorkActivity() {

    companion object {

        const val TO_SEARCH_TEACHER = "search.teacher"
        const val TO_SEARCH_PARTNER = "search.partner"

        const val EXTRA_MODEL = "extra.model"

        const val REQUEST_CODE_ADDRESS_SELECT = 1

        const val REQUEST_CODE_SCHEDULE_SELECT = 2
    }

    var currModel = TO_SEARCH_PARTNER

    var sexList: ArrayList<CfgDicItem> = SysCfgData.getSexCfgItems()!!

    /**
     * 上课时间
     */
    var attendDate: Calendar? = null

    var selectGrade: CfgDicItem? = null

    var selectCourse: CfgDicItem? = null

    var selectSex: CfgDicItem? = null

    var selectClassType: CfgDicItem? = null

    var location: AMapLocation? = null

    var scheduleData: ArrayList<ScheduleTimeItem>? = null

    var selectAddress: AddressItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_search_partner)
        if (intent.hasExtra(EXTRA_MODEL)) {
            currModel = intent.getStringExtra(EXTRA_MODEL)
        }
        getCustomToolbar(title_bar).bindActivity(this, "找伴读")
        initData()
        initView()
        getLocation()
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
                var locManager = AMapLocationManager(this)
                locManager.getLocation(object : ILocationCallback {
                    override fun call(loc: AMapLocation) {
                        tv_address.text = loc.aoiName
                        var address = loc.address
                        location = loc
                    }
                })
            } else {

            }
        }

    }

    private fun initData() {

    }

    override fun onBackPressed() {
        dismissInputMethod()
        super.onBackPressed()
    }

    private fun initView() {
        if (TO_SEARCH_TEACHER == this.currModel) {
            checkbox_to.text = "同时伴读"
            getCustomToolbar(title_bar).title = "找老师"
        }
        layout_sex.setOnClickListener {
            var popWindow = ListSelectPopWindow(mActivity, sexList)
            popWindow.showPopupWindow(it)
            popWindow.listener = object : BaseSelectPopWindow.OnCfgItemSelectListener {
                override fun onSelect(dataList: List<CfgDicItem>) {
                    selectSex = dataList[0]
                    tv_sex.text = selectSex!!.name
                }
            }
        }

        tv_course.setOnClickListener { view ->
            var popWindow = ListSelectPopWindow(mActivity, sysCfgData!!.course)
            popWindow.showPopupWindow(view)
            popWindow.listener = object : BaseSelectPopWindow.OnCfgItemSelectListener {
                override fun onSelect(dataList: List<CfgDicItem>) {
                    selectCourse = dataList[0]
                    tv_course.text = selectCourse!!.name
                }
            }
        }

        tv_grade.setOnClickListener {
            var popWindow = ListSelectPopWindow(mActivity, sysCfgData!!.grade)
            popWindow.showPopupWindow(it)
            popWindow.listener = object : BaseSelectPopWindow.OnCfgItemSelectListener {
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
            var popWindow = ListSelectPopWindow(mActivity, sysCfgData!!.classType)
            popWindow.showPopupWindow(it)
            popWindow.listener = object : BaseSelectPopWindow.OnCfgItemSelectListener {
                override fun onSelect(dataList: List<CfgDicItem>) {
                    selectClassType = dataList[0]
                    tv_class_type.text = selectClassType!!.name
                }
            }
        }

        layout_schedule_attend.setOnClickListener {
            var intent = Intent(mActivity, ScheduleForSearchPartnerActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_SCHEDULE_SELECT)
        }

        tv_address_modify.setOnClickListener {
            var intent = Intent(mActivity, AddressEditActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADDRESS_SELECT)
        }

        button_post.setOnClickListener {
            if (!isValidInput()) {
                return@setOnClickListener
            }

            var postItem = getPostData()
            showProgressDlg()
            WebApi.getInstance().postSearchPartnerItem(postItem, object : IResponseListener<String> {
                override fun onError(code: String?, message: String?) {
                    dismissProgressDlg()
                    ToastUtil.showToast(mActivity, message)
                }

                override fun onSuccess(result: String?) {
                    dismissProgressDlg()
                    ToastUtil.showToast(mActivity, "发布成功")
                    finish()
                }

            })
        }
    }

    private fun getPostData(): PostSearchPartnerItem {
        var result = PostSearchPartnerItem()
        result.classType = selectClassType!!.id
        result.position = PostSearchPartnerItem.Position()
        if (selectAddress != null) {
            result.position.assign(selectAddress)
        } else {
            location?.let {
                result.position.province = location!!.province
                result.position.city = location!!.city
                result.position.district = location!!.district
                result.position.address = location!!.poiName
                result.position.latitude = location!!.latitude
                result.position.longitude = location!!.longitude
            }
        }
        result.discipline = selectCourse!!.id

        selectSex?.let { result.sex = selectSex!!.id }

        result.note = edit_note.text.toString()
        selectGrade?.let {
            result.grade = selectGrade!!.id
        }

        result.score = edit_course_score.text.toString()

//        result.school = edit_school.text.toString()
        result.note = edit_note.text.toString()
        attendDate?.let {
            result.openDate = attendDate!!.timeInMillis
        }

        scheduleData?.let {
            result.scheduleCards = ArrayList<PostSearchPartnerItem.scheduleCard>()
            for (item in scheduleData!!) {
                var scheduleCard = PostSearchPartnerItem.scheduleCard()
                scheduleCard.type = 1
                scheduleCard.startTime = item.startTime
                scheduleCard.endTime = item.endTime
                result.scheduleCards.add(scheduleCard)
            }
        }
        if (checkbox_to.isChecked) {
            result.purpose = PostSearchPartnerItem.PurposeType.SEEK_ALL.toString()
        } else {
            if (TO_SEARCH_TEACHER == this.currModel) {
                result.purpose = PostSearchPartnerItem.PurposeType.SEEK_TEACHER.toString()
            } else {
                result.purpose = PostSearchPartnerItem.PurposeType.SEEK_PARTNER.toString()
            }
        }

        return result
    }

    private fun isValidInput(): Boolean {
        if (selectClassType == null) {
            ToastUtil.showToast(mActivity, "请选择班型")
            return false
        }
        if (selectCourse == null) {
            ToastUtil.showToast(mActivity, "请选择科目")
            return false
        }

//        if (selectGrade == null) {
//            ToastUtil.showToast(mActivity, "请选择年级")
//            return false
//        }

        if (edit_cost.text.toString() == null) {
            ToastUtil.showToast(mActivity, "请输入价格")
            edit_cost.requestFocus()
            return false
        }

//        if (edit_school.text.toString() == null) {
//            ToastUtil.showToast(mActivity, "请输入学校")
//            edit_school.requestFocus()
//            return false
//        }

        if (attendDate == null) {
            ToastUtil.showToast(mActivity, "请输入上课日期")
            return false
        }

        if (location == null && selectAddress == null) {
            ToastUtil.showToast(mActivity, "位置信息不能为空")
            return false
        }

        if (scheduleData == null) {
            ToastUtil.showToast(mActivity, "请选择上课时间")
            return false
        }
        return true;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            REQUEST_CODE_ADDRESS_SELECT -> {
                var addressData = data!!.getSerializableExtra(BaseActivity.EXTRA_DATA) as AddressItem
                selectAddress = addressData

            }
            REQUEST_CODE_SCHEDULE_SELECT -> {
                scheduleData = data!!.getSerializableExtra(BaseActivity.EXTRA_DATA) as ArrayList<ScheduleTimeItem>
                tv_schedule_attend.text = "已选择"
            }

        }

    }

    private fun showDatePickerDlg(cal: Calendar, listener: DatePickerDialog.OnDateSetListener) {
        var year = cal.get(Calendar.YEAR);
        var month = cal.get(Calendar.MONTH)
        var dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(mActivity!!, listener, year, month, dayOfMonth)
        datePickerDialog.show()
    }
}