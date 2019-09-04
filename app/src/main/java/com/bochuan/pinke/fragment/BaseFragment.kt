package com.bochuan.pinke.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.common.fragment.BaseWorkFragment

abstract class BaseFragment : BaseWorkFragment() {


    protected var mActivity: BaseGomeWorkActivity? = null

    private var mContentView: View? = null

    open fun refreshData() {}

    abstract fun getLayoutID(): Int


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mActivity = activity as BaseGomeWorkActivity
    }

    open fun onBackPressed(): Boolean {
        return false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentView?.let { return this.mContentView }
        var id = getLayoutID();
        if (id > 0) {
            mContentView = inflater.inflate(id, null);
        }
        return mContentView
    }


}