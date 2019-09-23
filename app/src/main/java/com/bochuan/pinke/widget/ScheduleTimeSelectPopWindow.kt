package com.bochuan.pinke.widget

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter
import com.bochuan.pinke.R
import com.contrarywind.view.WheelView
import razerdp.basepopup.BasePopupWindow
import java.util.*
import kotlin.collections.ArrayList

class ScheduleTimeSelectPopWindow(activity: Activity) :
    BasePopupWindow(activity, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) {

    private var wheelview_weeks: WheelView? = null
    private var wheelview_start_hour: WheelView? = null
    private var wheelview_end_hour: WheelView? = null
    private var wheelview_end_min: WheelView? = null
    private var wheelview_start_min: WheelView? = null

    private var adapterWeek: MyWheelViewAdapter

    private var adapterStartHour: MyWheelViewAdapter
    private var adapterEndHour: MyWheelViewAdapter

    private var adapterStartMin: MyWheelViewAdapter
    private var adapterEndMin: MyWheelViewAdapter

    var animationView: View? = null

    private var weeeksValues = arrayOf("周一", "周二", "周三", "周四", "周五", "周六", "周日")
    private var hourValues = arrayOf("07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22")
    private var minValues = arrayOf("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55")

    init {

        var dataListWeek = ArrayList<String>()
        dataListWeek.addAll(weeeksValues)

        var dataListHour = ArrayList<String>()
        dataListHour.addAll(hourValues)

        var dataListMin = ArrayList<String>()
        dataListMin.addAll(minValues)

        adapterWeek = MyWheelViewAdapter(dataListWeek)
        wheelview_weeks!!.adapter = adapterWeek

        adapterStartMin = MyWheelViewAdapter(dataListMin)
        adapterStartHour = MyWheelViewAdapter(dataListHour)

        adapterEndHour = MyWheelViewAdapter(dataListHour)
        adapterEndMin = MyWheelViewAdapter(dataListMin)

        wheelview_start_hour!!.adapter = adapterStartHour
        wheelview_end_hour!!.adapter = adapterEndHour

        wheelview_start_min!!.adapter = adapterStartMin
        wheelview_end_min!!.adapter = adapterEndMin

    }


    override fun onCreateContentView(): View {
        val contentView = LayoutInflater.from(context).inflate(R.layout.popwindow_schedule_time_select, null)
        wheelview_weeks = contentView.findViewById(R.id.wheelview_weeks)
        wheelview_weeks!!.setCyclic(false)
        wheelview_start_hour = contentView.findViewById(R.id.wheelview_start_hour)
        wheelview_start_hour!!.setCyclic(false)
        wheelview_end_hour = contentView.findViewById(R.id.wheelview_end_hour)
        wheelview_end_hour!!.setCyclic(false)
        wheelview_start_min = contentView.findViewById(R.id.wheelview_start_min)
        wheelview_start_min!!.setCyclic(false)
        wheelview_end_min = contentView.findViewById(R.id.wheelview_end_min)
        wheelview_end_min!!.setCyclic(false)

        animationView = contentView.findViewById(R.id.layout_content)
        var view = contentView.findViewById<View>(R.id.tv_ok)
        view.setOnClickListener {
            dismiss()
            var selectIndex = 0
            wheelview_weeks.let {
                selectIndex = wheelview_weeks!!.currentItem
            }

            var weekCout = adapterWeek.getItem(selectIndex).toString().toInt()

            wheelview_start_hour.let {
                selectIndex = wheelview_start_hour!!.currentItem
            }
            var startHour = adapterStartHour.getItem(selectIndex).toString().toInt()
            wheelview_start_min.let {
                selectIndex = wheelview_start_min!!.currentItem
            }
            var startMin = adapterStartMin.getItem(selectIndex).toString().toInt()

            wheelview_end_hour.let {
                selectIndex = wheelview_end_hour!!.currentItem
            }
            var endHour = adapterEndHour.getItem(selectIndex).toString().toInt()
            wheelview_start_min.let {
                selectIndex = wheelview_end_min!!.currentItem
            }
            var endMin = adapterEndMin.getItem(selectIndex).toString().toInt()

            var calStart = Calendar.getInstance()
            calStart.set(Calendar.HOUR_OF_DAY, startHour)
            calStart.set(Calendar.MINUTE, startMin)

            var calEnd = Calendar.getInstance()
            calEnd.set(Calendar.HOUR_OF_DAY, endHour)
            calEnd.set(Calendar.MINUTE, endMin)

            listener?.onSuccess(weekCout, calStart.timeInMillis, calEnd.timeInMillis)
        }
        view = contentView.findViewById(R.id.tv_cancel)
        view.setOnClickListener {
            dismiss()
            listener?.onCancel()
        }
        return contentView
    }

    var listener: OnTimeResultListener? = null

    interface OnTimeResultListener {
        fun onSuccess(weekCount: Int, startTime: Long, endTime: Long)
        fun onCancel()
    }

    override fun onCreateAnimateView(): View {
        return animationView!!
    }

    override fun onCreateShowAnimation(): Animation {
        return getTranslateVerticalAnimation(1f, 0f, 500)
    }

    override fun onCreateDismissAnimation(): Animation {
        return getTranslateVerticalAnimation(0f, 1f, 500)
    }

    inner class MyWheelViewAdapter(dataList: ArrayList<String>) : ArrayWheelAdapter<String>(dataList) {


    }

}
