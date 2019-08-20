package com.bochuan.pinke.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import com.amap.api.location.AMapLocation
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.help.Tip
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.bochuan.pinke.R
import com.bochuan.pinke.util.AMapLocationManager
import com.gome.applibrary.activity.BaseActivity
import com.gome.utils.CommonUtils
import com.gome.utils.ToastUtil
import com.gome.work.common.KotlinViewHolder
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.common.adapter.BaseRecyclerAdapter
import com.gome.work.common.adapter.BaseViewHolder
import com.gome.work.core.model.RegionItem
import kotlinx.android.synthetic.main.activity_school_choose.*
import kotlinx.android.synthetic.main.adapter_location_list_item.*

/**
 * 学校选取
 */
class SchoolChooseActivity : BaseGomeWorkActivity() {

    companion object {
        const val REQUEST_CODE_CITY_SELECT = 1
    }

    private var mLocation: AMapLocation? = null

    private lateinit var mLocationManager: AMapLocationManager

    private var searchAdapter: SearchPoiAdapter? = null
    private var nearbyAdapter: PoiAdapter? = null
    private var newSelectCity: RegionItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_choose)
        getCustomToolbar(title_bar).bindActivity(this, "学校选择")

        mLocationManager = AMapLocationManager(mActivity)
        mLocation = mLocationManager.getLastLocation()

        layout_edit_city.setOnClickListener {
            var intent = Intent(mActivity, CitySelectActivity::class.java);
            startActivityForResult(intent, REQUEST_CODE_CITY_SELECT)
        }
        button_re_location.setOnClickListener {
            getLocation()
        }

        edit_address.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                cover_mask.visibility = View.VISIBLE
            } else {
                cover_mask.visibility = View.GONE
            }
        }
        edit_address.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (mLocation != null) {
                    var keyword = s.toString()
                    searchPoi(keyword)
                    recyclerView_search.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
        searchAdapter = SearchPoiAdapter(this)
        searchAdapter!!.setOnItemClickListener { parent, view, position, id ->
            var item = searchAdapter!!.getItem(position)
            if (!item.typeCode.startsWith("1412")) {
                ToastUtil.showToast(mActivity, "请选择学校地址")
                return@setOnItemClickListener
            }
//            if (item.poiID in arrayOf("141200", "141200", "141200","141200","141200",)) {
//
//            }
            var data = Intent()
            data.putExtra(BaseActivity.EXTRA_DATA, item.name)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
        nearbyAdapter = PoiAdapter(this)
        nearbyAdapter!!.setOnItemClickListener { parent, view, position, id ->
            var item = nearbyAdapter!!.getItem(position)
            var data = Intent()
            data.putExtra(BaseActivity.EXTRA_DATA, item.title)
            setResult(Activity.RESULT_OK, data)
            finish()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = nearbyAdapter
        recyclerView.visibility = View.VISIBLE

        recyclerView_search.layoutManager = LinearLayoutManager(this)
        recyclerView_search.adapter = searchAdapter
        recyclerView_search.visibility = View.GONE

        cover_mask.visibility = View.GONE
        getLocation()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CITY_SELECT) {
            if (resultCode == Activity.RESULT_OK) {
                var regionItem = data!!.getSerializableExtra(BaseActivity.EXTRA_DATA) as RegionItem
                tv_city.text = regionItem.name
                newSelectCity = regionItem
            }
        }
    }

    override fun onBackPressed() {
        if (cover_mask.visibility == View.VISIBLE) {
            cover_mask.visibility = View.GONE
            recyclerView_search.visibility = View.GONE
            dismissInputMethod()
        } else {
            super.onBackPressed()
        }
    }

    fun searchPoi(keyword: String) {
        var city = ""
        mLocation?.let {
            city = mLocation!!.cityCode
        }
        if (newSelectCity != null) {
            city = newSelectCity!!.name
        }
        val inputQuery = InputtipsQuery(keyword, city)
        inputQuery.cityLimit = true//限制在当前城市
        val inputTips = Inputtips(this, inputQuery)
        inputTips.setInputtipsListener { tipList, rCode ->
            if (rCode == 1000) {
                searchAdapter!!.setItemList(tipList)
            }
        }
        inputTips.requestInputtipsAsyn()

    }


    fun searchNearPoi(lat: Double, lon: Double, cityCode: String) {
        var query = PoiSearch.Query("", "141200", cityCode)
        query.pageSize = 10
        var poiSearch = PoiSearch(this, query)
        var ad = LatLonPoint(lat, lon)
        var bound = PoiSearch.SearchBound(ad, 500)
        poiSearch.bound = bound


        poiSearch.setOnPoiSearchListener(object : PoiSearch.OnPoiSearchListener {
            override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {
            }

            override fun onPoiSearched(p0: PoiResult?, p1: Int) {
                nearbyAdapter!!.setItemList(p0!!.pois)

            }
        })
        poiSearch.searchPOIAsyn()
        progressbar.visibility = View.GONE
    }


    public fun refreshLocationView(location: AMapLocation) {
        var text = location.street
        location.aoiName?.let {
            text += location.aoiName
        }
        tv_location_address.text = text
        tv_city.text = location.city
    }

    private fun getLocation() {
        if (!CommonUtils.isGPSOpen(this)) {
            showAlertDlg("请先打开手机定位开关")
            return;
        }
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION) { permission, isSuccess ->
            if (isSuccess) {
                mLocationManager.getLocation(object : AMapLocationManager.ILocationCallback {
                    override fun call(loc: AMapLocation) {
//                        var province = loc.province
//                        var city = loc.city
//                        var district = loc.district
//                        var cityCode = loc.cityCode
//                        var street = loc.street
//                        var adCode = loc.adCode
//                        var address = loc.address
                        mLocation = loc
                        refreshLocationView(loc)
                        searchNearPoi(loc.latitude, loc.longitude, loc.cityCode)
                    }
                })

            } else {

            }

        }
    }


    inner class PoiAdapter(fragmentActivity: FragmentActivity) : BaseRecyclerAdapter<PoiItem>(fragmentActivity) {
        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<PoiItem> {
            var view = layoutInflater.inflate(R.layout.adapter_location_list_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindMyViewHolder(holder: BaseViewHolder<PoiItem>?, dataItem: PoiItem?, position: Int) {
            var myHolder = holder as ViewHolder
            myHolder.bind(dataItem!!, position)
        }

        inner class ViewHolder(view: View) : KotlinViewHolder<PoiItem>(view) {
            override fun bind(t: PoiItem, position: Int) {
                var cityName = t.cityName
                tv_item_location.text = t.title
            }

        }

    }

    inner class SearchPoiAdapter(fragmentActivity: FragmentActivity) : BaseRecyclerAdapter<Tip>(fragmentActivity) {
        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<Tip> {
            var view = layoutInflater.inflate(R.layout.adapter_location_list_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindMyViewHolder(holder: BaseViewHolder<Tip>?, dataItem: Tip?, position: Int) {
            var myHolder = holder as ViewHolder
            myHolder.bind(dataItem!!, position)
        }

        inner class ViewHolder(view: View) : KotlinViewHolder<Tip>(view) {
            override fun bind(t: Tip, position: Int) {
                tv_item_location.text = t.name + " " + t.district
            }

        }

    }


}
