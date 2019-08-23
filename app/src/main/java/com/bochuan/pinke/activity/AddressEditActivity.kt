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
import com.gome.work.common.KotlinViewHolder
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.common.adapter.BaseRecyclerAdapter
import com.gome.work.common.adapter.BaseViewHolder
import com.gome.work.core.model.AddressItem
import com.gome.work.core.model.RegionItem
import kotlinx.android.synthetic.main.activity_adress_edit.*
import kotlinx.android.synthetic.main.adapter_address_category_item.*
import kotlinx.android.synthetic.main.adapter_location_list_item.*

/**
 * 地址编辑界面
 */
class AddressEditActivity : BaseGomeWorkActivity() {

    companion object {
        const val REQUEST_CODE_CITY_SELECT = 1
    }

    var mLocation: AMapLocation? = null

    lateinit var mLocationManager: AMapLocationManager

    private var searchAdapter: SearchPoiAdapter? = null

    private var categoryAdapter: AddressCategoryAdapter? = null

    private var newSelectCity: RegionItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adress_edit)
        getCustomToolbar(title_bar).bindActivity(this, "选择可上课地址")

        mLocationManager = AMapLocationManager(mActivity)

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

        categoryAdapter = AddressCategoryAdapter(this)
        categoryAdapter!!.setItemList(arrayListOf(0, 1))

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = categoryAdapter
        recyclerView.visibility = View.VISIBLE

        recyclerView_search.layoutManager = LinearLayoutManager(this)
        recyclerView_search.adapter = searchAdapter
        recyclerView_search.visibility = View.GONE

        cover_mask.visibility = View.GONE

        searchAdapter!!.setOnItemClickListener { parent, view, position, id ->
            if (mLocation != null) {
                var data = Intent()
                var tip = searchAdapter!!.getItem(position)
                mLocation!!.poiName = tip.name
                mLocation!!.latitude = tip.point.latitude
                mLocation!!.longitude = tip.point.longitude
                var addressInfo = AMapLocationManager.toAddressItem(mLocation!!)

                data.putExtra(EXTRA_DATA, addressInfo)
                setResult(Activity.RESULT_OK, data)
                finish()
            }
        }

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


    fun searchNearPoi(lat: Double, lon: Double) {
        var query = PoiSearch.Query("", "120300", "北京")
        query.pageSize = 10
        var poiSearch = PoiSearch(this, query)
        var ad = LatLonPoint(lat, lon)
        var bound = PoiSearch.SearchBound(ad, 500)
        poiSearch.bound = bound


        poiSearch.setOnPoiSearchListener(object : PoiSearch.OnPoiSearchListener {
            override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {
            }

            override fun onPoiSearched(p0: PoiResult?, p1: Int) {
                categoryAdapter!!.nearbyAdapter.setItemList(p0!!.pois)

            }
        })
        poiSearch.searchPOIAsyn()
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
                        var province = loc.province
                        var city = loc.city
                        var district = loc.district
                        var cityCode = loc.cityCode
                        var street = loc.street
                        var adCode = loc.adCode
                        var address = loc.address
                        mLocation = loc
                        refreshLocationView(loc)
                        searchNearPoi(loc.latitude, loc.longitude)
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
                tv_item_location.text = t.title + " " + cityName
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

    inner class AddressCategoryAdapter(fragmentActivity: FragmentActivity) : BaseRecyclerAdapter<Int>(fragmentActivity) {

        var nearbyAdapter = PoiAdapter(fragmentActivity)
        var myAddressAdapter = MyAddressAdapter(fragmentActivity)


        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<Int> {
            var view = layoutInflater.inflate(R.layout.adapter_address_category_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindMyViewHolder(holder: BaseViewHolder<Int>?, dataItem: Int?, position: Int) {
            var myHolder = holder as ViewHolder
            myHolder.bind(dataItem!!, position)
        }

        inner class ViewHolder(view: View) : KotlinViewHolder<Int>(view) {

            init {
                recyclerView_child.layoutManager = LinearLayoutManager(mActivity);
            }

            override fun bind(t: Int, position: Int) {
                if (t == 0) {
                    recyclerView_child.adapter = myAddressAdapter
                    tv_category.text = "常用地址"
                } else {
                    recyclerView_child.adapter = nearbyAdapter
                    tv_category.text = "附近地址"
                }
            }

        }


    }

    inner class MyAddressAdapter(fragmentActivity: FragmentActivity) : BaseRecyclerAdapter<AddressItem>(fragmentActivity) {
        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<AddressItem> {
            var view = layoutInflater.inflate(R.layout.adapter_address_list_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindMyViewHolder(holder: BaseViewHolder<AddressItem>?, dataItem: AddressItem?, position: Int) {
            var myHolder = holder as ViewHolder
            myHolder.bind(dataItem!!, position)
        }

        inner class ViewHolder(view: View) : KotlinViewHolder<AddressItem>(view) {
            override fun bind(t: AddressItem, position: Int) {
            }

        }

    }


}
