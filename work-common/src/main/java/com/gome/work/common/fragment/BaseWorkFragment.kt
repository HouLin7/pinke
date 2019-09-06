package com.gome.work.common.fragment

import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import com.gome.applibrary.activity.BaseActivity
import com.gome.utils.ToastUtil
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.core.event.BaseEventConsumer
import com.gome.work.core.event.IEventConsumer
import com.gome.work.core.event.model.EventInfo
import com.gome.work.core.utils.SharedPreferencesHelper

open class BaseWorkFragment : Fragment() {
    private var mEventConsumerHolder: IEventConsumer? = null
    protected var mBaseHandler = Handler()

    val myActivity: BaseGomeWorkActivity?
        get() = activity as BaseGomeWorkActivity?


    val loginUserId: String
        get() {
            val bean = SharedPreferencesHelper.getAccessTokenInfo()
            return if (bean != null) {
                if (bean.userInfo == null) "" else bean.userInfo.id
            } else ""
        }


    fun showProgressDlg() {
        if (myActivity != null) {
            myActivity!!.showProgressDlg()
        }
    }

    fun dismissProgressDlg() {
        if (myActivity != null) {
            myActivity!!.dismissProgressDlg()
        }
    }


    fun setToolbar(toolbar: Toolbar) {
        val appCompatActivity = activity as AppCompatActivity?
        appCompatActivity!!.setSupportActionBar(toolbar)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()

    }

    fun showToast(message: CharSequence) {
        if (isAdded && activity != null) {
            ToastUtil.showToast(activity, message)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (mEventConsumerHolder != null) {
            mEventConsumerHolder!!.detach()
        }
    }

    fun observeEvents(vararg flags: Int) {
        if (mEventConsumerHolder != null) {
            mEventConsumerHolder!!.detach()
        }

        mEventConsumerHolder = object : BaseEventConsumer(activity, *flags) {

            override fun handleEvent(event: EventInfo) {
                this@BaseWorkFragment.handleEvent(event)
            }
        }
        mEventConsumerHolder!!.attach()
    }

    protected open fun handleEvent(event: EventInfo) {

    }


    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return false
    }


    fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return false
    }

    companion object {

        val EXTRA_DATA = BaseActivity.EXTRA_DATA
    }
}
