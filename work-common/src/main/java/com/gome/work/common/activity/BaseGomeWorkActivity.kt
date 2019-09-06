package com.gome.work.common.activity

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.text.TextUtils
import android.view.View
import com.android.common.media.CameraHelper
import com.gome.applibrary.activity.BaseActivity
import com.gome.utils.ContentUriUtils
import com.gome.utils.FileCacheUtils
import com.gome.utils.PictureUtils
import com.gome.work.common.R
import com.gome.work.common.utils.ActivityStack
import com.gome.work.common.widget.BaseMenuPopupWindow
import com.gome.work.common.widget.MenuMenuPopup
import com.gome.work.common.widget.MyToolbarView
import com.gome.work.common.widget.SlideFromBottomPopWindowMenu
import com.gome.work.core.Constants
import com.gome.work.core.SystemFramework
import com.gome.work.core.event.BaseEventConsumer
import com.gome.work.core.event.IEventConsumer
import com.gome.work.core.event.model.EventInfo
import com.gome.work.core.model.SysCfgData
import com.gome.work.core.persistence.DaoUtil
import com.gome.work.core.utils.GsonUtil
import com.gome.work.core.utils.SharedPreferencesHelper
import io.reactivex.disposables.Disposable
import razerdp.basepopup.BasePopupWindow
import java.io.File

open class BaseGomeWorkActivity : BaseActivity() {

    /**
     * A global file reference to link select image file from photo album.
     */
    protected var mFileSelect: File? = null

    /**
     * A global reference to to link image from camera.
     */
    private var mFileCamera: File? = null


    /**
     * 调用系统截图功能的输出uri
     */
    private var mImageCropOutputUri: Uri? = null

    private var mImageCropOutputFile: File? = null

    /**
     * 选取图片时候的参数，标识是否采用剪辑
     */
    private var isNeedCrop: Boolean = false
    /**
     * 标记弹出的图片选择界面，是否做出了选择。
     */
    private var isDoneSelect = false


    private var mEventConsumerHolder: IEventConsumer? = null

    private var mSlideFromBottomPopup: SlideFromBottomPopWindowMenu? = null

    protected lateinit var mDaoUtil: DaoUtil

    protected lateinit var mActivity: BaseGomeWorkActivity

    private val mDisposableList = ArrayList<Disposable>()

    var sysCfgData: SysCfgData? = null
        @Synchronized
        get() {
            if (field == null) {
                var rawData = SharedPreferencesHelper.getString(Constants.PreferKeys.SYS_CONFIG_DATA)
                if (!TextUtils.isEmpty(rawData)) {
                    field = GsonUtil.jsonToBean(rawData, SysCfgData::class.java)
                }
            }
            return field
        }

//    init {
//        var rawData = SharedPreferencesHelper.getString(Constants.PreferKeys.SYS_CONFIG_DATA)
//        sysCfgData = GsonUtil.jsonToBean(rawData, SysCfgData::class.java)
//    }

    val isLogined: Boolean
        get() = !TextUtils.isEmpty(SharedPreferencesHelper.getAccessToken())


    val loginUserId: String
        get() {
            val bean = SharedPreferencesHelper.getAccessTokenInfo()
            return if (bean != null) {
                if (bean.userInfo == null) "" else bean.userInfo.id
            } else ""
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        ActivityStack.getInstance().addActivity(this)
        //        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        //            StatusBarUtil.setTranslucentForCoordinatorLayout(this, 0);
        //        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        mDaoUtil = DaoUtil(this)
        mActivity = this

        //        ToastUtil.showToast(this, this.getClass().getSimpleName());

    }


    fun getCustomToolbar(view: View): MyToolbarView {
        return view.findViewById(R.id.my_tool_bar)
    }


    fun tagRxTask(item: Disposable) {
        mDisposableList.add(item)
    }

    fun checkUpdate() {
        if (SystemFramework.Environment.RELEASE == SystemFramework.getInstance().environment) {
            super.checkUpdate("app0001")
        }
    }

    fun observeEvents(vararg flags: Int) {
        if (mEventConsumerHolder != null) {
            mEventConsumerHolder!!.detach()
        }

        mEventConsumerHolder = object : BaseEventConsumer(this, *flags) {

            override fun handleEvent(event: EventInfo) {
                this@BaseGomeWorkActivity.handleEvent(event)
            }
        }
        mEventConsumerHolder!!.attach()
    }


