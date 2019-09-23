package com.bochuan.pinke.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bochuan.pinke.R
import com.bochuan.pinke.widget.ScheduleTimeSelectPopWindow
import com.gome.work.common.KotlinViewHolder
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.common.adapter.BaseRecyclerAdapter
import com.gome.work.common.adapter.BaseViewHolder
import com.gome.work.core.model.CfgDicItem
import kotlinx.android.synthetic.main.activity_schedule_edit.*
import kotlinx.android.synthetic.main.adapter_color_list_item.*


/**
 * 日程添加、修改
 */
class ScheduleEditActivity : BaseGomeWorkActivity() {

    private var mScheduleTimeSelecterPopWindow: ScheduleTimeSelectPopWindow? = null
        @Synchronized
        get() {
            if (field == null) {
                mScheduleTimeSelecterPopWindow = ScheduleTimeSelectPopWindow(this)
            }
            return field
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_edit)

        tv_add_time.setOnClickListener {
            mScheduleTimeSelecterPopWindow!!.showPopupWindow()
        }

        recyclerView_color.layoutManager = GridLayoutManager(this, 5, RecyclerView.VERTICAL, false)
        recyclerView_color.adapter = ColorAdapter(this)

    }


    inner class ColorAdapter(activity: BaseGomeWorkActivity) : BaseRecyclerAdapter<CfgDicItem>(activity, sysCfgData!!.colors) {

        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<CfgDicItem> {
            var contentView = layoutInflater.inflate(R.layout.adapter_color_list_item, null)
            return ViewHolder(contentView)
        }

        override fun onBindMyViewHolder(holder: BaseViewHolder<CfgDicItem>?, dataItem: CfgDicItem?, position: Int) {
            var myViewHolder = holder as ViewHolder
            myViewHolder.bind(dataItem, position)
        }

        inner class ViewHolder(view: View) : KotlinViewHolder<CfgDicItem>(view) {

            override fun bind(t: CfgDicItem?, position: Int) {
                super.bind(t, position)
                var colorValue = "#" + t!!.name
                iv_schedule_color.setBackgroundColor(Color.parseColor(colorValue))

            }

        }


    }


}
