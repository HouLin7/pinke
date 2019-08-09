package com.bochuan.pinke.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DividerItemDecoration
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import com.baidu.location.BDLocation
import com.bochuan.pinke.R
import com.bochuan.pinke.model.CityItem
import com.bochuan.pinke.util.BDLocationManager
import com.gome.utils.CommonUtils
import com.gome.work.common.KotlinViewHolder
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.common.adapter.BaseRecyclerAdapter
import com.gome.work.common.adapter.BaseViewHolder
import com.gome.work.common.divider.CustomNewsDivider
import com.gome.work.common.widget.LinearLayoutManagerWithSmoothScroller
import com.gome.work.common.widget.indexview.indexbar.widget.IndexBar
import com.gome.work.core.Constants
import com.gome.work.core.model.RegionItem
import com.gome.work.core.utils.GsonUtil
import com.gome.work.core.utils.SharedPreferencesHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_city_select.*
import kotlinx.android.synthetic.main.adapter_city_list_item.*
import java.util.*
import java.util.concurrent.Callable
import kotlin.Comparator
import kotlin.collections.ArrayList


/**
 * 城市选择
 */
class CitySelectActivity : BaseGomeWorkActivity() {

    var mAdapter: MyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_select)
        getCityData()
        getLocation()
        iv_back.setOnClickListener { onBackPressed() }
        recyclerView.layoutManager = LinearLayoutManagerWithSmoothScroller(this);
        recyclerView.addItemDecoration(
            CustomNewsDivider(
                mActivity,
                DividerItemDecoration.HORIZONTAL,
                2,
                R.color.divider_color
            )
        )
        mAdapter = MyAdapter(this)
        recyclerView.adapter = mAdapter

        mAdapter!!.setOnItemClickListener { parent, view, position, id ->
            var cityItem = mAdapter!!.getItem(position)
            var data = Intent()
            data.putExtra(EXTRA_DATA, cityItem.regionItem)
            setResult(Activity.RESULT_OK, data)
            finish()
        }

        ed_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                var keyword = s.toString()
                mAdapter!!.filter.filter(keyword)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }


        })
        sidebar.setmSourceDatas(mAdapter!!.allItems)

        sidebar.setmOnIndexPressedListener(object : IndexBar.onIndexPressedListener {
            override fun onIndexPressed(index: Int, text: String?) {
                var pos = findPositionByLetter(text!!)
                if (pos > -1) {
//                    recyclerView.scrollToPosition(index)
                    var llm = recyclerView.layoutManager
                    llm!!.scrollToPosition(pos)
                }
            }

            override fun onMotionEventEnd() {
            }

        })
    }

    override fun onBackPressed() {
        dismissInputMethod()
        super.onBackPressed()
    }

    private fun findPositionByLetter(letter: String): Int {
        var dataList = mAdapter!!.allItems
        for (item in dataList) {
            if (item.regionItem.firstChar.equals(letter)) {
                return dataList.indexOf(item)
            }
        }
        return -1
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
                val locationManager = BDLocationManager(mActivity);
                locationManager.getLocation(object : BDLocationManager.ILocationCallback {
                    override fun call(loc: BDLocation) {
                        tv_location_city.text = loc.city
                    }
                })
            } else {

            }

        }
    }

    private fun getCityData() {
        tagRxTask(Observable.fromCallable(Callable<List<CityItem>> {
            return@Callable getALlCityData()

        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                mAdapter!!.setItemList(result)

            })
    }

    private fun getALlCityData(): List<CityItem> {
        var dataStr = SharedPreferencesHelper.getString(Constants.PreferKeys.CITY_DATA)
        var regionList = ArrayList<RegionItem>()
        if (!TextUtils.isEmpty(dataStr)) {
//                var tokenInfo = object : TypeToken<List<RegionItem>>() {}
            regionList = GsonUtil.jsonToList(dataStr, RegionItem::class.java) as ArrayList<RegionItem>
        }
        var cityList = ArrayList<CityItem>()

        var topLevelCity = listOf("北京", "上海", "广州", "重庆")
        for (item1 in regionList) {
            if (item1.name in topLevelCity) {
                cityList.add(CityItem(item1))
                continue
            }
            for (item2 in item1.children) {
                cityList.add(CityItem(item2))
                for (item3 in item2.children) {
//                    cityList.add(item3)
                }
            }
        }

        Collections.sort(cityList, Comparator<CityItem> { o1, o2 ->
            return@Comparator o1.regionItem.firstChar.compareTo(o2.regionItem.firstChar)
        })

        return cityList
    }

    inner class MyAdapter(activity: FragmentActivity) : BaseRecyclerAdapter<CityItem>(activity) {
        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<CityItem> {
            var view: View = layoutInflater.inflate(R.layout.adapter_city_list_item, parent, false);
            return MyViewHolder(view)
        }

        override fun onBindMyViewHolder(holder: BaseViewHolder<CityItem>?, dataItem: CityItem?, position: Int) {
            var viewHolder = holder as KotlinViewHolder<CityItem>
            viewHolder.bind(dataItem!!, position)
        }

        override fun matchFilter(t: CityItem?, keyword: String?): Boolean {
            return t!!.regionItem.name.contains(keyword!!);
        }

        inner class MyViewHolder(view: View) : KotlinViewHolder<CityItem>(view) {

            override fun bind(t: CityItem, position: Int) {
                var dataList = mAdapter!!.allItems

                tv_city_name.text = t.regionItem.name
                tv_first_letter.text = t.regionItem.firstChar
                if (position == 0) {
                    tv_first_letter.visibility = View.VISIBLE
                } else if (!dataList[position].regionItem.firstChar.equals(dataList[position - 1].regionItem.firstChar)) {
                    tv_first_letter.visibility = View.VISIBLE
                } else {
                    tv_first_letter.visibility = View.GONE
                }
            }
        }

    }

}
