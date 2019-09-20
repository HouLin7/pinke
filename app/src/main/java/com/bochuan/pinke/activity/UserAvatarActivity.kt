package com.bochuan.pinke.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.EventLog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bochuan.pinke.R
import com.gome.utils.ToastUtil
import com.gome.work.common.activity.BaseGomeWorkActivity
import com.gome.work.common.activity.PhotoPickerActivity
import com.gome.work.common.imageloader.ImageLoader
import com.gome.work.core.event.EventDispatcher
import com.gome.work.core.event.model.EventInfo
import com.gome.work.core.model.PostUserInfo
import com.gome.work.core.model.UploadFileResultInfo
import com.gome.work.core.model.UserInfo
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import com.gome.work.core.upload.IUploadListener
import com.liulishuo.okdownload.SpeedCalculator
import kotlinx.android.synthetic.main.activity_user_avatar.*
import java.io.File

/**
 * 跟人用户头像预览与修改
 */
class UserAvatarActivity : BaseGomeWorkActivity() {

    private var mUserInfo: UserInfo? = null
    private var isSelf: Boolean = false

    private var isUpdateSuccess = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUserInfo = intent.getSerializableExtra(EXTRA_DATA) as UserInfo
        setContentView(R.layout.activity_user_avatar)

        toolbar.bindActivity(this, "")
        initView()
        pb_progress.visibility = View.GONE
        isSelf = loginUserId == mUserInfo!!.id
    }

    override fun onBackPressed() {
        if (isUpdateSuccess) {
            setResult(Activity.RESULT_OK, null)
        }
        super.onBackPressed()
    }

    private fun initView() {
        ImageLoader.loadImage(this, mUserInfo!!.avatar, photo_view)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isSelf) {
            val item = menu.add("编辑")
            item.setIcon(R.mipmap.ic_title_more)
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        showImagePickerWindow(true, null)
//        var intent = Intent(this, PhotoPickerActivity::class.java);
//        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }


    override fun onImageGetResult(isSuccess: Boolean, uri: Uri?, file: File?) {
        if (isSuccess) {
            ImageLoader.loadImage(this, uri, photo_view)
            WebApi.getInstance().uploadFile(file, object : IUploadListener<UploadFileResultInfo>() {
                override fun onProcess(totalBytes: Long, transferBytes: Long, speed: SpeedCalculator) {
                    runOnUiThread {
                        val progress = transferBytes * 100 / totalBytes.toDouble()
                        pb_progress.progress = progress.toInt()
                    }
                }

                override fun onStart() {
                    runOnUiThread {
                        pb_progress.visibility = View.VISIBLE
                        pb_progress.progress = 0
                    }

                }

                override fun onEnd() {

                }

                override fun onError(code: String, message: String) {
                    pb_progress.visibility = View.GONE
                    pb_progress.progress = 0
                    ToastUtil.showToast(baseContext, message)
                }

                override fun onSuccess(result: UploadFileResultInfo) {
                    isUpdateSuccess = true
                    pb_progress.visibility = View.GONE
                    pb_progress.progress = 0
                    postAvatarInfo(result.url)

                }
            })
        }
    }

    private fun postAvatarInfo(fileUrl: String) {
        showProgressDlg()
        var postInfo = PostUserInfo()
        postInfo.id = loginUserId
        postInfo.avatar = fileUrl
        WebApi.getInstance().postUserInfo(postInfo, object : IResponseListener<String> {
            override fun onError(code: String?, message: String?) {
                ToastUtil.showToast(mActivity, message)
                dismissProgressDlg()
            }

            override fun onSuccess(result: String?) {
                ToastUtil.showToast(baseContext, "修改成功")
                dismissProgressDlg()

                var event = EventInfo.obtain(EventInfo.FLAG_LOGIN_USER_INFO_CHANGED)
                EventDispatcher.postEvent(event)
            }

        })
    }


}
