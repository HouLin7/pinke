package com.bochuan.pinke.fragment

import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment() {

    abstract fun refreshData()

    open fun onBackPressed(): Boolean {
        return false
    }


}