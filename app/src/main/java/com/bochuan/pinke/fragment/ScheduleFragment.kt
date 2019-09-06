package com.bochuan.pinke.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bochuan.pinke.R
import com.gome.work.common.KotlinViewHolder
import com.gome.work.common.adapter.BaseRecyclerAdapter
import com.gome.work.common.adapter.BaseViewHolder
import com.gome.work.common.divider.CustomNewsDivider
import com.gome.work.common.utils.UiUtils
import com.gome.work.core.model.ScheduleTimeItem
import com.gome.work.core.utils.DateUtils
import kotlinx.android.synthetic.main.adapter_course_schedule_list_item.*
import kotlinx.android.synthetic.main.fragment_schedule.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet


class ScheduleFragment : BaseFragment() {

    companion object {

        const val EXTRA_SCHEDULE_TYPE = "extra.schedule.type"
        /**
         * 周计划
         */
        const val WEEK_SCHEDULE: Int = 1
        /**
         * 每日计划
         */
        const val WEEK_DAILY: Int = 2

        const val EXTRA_MODEL = "extra.model"

        /**
         * 周计划
         */
        const val MODEL_VIEW: String = "model.view"
        /**
         *  可编辑状态
         */
        const val MODEL_EDIT = "model.edit"


    }

    private var currModel: String = MODEL_VIEW

    var mAdapter: Adapter? = null;

    private var nowWeekHeaderData: WeekHeaderItem? = null

    var currWeekHeaderData: WeekHeaderItem = getWeekHeaderData(getFirstDayOfWeek())

    private val currSelectCourseScheduleList = HashMap<String, HashSet<CourseScheduleItem>>()

    var selectScheduleItems: ArrayList<ScheduleTimeItem>? = null
        get() {
            var result = ArrayList<ScheduleTimeItem>()
            val sDateFormat = SimpleDateFormat("yyyy-MM-dd")
            for (item in currSelectCourseScheduleList) {
                var date = sDateFormat.parse(item.key)
                for (value in item.value) {
                    result.add(calculateSchedueTime(date.time, value.startTime, value.endTime))
                }
            }
            return result
        }

    override fun getLayoutID(): Int {
        return R.layout.fragment_schedule
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var type = arguments?.getInt(EXTRA_SCHEDULE_TYPE)

        arguments?.let {
            currModel = arguments!!.getString(EXTRA_MODEL)
        }

        if (type != null) {
            if (WEEK_SCHEDULE == type) {
                layout_control.visibility = View.GONE
            } else {
                layout_control.visibility = View.VISIBLE
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false);
        recyclerView.addItemDecoration(
            CustomNewsDivider(
                context,
                DividerItemDecoration.HORIZONTAL,
                (1 * resources.displayMetrics.density).toInt(),
                R.color.theme_blue
            )
        )
        if (nowWeekHeaderData == null) {
            nowWeekHeaderData = currWeekHeaderData
        }

        tv_prev_week.setOnClickListener {
            if (currWeekHeaderData.prev == null) {
                var startTime = currWeekHeaderData.values?.get(0)
                val cal = Calendar.getInstance()
                cal.timeInMillis = startTime!!
                cal.add(Calendar.DAY_OF_MONTH, -7)
                startTime = cal.timeInMillis
                currWeekHeaderData.prev = getWeekHeaderData(startTime!!)
            }
            currWeekHeaderData = currWeekHeaderData.prev!!;
            updateHeaderView(currWeekHeaderData.values!!)
        }

        tv_next_week.setOnClickListener {
            if (currWeekHeaderData.next == null) {
                var startTime: Long? = null
                var index = currWeekHeaderData.values?.lastIndex
                startTime = currWeekHeaderData?.values?.get(index!!)
                startTime = DateUtils.getNextDay(startTime!!)
                currWeekHeaderData.next = getWeekHeaderData(startTime!!)
            }
            currWeekHeaderData = currWeekHeaderData.next!!;

            updateHeaderView(currWeekHeaderData.values!!)
        }

        tv_now_week.setOnClickListener {
            currWeekHeaderData = nowWeekHeaderData!!
            updateHeaderView(currWeekHeaderData?.values!!)
        }

        updateDateView(Calendar.getInstance().timeInMillis)

        tv_date.setOnClickListener {
            var cal = Calendar.getInstance()

            if (!currWeekHeaderData.isHasToday()) {
                cal.timeInMillis = currWeekHeaderData.values?.last()!!
            }

            showDatePickerDlg(cal, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                cal = Calendar.getInstance()
                cal.set(year, month, dayOfMonth)

                if (!currWeekHeaderData.isContain(cal.timeInMillis)) {
                    updateDateView(cal.timeInMillis)
                    currWeekHeaderData = getWeekHeaderData(cal.timeInMillis)
                    updateHeaderView(currWeekHeaderData.values!!)
                }

            })
        }

    }

    private fun showDatePickerDlg(cal: Calendar, listener: DatePickerDialog.OnDateSetListener) {
        var year = cal.get(Calendar.YEAR);
        var month = cal.get(Calendar.MONTH)
        var dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(mActivity, listener, year, month, dayOfMonth)
        datePickerDialog.show()
    }

