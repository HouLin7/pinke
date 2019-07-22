package com.bochuan.pinke.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gome.applibrary.R
import com.gome.work.common.activity.BaseGomeWorkActivity

abstract class BaseFragment : Fragment() {

    private var mProgressDialog: ProgressDialog? = null

    protected var mActivity: BaseGomeWorkActivity? = null

    private var mContentView: View? = null

    abstract fun refreshData()

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

    fun showProgressDlg() {
        showProgressDlg("", true)
    }

    protected fun showProgressDlg(text: CharSequence, isCancelable: Boolean) {
        var text = text
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(activity)
        }
        if (TextUtils.isEmpty(text)) {
            text = getString(R.string.loading_hint)
        }
        mProgressDialog!!.setMessage(text)
        mProgressDialog!!.setCancelable(isCancelable)
        mProgressDialog!!.show()
    }

    fun dismissProgressDlg() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
        }
    }


}