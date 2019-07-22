package com.bochuan.pinke.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import com.bochuan.pinke.R
import com.gome.work.common.KotlinViewHolder
import com.gome.work.common.adapter.BaseRecyclerAdapter
import com.gome.work.common.divider.CustomNewsDivider
import com.gome.work.common.utils.UiUtils
import com.gome.work.core.model.CourseScheduleItem
import com.gome.work.core.utils.DateUtils
import kotlinx.android.synthetic.main.adapter_course_schedule_list_item.*
import kotlinx.android.synthetic.main.fragment_course_schedule.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class CourseScheduleFragment : BaseFragment() {

    var mAdapter: Adapter? = null;

    var nowWeekHeaderData: WeekHeaderItem? = null

    var currWeekHeaderData: WeekHeaderItem = getWeekHeaderData(getFirstDayOfWeek())

    override fun getLayoutID(): Int {
        return R.layout.fragment_course_schedule
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    fun showDatePickerDlg(cal: Calendar, listener: DatePickerDialog.OnDateSetListener) {
        var year = cal.get(Calendar.YEAR);
        var month = cal.get(Calendar.MONTH)
        var dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(activity!!, listener, year, month, dayOfMonth)
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
    private fun getWeekValueData(intervalTime: Int): List<CourseScheduleItem> {
        var result = ArrayList<CourseScheduleItem>()
        var calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        var random = Random()
        var startTime = calendar.timeInMillis;
        var offsetByMillis = TimeUnit.MINUTES.toMillis(intervalTime.toLong())
        for (index in 0 until 15) {
            var item = CourseScheduleItem()
            item.startTime = startTime + index * offsetByMillis
            item.endTime = startTime + (index + 1) * offsetByMillis
            result.add(item)
            var i = random.nextInt(6)
            item.weeks[i].state = 1
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
        var intervalTime = 0
        when (radio_group.checkedRadioButtonId) {
            R.id.radio_button_half_hour -> intervalTime = 30
            R.id.radio_button_hour -> intervalTime = 60
        }

        radio_group.setOnCheckedChangeListener { group, checkedId ->
            var value = 0
            when (checkedId) {
                R.id.radio_button_half_hour -> value = 30
                R.id.radio_button_hour -> value = 60
            }

            var dataList = getWeekValueData(value)
            mAdapter!!.setItemList(dataList)

        }

        var dataList = getWeekValueData(intervalTime)
        mAdapter!!.setItemList(dataList)


        updateHeaderView(currWeekHeaderData.values!!)
    }

    override fun refreshData() {

    }

    inner class Adapter(activity: FragmentActivity?) : BaseRecyclerAdapter<CourseScheduleItem>(activity) {

        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            var view: View = layoutInflater.inflate(R.layout.adapter_course_schedule_list_item, parent, false);
            return MyViewHolder(view);
        }

        override fun onBindMyViewHolder(
            holder: RecyclerView.ViewHolder?,
            dataItem: CourseScheduleItem?,
            position: Int
        ) {
            var myViewholder: MyViewHolder = holder as MyViewHolder
            myViewholder.bind(dataItem!!)

        }

        inner class MyViewHolder(view: View) : KotlinViewHolder<CourseScheduleItem>(view) {

            var dateFormat = SimpleDateFormat("HH:mm")
            override fun bind(t: CourseScheduleItem) {

                var strStart = dateFormat.format(Date(t.startTime))
                var strEnd = dateFormat.format(Date(t.endTime))
                tv_date_dsc.setText(strStart + "-" + strEnd);

                var index = 0
                for (item in t.weeks) {

                    var targetView: TextView? = null
                    when (index) {
                        0 -> targetView = tv_week0
                        1 -> targetView = tv_week1
                        2 -> targetView = tv_week2
                        3 -> targetView = tv_week3
                        4 -> targetView = tv_week4
                        5 -> targetView = tv_week5
                        6 -> targetView = tv_week6
                    }
                    targetView?.let {
                        if (item.state == 0) {
                            targetView.setBackgroundColor(UiUtils.getColor(R.color.white))
                        } else {
                            targetView.setBackgroundColor(UiUtils.getColor(R.color.app_red))
                        }
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


}