    protected open fun handleEvent(event: EventInfo) {}


    override fun onDestroy() {
        super.onDestroy()
        ActivityStack.getInstance().removeActivity(this)
        if (mEventConsumerHolder != null) {
            mEventConsumerHolder!!.detach()
        }

        for (item in mDisposableList) {
            if (!item.isDisposed) {
                item.dispose()
            }
        }
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportFinishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }

    //    public boolean isRunningForeground() {
    //        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
    //        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
    //        // 枚举进程
    //        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
    //            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
    //                if (appProcessInfo.processName.equals(this.getApplicationInfo().processName)) {
    //                    return true;
    //                }
    //            }
    //        }
    //        return false;
    //    }


    protected open fun onImageGetResult(isSuccess: Boolean, uri: Uri?, file: File?) {}


    private fun getImageContentUri(imageFile: File): Uri? {
        val filePath = imageFile.absolutePath
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID),
            MediaStore.Images.Media.DATA + "=? ",
            arrayOf(filePath), null
        )

        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
            val baseUri = Uri.parse("content://media/external/images/media")
            return Uri.withAppendedPath(baseUri, "" + id)
        } else {
            if (imageFile.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                return null
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_CAMERA_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                if (!isNeedCrop) {
                    val filePath = handlePickedImage(mFileCamera!!.absolutePath)
                    onImageGetResult(true, getUriForFile(filePath), File(filePath))
                } else {
                    val filePath = handlePickedImage(mFileCamera!!.absolutePath)
                    mFileCamera = File(filePath)
//                    startImageCropIntent(getImageContentUri(mFileCamera!!))
                    var intent = Intent(mActivity, ImageCropActivity::class.java)
                    intent.putExtra(EXTRA_DATA, mFileCamera?.absolutePath)
                    startActivityForResult(intent, REQUEST_CODE_IMAGE_CROP_SELF)
                }
            } else {
                onImageGetResult(false, null, null)
            }
            REQUEST_CODE_SELECT_PHOTO -> if (data != null && resultCode == Activity.RESULT_OK) {
                val originalUri = data.data
                var filePath = ContentUriUtils.getPath(this, originalUri)
                if (!isNeedCrop) {
                    filePath = handlePickedImage(filePath)
                }
                val result = getUriForFile(filePath)
                if (!isNeedCrop) {
                    onImageGetResult(true, result, File(filePath))
                } else {
//                    startImageCropIntent(originalUri)
                    var intent = Intent(mActivity, ImageCropActivity::class.java)
                    intent.putExtra(EXTRA_DATA, filePath)
                    startActivityForResult(intent, REQUEST_CODE_IMAGE_CROP_SELF)
                }
            } else {
                onImageGetResult(false, null, null)
            }
            REQUEST_CODE_IMAGE_CROP -> if (resultCode == Activity.RESULT_OK) {
                onImageGetResult(true, mImageCropOutputUri, mImageCropOutputFile)
            } else {
                onImageGetResult(false, null, null)
            }

            REQUEST_CODE_IMAGE_CROP_SELF ->
                if (resultCode == Activity.RESULT_OK) {
                    var filePath = data!!.getStringExtra(EXTRA_DATA)
                    var fielUri = getUriForFile(filePath)
                    onImageGetResult(true, fielUri, File(filePath))
                } else {
                    onImageGetResult(false, null, null)
                }

            else -> {
            }
        }
    }

    protected fun startImageCropIntent(inputUri: Uri?) {
        val file = CameraHelper.getPublicStorageOutputMediaFile(baseContext, CameraHelper.MEDIA_TYPE_IMAGE)
        mImageCropOutputUri = Uri.fromFile(file)
        mImageCropOutputFile = file

        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(inputUri, "image/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        intent.putExtra("crop", "true")
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        intent.putExtra("outputX", 500)
        intent.putExtra("outputY", 500)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCropOutputUri)
        intent.putExtra("return-data", false)
        //        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG);
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //        }
        startActivityForResult(intent, REQUEST_CODE_IMAGE_CROP)

        //        Crop.of(inputUri, mImageCropUri).asSquare().start(this, REQUEST_CODE_IMAGE_CROP);
    }

    /**
     * Handle the picture file which get from camera or album.
     *
     * @param fileName
     */
    protected fun handlePickedImage(fileName: String?): String? {
        var fileName = fileName
        var myBitmap = PictureUtils.getSmallBitmap(fileName)
        val angle = PictureUtils.getAngle(fileName)
        if (angle != 0) {
            val m = Matrix()
            val width = myBitmap.width
            val height = myBitmap.height
            m.setRotate(angle.toFloat()) // 旋转angle度
            myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, width, height, m, true)// 从新生成图片
        }
        fileName = outputBitmap(myBitmap)
        return fileName
    }

    /**
     * Cache a special bitmap and return cached path
     *
     * @param data
     * @return
     */
    protected fun outputBitmap(data: Bitmap): String {
        try {
            val rawData = PictureUtils.Bitmap2Bytes(data)
            val file = CameraHelper.getOutputMediaFile(this, CameraHelper.MEDIA_TYPE_IMAGE)
            return FileCacheUtils.cache(this, rawData, file)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    protected fun getUriForFile(file: File?): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(baseContext, baseContext.packageName + ".fileProvider", file!!)
        } else {
            Uri.fromFile(file)
        }
    }

    private fun getUriForFile(filePath: String?): Uri {
        return getUriForFile(File(filePath))
    }

    fun showImagePickerWindow() {
        this.showImagePickerWindow(false, null)
    }

    /**
     * Show a pop window , ask user select the path from getting image.
     */
    @JvmOverloads
    fun showImagePickerWindow(isNeedCrop: Boolean, listener: DialogInterface.OnCancelListener?, isOnlyCamera: Boolean = false) {
        this.isNeedCrop = isNeedCrop
        isDoneSelect = false
        if (mSlideFromBottomPopup == null) {
            val menus = ArrayList<String>()
            menus.add("拍照")
            if (!isOnlyCamera) {
                menus.add("相册")
            }
            mSlideFromBottomPopup = SlideFromBottomPopWindowMenu(this@BaseGomeWorkActivity, menus)
            mSlideFromBottomPopup!!.setOnMenuItemClickListener { position ->
                when (position) {
                    0 -> {
                        isDoneSelect = true
                        requestPermission(Manifest.permission.CAMERA) { permission, isSuccess ->
                            if (isSuccess) {
                                val intent_camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                mFileCamera = CameraHelper.getOutputMediaFile(baseContext, CameraHelper.MEDIA_TYPE_IMAGE)
                                val contentUri = getUriForFile(mFileCamera)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    intent_camera.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                                    //                                            intent_camera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    //                                                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                    //
                                }
                                intent_camera.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
                                startActivityForResult(intent_camera, REQUEST_CODE_CAMERA_PHOTO)
                            } else {
                                listener?.onCancel(null)
                            }
                        }
                        mSlideFromBottomPopup!!.dismiss()
                    }
                    1 -> {
                        isDoneSelect = true
                        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE) { permission, isSuccess ->
                            if (isSuccess) {
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                    startActivityForResult(
                                        Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                                        REQUEST_CODE_SELECT_PHOTO
                                    )
                                } else {
                                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                                    intent.type = "image/*"
                                    startActivityForResult(intent, REQUEST_CODE_SELECT_PHOTO)
                                }

                            } else {
                                listener?.onCancel(null)
                            }
                        }
                        mSlideFromBottomPopup!!.dismiss()
                    }
                    else -> {
                    }
                }
            }
        }

        mSlideFromBottomPopup!!.onDismissListener = object : BasePopupWindow.OnDismissListener() {
            override fun onDismiss() {
                if (!isDoneSelect) {
                    listener?.onCancel(null)
                }
            }
        }
        mSlideFromBottomPopup!!.showPopupWindow()

    }

    fun sshowPopWindowMenu(anchorView: View, menuList: List<String>, listener: BaseMenuPopupWindow.OnMenuItemClickListener?) {
        val menu = MenuMenuPopup(this, menuList)
        menu.showPopupWindow(anchorView)
        menu.setOnMenuItemClickListener { position ->
            if (listener != null) {
                listener!!.onMenuItemClick(position)
                menu.dismiss()
            }
        }
    }

    companion object {

        private val TAG = BaseGomeWorkActivity::class.java.simpleName

        protected val REQUEST_CODE_SELECT_PHOTO = 20000

        protected val REQUEST_CODE_CAMERA_PHOTO = 20001

        protected val REQUEST_CODE_IMAGE_CROP = 20002

        protected val REQUEST_CODE_IMAGE_CROP_SELF = 20003
    }


}