    private fun updateDateView(time: Long) {
        var cal = Calendar.getInstance()
        cal.timeInMillis = time
        var month = cal.get(Calendar.MONTH) + 1
        var year = cal.get(Calendar.YEAR)
        tv_date.setText(year.toString() + "年" + month.toString() + "月")
    }

    /**
     * @param intervalTime 时间间隔 单位分钟
     */
    private fun getWeekColumnData(intervalTime: Int, count: Int): List<CourseScheduleItem> {
        var result = ArrayList<CourseScheduleItem>()
        var calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

//        var random = Random()
        var startTime = calendar.timeInMillis;
        var offsetByMillis = TimeUnit.MINUTES.toMillis(intervalTime.toLong())
        for (index in 0 until count) {
            var item = CourseScheduleItem()
            item.startTime = startTime + index * offsetByMillis
            item.endTime = startTime + (index + 1) * offsetByMillis
            result.add(item)
//            var i = random.nextInt(6)
//            item.weeks[i]!!.state = 0
        }

        return result
    }


    private fun getFirstDayOfWeek(): Long {
        return getFirstDayOfWeek(Calendar.getInstance())
    }

    private fun getFirstDayOfWeek(calendar: Calendar): Long {
        var day = calendar.get(Calendar.DAY_OF_WEEK)
        if (day == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, -6)
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, -day + 2)
        }
        return calendar.timeInMillis
    }

    private fun getWeekHeaderData(startTime: Long): WeekHeaderItem {
        var values = ArrayList<Long>()
        for (index in 0 until 7) {
            values.add(startTime + index * TimeUnit.DAYS.toMillis(1))
        }
        var weekHeaderItem = WeekHeaderItem()
        weekHeaderItem.values = values
        return weekHeaderItem
    }

    private fun updateHeaderView(dateList: ArrayList<Long>) {
        if (dateList.size != 7) {
            return
        }
        var calNow = Calendar.getInstance()
        var cal = Calendar.getInstance()
        var textViews = listOf(
            tv_header_week0, tv_header_week1,
            tv_header_week2, tv_header_week3,
            tv_header_week4, tv_header_week5,
            tv_header_week6
        )
        var numZH = listOf<String>("一", "二", "三", "四", "五", "六", "日")
        var index = 0
        for (date in dateList) {
            cal.setTimeInMillis(date)
            var dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
            textViews[index].setText(dayOfMonth.toString() + "\n" + numZH[index])

            if (DateUtils.isSameDay(calNow, cal)) {
                textViews[index].setTextColor(UiUtils.getColor(R.color.theme_blue))
            } else {
                textViews[index].setTextColor(UiUtils.getColor(R.color.app_black))
            }
            index++
        }

        updateDateView(dateList.last())
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (mAdapter != null) {
            return;
        }
        mAdapter = Adapter(activity)
        recyclerView.adapter = mAdapter

        radio_group.setOnCheckedChangeListener { group, checkedId ->
            var intervalTime = 0
            var count = 30
            when (checkedId) {
                R.id.radio_button_half_hour -> {
                    intervalTime = 30
                    count = 30
                }
                R.id.radio_button_hour -> {
                    intervalTime = 60
                    count = 15
                }
            }

            var dataList = getWeekColumnData(intervalTime, count)
            mAdapter!!.setItemList(dataList)
            currSelectCourseScheduleList.clear()
        }

        var intervalTime = 0
        var count = 0
        when (radio_group.checkedRadioButtonId) {
            R.id.radio_button_half_hour -> {
                intervalTime = 30
                count = 30
            }
            R.id.radio_button_hour -> {
                intervalTime = 60
                count = 15
            }
        }
        var dataList = getWeekColumnData(intervalTime, count)
        mAdapter!!.setItemList(dataList)


        updateHeaderView(currWeekHeaderData.values!!)
    }

    override fun refreshData() {

    }

    /**
     * @param date 当前的日期
     * @param start 开始的时间
     * @param end  结束的时间
     */
    private fun calculateSchedueTime(date: Long, startTime: Long, endTime: Long): ScheduleTimeItem {
        val calDate = Calendar.getInstance()

        val calStartTime = Calendar.getInstance()
        calStartTime.timeInMillis = startTime

        var calEndTime = Calendar.getInstance()
        calEndTime.timeInMillis = endTime

        var hours1 = calStartTime.get(Calendar.HOUR_OF_DAY)
        var min1 = calStartTime.get(Calendar.MINUTE)

        var hours2 = calEndTime.get(Calendar.HOUR_OF_DAY)
        var min2 = calEndTime.get(Calendar.MINUTE)

        var result = ScheduleTimeItem()
        calDate.set(Calendar.HOUR_OF_DAY, hours1)
        calDate.set(Calendar.MINUTE, min1)
        result.startTime = calDate.timeInMillis
        calDate.set(Calendar.HOUR_OF_DAY, hours2)
        calDate.set(Calendar.MINUTE, min2)
        result.endTime = calDate.timeInMillis

        return result
    }

    inner class Adapter(activity: FragmentActivity?) : BaseRecyclerAdapter<CourseScheduleItem>(activity) {

        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<CourseScheduleItem> {
            var view: View = layoutInflater.inflate(R.layout.adapter_course_schedule_list_item, parent, false);
            return MyViewHolder(view)
        }

        override fun onBindMyViewHolder(holder: BaseViewHolder<CourseScheduleItem>?, dataItem: CourseScheduleItem?, position: Int) {
            var myViewholder: MyViewHolder = holder as MyViewHolder
            myViewholder.bind(dataItem!!, position)

        }

        inner class MyViewHolder(view: View) : KotlinViewHolder<CourseScheduleItem>(view) {

            private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

            var textViews = arrayOf(tv_week0, tv_week1, tv_week2, tv_week3, tv_week4, tv_week5, tv_week6)

            init {
                for (item in textViews) {
                    if (MODEL_EDIT == currModel) {
                        item.setOnClickListener { handleViewClick(it.id) }
                    }
                }
            }

            private fun handleViewClick(viewId: Int) {
                var activityItem: ActivityItem? = null
                var date: Long? = null
                when (viewId) {
                    R.id.tv_week0 -> {
                        activityItem = dataItem.weeks[0]
                        date = currWeekHeaderData.values?.get(0)
                    }
                    R.id.tv_week1 -> {
                        activityItem = dataItem.weeks[1]
                        date = currWeekHeaderData.values?.get(1)
                    }
                    R.id.tv_week2 -> {
                        activityItem = dataItem.weeks[2]
                        date = currWeekHeaderData.values?.get(2)
                    }
                    R.id.tv_week3 -> {
                        activityItem = dataItem.weeks[3]
                        date = currWeekHeaderData.values?.get(3)
                    }
                    R.id.tv_week4 -> {
                        activityItem = dataItem.weeks[4]
                        date = currWeekHeaderData.values?.get(4)
                    }
                    R.id.tv_week5 -> {
                        activityItem = dataItem.weeks[5]
                        date = currWeekHeaderData.values?.get(5)
                    }
                    R.id.tv_week6 -> {
                        activityItem = dataItem.weeks[6]
                        date = currWeekHeaderData.values?.get(6)
                    }

                }

                if (activityItem!!.state == 0) {
                    activityItem!!.state = 1
                } else {
                    activityItem!!.state = 0
                }
                if (activityItem!!.state != 0) {
                    if (date != null) {
                        if (date > 0) {
                            var strTime = simpleDateFormat.format(Date(date))
                            var values = currSelectCourseScheduleList[strTime]
                            if (values == null) {
                                values = HashSet();
                                currSelectCourseScheduleList[strTime] = values
                            }
                            values.add(dataItem)
                        }
                    }
                }
                notifyDataSetChanged()
            }

            private var dateFormat = SimpleDateFormat("HH:mm")
            override fun bind(t: CourseScheduleItem, position: Int) {
                var strStart = dateFormat.format(Date(t.startTime))
                var strEnd = dateFormat.format(Date(t.endTime))
                tv_date_dsc.text = strStart + "-" + strEnd;
                var index = 0
                for (item in t.weeks) {
                    var targetView = textViews[index]
                    if (item!!.state == 0) {
                        targetView.isEnabled = true
                        targetView.setBackgroundColor(UiUtils.getColor(R.color.white))
                    } else {
                        targetView.setBackgroundColor(UiUtils.getColor(R.color.theme_green))
                        targetView.isEnabled = false
                    }
                    index++
                }
            }
        }

    }

    class WeekHeaderItem {
        var prev: WeekHeaderItem? = null
        var next: WeekHeaderItem? = null
        var values: ArrayList<Long>? = null


        override fun equals(other: Any?): Boolean {
            if (other == null) {
                return false
            }
            if (other is WeekHeaderItem) {
                var target = other as WeekHeaderItem
                if (target.values == null && values == null) {
                    return true
                } else if (target.values != null && values != null) {
                    if (target.values!!.size == values!!.size) {
                        for (index in 0 until values!!.size - 1) {
                            if (!DateUtils.isSameDay(target.values!![index], values!![index])) {
                                return false
                            }
                        }
                        return true
                    }
                }

            }
            return super.equals(other)
        }

        fun isContain(time: Long): Boolean {
            values?.let {
                if (time >= values!!.first() && time <= values!!.last()) {
                    return true
                }
            }
            return false;
        }

        fun isHasToday(): Boolean {
            var cal = Calendar.getInstance()
            var cal1 = Calendar.getInstance()
            values?.let {
                for (time in values!!) {
                    cal1.timeInMillis = time
                    if (DateUtils.isSameDay(cal!!, cal1!!)) {
                        return true
                    }
                }
            }
            return false
        }
    }


    class CourseScheduleItem {

        var startTime: Long = 0
        var endTime: Long = 0

        var weeks: Array<ActivityItem?> = arrayOfNulls(7)

        init {
            for (i in 0 until 7) {
                var item = ActivityItem()
                item.state = 0
                weeks[i] = item
            }
        }

    }

    class ActivityItem {

        var state: Int = 0

        var content: String? = null

    }
}
