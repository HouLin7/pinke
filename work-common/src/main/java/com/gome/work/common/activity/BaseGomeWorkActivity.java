
package com.gome.work.common.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;

import com.android.common.media.CameraHelper;
import com.gome.applibrary.activity.BaseActivity;
import com.gome.utils.ContentUriUtils;
import com.gome.utils.FileCacheUtils;
import com.gome.utils.PictureUtils;
import com.gome.work.common.databinding.CustomToolbarBinding;
import com.gome.work.common.utils.ActivityStack;
import com.gome.work.common.widget.MenuPopup;
import com.gome.work.common.widget.MyBasePopupWindow;
import com.gome.work.common.widget.SlideFromBottomPopup;
import com.gome.work.core.SystemFramework;
import com.gome.work.core.event.BaseEventConsumer;
import com.gome.work.core.event.IEventConsumer;
import com.gome.work.core.event.model.EventInfo;
import com.gome.work.core.model.AccessTokenInfo;
import com.gome.work.core.persistence.DaoUtil;
import com.gome.work.core.utils.SharedPreferencesHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import razerdp.basepopup.BasePopupWindow;

public class BaseGomeWorkActivity extends BaseActivity {

    private static final String TAG = BaseGomeWorkActivity.class.getSimpleName();

    protected final static int REQUEST_CODE_SELECT_PHOTO = 20000;

    protected final static int REQUEST_CODE_CAMERA_PHOTO = 20001;

    protected final static int REQUEST_CODE_IMAGE_CROP = 20002;

    /**
     * A global file reference to link select image file from photo album.
     */
    protected File mFileSelect;

    /**
     * A global reference to to link image from camera.
     */
    private File mFileCamera;


    /**
     * 调用系统截图功能的输出uri
     */
    private Uri mImageCropOutputUri;

    private File mImageCropOutputFile;

    /**
     * 选取图片时候的参数，标识是否采用剪辑
     */
    private boolean isNeedCrop;
    /**
     * 标记弹出的图片选择界面，是否做出了选择。
     */
    private boolean isDoneSelect = false;


    private IEventConsumer mEventConsumerHolder;

    private SlideFromBottomPopup mSlideFromBottomPopup;

    protected DaoUtil mDaoUtil;

    protected BaseGomeWorkActivity mActivity;

    private List<Disposable> mDisposableList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityStack.getInstance().addActivity(this);
//        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            StatusBarUtil.setTranslucentForCoordinatorLayout(this, 0);
//        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        mDaoUtil = new DaoUtil(this);
        mActivity = this;

//        ToastUtil.showToast(this, this.getClass().getSimpleName());

    }


    public CustomToolbarBinding getCustomToolbarBinding(ViewDataBinding binding) {
        return (CustomToolbarBinding) binding;
    }


    public void tagRxTask(Disposable item) {
        mDisposableList.add(item);
    }

    public void checkUpdate() {
        if (SystemFramework.Environment.RELEASE.equals(SystemFramework.getInstance().getEnvironment())) {
            super.checkUpdate("app0001");
        }
    }

    public void observeEvents(final int... flags) {
        if (mEventConsumerHolder != null) {
            mEventConsumerHolder.detach();
        }

        mEventConsumerHolder = new BaseEventConsumer(this, flags) {

            @Override
            public void handleEvent(EventInfo event) {
                BaseGomeWorkActivity.this.handleEvent(event);
            }
        };
        mEventConsumerHolder.attach();
    }


    protected void handleEvent(EventInfo event) {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStack.getInstance().removeActivity(this);
        if (mEventConsumerHolder != null) {
            mEventConsumerHolder.detach();
        }

        for (Disposable item : mDisposableList) {
            if (!item.isDisposed()) {
                item.dispose();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportFinishAfterTransition();
        } else {
            super.onBackPressed();
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


    protected void onImageGetResult(boolean isSuccess, Uri uri, File file) {
    }


    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CAMERA_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    if (!isNeedCrop) {
                        String filePath = handlePickedImage(mFileCamera.getAbsolutePath());
                        onImageGetResult(true, getUriForFile(filePath), new File(filePath));
                    } else {
                        String filePath = handlePickedImage(mFileCamera.getAbsolutePath());
                        mFileCamera = new File(filePath);
                        startImageCropIntent(getImageContentUri(mFileCamera));
                    }
                } else {
                    onImageGetResult(false, null, null);
                }
                break;
            case REQUEST_CODE_SELECT_PHOTO:
                if (data != null && resultCode == Activity.RESULT_OK) {
                    final Uri originalUri = data.getData();
                    String filePath = ContentUriUtils.getPath(this, originalUri);
                    if (!isNeedCrop) {
                        filePath = handlePickedImage(filePath);
                    }
                    Uri result = getUriForFile(filePath);
                    if (!isNeedCrop) {
                        onImageGetResult(true, result, new File(filePath));
                    } else {
                        startImageCropIntent(originalUri);
                    }
                } else {
                    onImageGetResult(false, null, null);
                }
                break;
            case REQUEST_CODE_IMAGE_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    onImageGetResult(true, mImageCropOutputUri, mImageCropOutputFile);
                } else {
                    onImageGetResult(false, null, null);
                }
                break;
            default:
                break;
        }
    }

    protected void startImageCropIntent(Uri inputUri) {
        File file = CameraHelper.getPublicStorageOutputMediaFile(getBaseContext(), CameraHelper.MEDIA_TYPE_IMAGE);
        mImageCropOutputUri = Uri.fromFile(file);
        mImageCropOutputFile = file;

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inputUri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCropOutputUri);
        intent.putExtra("return-data", false);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        }
        startActivityForResult(intent, REQUEST_CODE_IMAGE_CROP);

