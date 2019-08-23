package com.bochuan.pinke.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.bochuan.pinke.R
import com.gome.applibrary.activity.BaseActivity
import com.gome.utils.ToastUtil
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.common.widget.BaseSelectPopWindow
import com.gome.work.common.widget.ListSelectPopWindow
import com.gome.work.core.model.CfgDicItem
import com.gome.work.core.model.PostUserInfo
import com.gome.work.core.model.SysCfgData
import com.gome.work.core.model.UserInfo
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import com.gome.work.core.persistence.UserCacheManager
import com.gome.work.core.utils.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_mine_info.*

/**
 * 我的资料
 */
class MineInfoActivity : BaseGomeWorkActivity(), UserCacheManager.IUserGetResultListener {

    companion object {
        const val REQUEST_CODE_NICKNAME = 100
        const val REQUEST_CODE_SCHOOLE = 101
    }

    lateinit var mUserInfo: UserInfo

    lateinit var selectGrade: CfgDicItem


    var sexList = SysCfgData.getSexCfgItems()
    lateinit var selectSex: CfgDicItem

    private var selectGradePopWindow: ListSelectPopWindow? = null

    private var selectSexPopWindow: ListSelectPopWindow? = null

    init {


    }

    override fun onResult(result: UserInfo) {
        mUserInfo = result;
        updateUI(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_info)
        getCustomToolbar(title_bar).bindActivity(this, "个人资料")

        mUserInfo = SharedPreferencesHelper.getAccessTokenInfo().userInfo
        if (mUserInfo == null) {
            return
        }

        layout_nickname.setOnClickListener {
            var intent = Intent(mActivity, EditTextActivity::class.java)
            intent.putExtra(EditTextActivity.EXTRA_TITLE, "修改昵称")
            startActivityForResult(intent, REQUEST_CODE_NICKNAME)
        }

        layout_avatar.setOnClickListener {
            var intent = Intent(mActivity, UserAvatarActivity::class.java)
            var accessToke = SharedPreferencesHelper.getAccessTokenInfo()
            intent.putExtra(BaseActivity.EXTRA_DATA, accessToke.userInfo)
            startActivity(intent)
        }

        layout_school.setOnClickListener {
            var intent = Intent(mActivity, SchoolChooseActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_SCHOOLE)
        }


        layout_grade.setOnClickListener {
            if (selectGradePopWindow != null) {
                selectGradePopWindow!!.showPopupWindow(it)
            } else {
                var popWindow = ListSelectPopWindow(mActivity, sysCfgData!!.grade)
                popWindow.showPopupWindow(it)
                popWindow.listener = object : BaseSelectPopWindow.OnCfgItemSelectListener {
                    override fun onSelect(dataList: List<CfgDicItem>) {
                        selectGrade = dataList[0]
                        tv_grade.text = selectGrade!!.name

                        var data = PostUserInfo()
                        data.id = mUserInfo.id
                        data.grade = selectGrade.id
                        WebApi.getInstance().postUserInfo(data, object : IResponseListener<String> {
                            override fun onError(code: String?, message: String?) {
                                ToastUtil.showToast(mActivity, message)
                            }

                            override fun onSuccess(result: String?) {
                                ToastUtil.showToast(mActivity, "修改年级成功")

                            }

                        })
                    }
                }
                selectGradePopWindow = popWindow
            }
        }


        layout_sex.setOnClickListener {
            if (selectSexPopWindow != null) {
                selectSexPopWindow!!.showPopupWindow(it)
            } else {
                var popWindow = ListSelectPopWindow(mActivity, sexList)
                popWindow.listener = object : BaseSelectPopWindow.OnCfgItemSelectListener {
                    override fun onSelect(dataList: List<CfgDicItem>) {
                        selectSex = dataList[0]
                        tv_sex.text = selectSex!!.name

                        var data = PostUserInfo()
                        data.id = mUserInfo.id
                        data.sex = selectSex.id
                        WebApi.getInstance().postUserInfo(data, object : IResponseListener<String> {
                            override fun onError(code: String?, message: String?) {
                                ToastUtil.showToast(mActivity, message)
                            }

                            override fun onSuccess(result: String?) {
                                ToastUtil.showToast(mActivity, "修改性别成功")

                            }

                        })
                    }
                }
                popWindow.showPopupWindow(it)
                selectSexPopWindow = popWindow
            }

        }

        var result = UserCacheManager.get(this).getUserInfo(mUserInfo!!.id)
        if (result != null) {
            updateUI(result)
            mUserInfo = result
        }

        UserCacheManager.get(this).addListener(this)


    }


    private fun updateUI(userInfo: UserInfo) {
        if (userInfo == null) {
            return
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_SCHOOLE -> {
                    tv_school.text = data!!.getStringExtra(BaseActivity.EXTRA_DATA)
                    var data = PostUserInfo()
                    data.id = mUserInfo.id
                    data.school = tv_school.text.toString()
                    WebApi.getInstance().postUserInfo(data, object : IResponseListener<String> {
                        override fun onError(code: String?, message: String?) {
                            ToastUtil.showToast(mActivity, message)
                        }

                        override fun onSuccess(result: String?) {
                            ToastUtil.showToast(mActivity, "修改学校成功")

                        }

                    })
                }

                REQUEST_CODE_NICKNAME -> {
                    tv_nickname.text = data!!.getStringExtra(BaseActivity.EXTRA_DATA)
                    var data = PostUserInfo()
                    data.id = mUserInfo.id
                    data.nickname = tv_nickname.text.toString()
                    WebApi.getInstance().postUserInfo(data, object : IResponseListener<String> {
                        override fun onError(code: String?, message: String?) {
                            ToastUtil.showToast(mActivity, message)
                        }

                        override fun onSuccess(result: String?) {
                            ToastUtil.showToast(mActivity, "修改昵称成功")

                        }

                    })
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        UserCacheManager.get(this).removeListener(this)
    }


}