//        Crop.of(inputUri, mImageCropUri).asSquare().start(this, REQUEST_CODE_IMAGE_CROP);
    }

    /**
     * Handle the picture file which get from camera or album.
     *
     * @param fileName
     */
    protected String handlePickedImage(String fileName) {
        Bitmap myBitmap = PictureUtils.getSmallBitmap(fileName);
        int angle = PictureUtils.getAngle(fileName);
        if (angle != 0) {
            Matrix m = new Matrix();
            int width = myBitmap.getWidth();
            int height = myBitmap.getHeight();
            m.setRotate(angle); // 旋转angle度
            myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, width, height, m, true);// 从新生成图片
        }
        fileName = outputBitmap(myBitmap);
        return fileName;
    }

    /**
     * Cache a special bitmap and return cached path
     *
     * @param data
     * @return
     */
    protected String outputBitmap(Bitmap data) {
        try {
            byte[] rawData = PictureUtils.Bitmap2Bytes(data);
            File file = CameraHelper.getOutputMediaFile(this, CameraHelper.MEDIA_TYPE_IMAGE);
            return FileCacheUtils.cache(this, rawData, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    protected Uri getUriForFile(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(getBaseContext(), getBaseContext().getPackageName() + ".fileProvider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    protected Uri getUriForFile(String filePath) {
        return getUriForFile(new File(filePath));
    }

    public void showImagePickerWindow() {
        this.showImagePickerWindow(false, null);
    }


    public void showImagePickerWindow(final boolean isNeedCrop, final DialogInterface.OnCancelListener listener) {
        showImagePickerWindow(isNeedCrop, listener, false);
    }

    /**
     * Show a pop window , ask user select the path from getting image.
     */
    public void showImagePickerWindow(final boolean isNeedCrop, final DialogInterface.OnCancelListener listener, final boolean isOnlyCamera) {
        this.isNeedCrop = isNeedCrop;
        isDoneSelect = false;
        if (mSlideFromBottomPopup == null) {
            List<String> menus = new ArrayList<>();
            menus.add("拍照");
            if (!isOnlyCamera) {
                menus.add("相册");
            }
            mSlideFromBottomPopup = new SlideFromBottomPopup(BaseGomeWorkActivity.this, menus);
            mSlideFromBottomPopup.setOnMenuItemClickListener(new MyBasePopupWindow.OnMenuItemClickListener() {
                @Override
                public void onMenuItemClick(int position) {
                    switch (position) {
                        case 0:
                            isDoneSelect = true;
                            requestPermission(Manifest.permission.CAMERA, new IPermissionRequestCallback() {
                                @Override
                                public void onFinished(String permission, boolean isSuccess) {
                                    if (isSuccess) {
                                        Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        mFileCamera = CameraHelper.getOutputMediaFile(getBaseContext(), CameraHelper.MEDIA_TYPE_IMAGE);
                                        Uri contentUri = getUriForFile(mFileCamera);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            intent_camera.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                                            intent_camera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
//                                                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//
                                        }
                                        intent_camera.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                                        startActivityForResult(intent_camera, REQUEST_CODE_CAMERA_PHOTO);
                                    } else {
                                        if (listener != null) {
                                            listener.onCancel(null);
                                        }
                                    }
                                }
                            });
                            mSlideFromBottomPopup.dismiss();
                            break;
                        case 1:
                            isDoneSelect = true;
                            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, new IPermissionRequestCallback() {
                                @Override
                                public void onFinished(String permission, boolean isSuccess) {
                                    if (isSuccess) {
                                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                            startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                                                    REQUEST_CODE_SELECT_PHOTO);
                                        } else {
                                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                                            intent.setType("image/*");
                                            startActivityForResult(intent, REQUEST_CODE_SELECT_PHOTO);
                                        }

                                    } else {
                                        if (listener != null) {
                                            listener.onCancel(null);
                                        }
                                    }
                                }
                            });
                            mSlideFromBottomPopup.dismiss();
                            break;
                        default:
                            break;
                    }
                }
            });
        }

        mSlideFromBottomPopup.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!isDoneSelect) {
                    if (listener != null) {
                        listener.onCancel(null);
                    }
                }
            }
        });
        mSlideFromBottomPopup.showPopupWindow();

    }


    public boolean isLogined() {
        return !TextUtils.isEmpty(SharedPreferencesHelper.getAccessToken());
    }


    public String getLoginUserId() {
        AccessTokenInfo bean = SharedPreferencesHelper.getAccessTokenInfo();
        if (bean != null) {
            return bean.userInfo == null ? "" : bean.userInfo.getId();
        }
        return "";
    }

    public void showPopWindowMenu(View anchorView, List<String> menuList, final MenuPopup.OnMenuItemClickListener listener) {
        final MenuPopup menu = new MenuPopup(this, menuList);
        menu.showPopupWindow(anchorView);
        menu.setOnMenuItemClickListener(new MyBasePopupWindow.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position) {
                if (listener != null) {
                    listener.onMenuItemClick(position);
                    menu.dismiss();
                }
            }
        });
    }


}
