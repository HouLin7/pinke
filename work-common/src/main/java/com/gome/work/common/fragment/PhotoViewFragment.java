package com.gome.work.common.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.common.media.CameraHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gome.applibrary.activity.BaseActivity;
import com.gome.utils.CommonUtils;
import com.gome.utils.PictureUtils;
import com.gome.utils.ToastUtil;
import com.gome.work.common.R;
import com.gome.work.common.widget.MenuMenuPopup;
import com.gome.work.common.widget.BaseMenuPopupWindow;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoView;

public class PhotoViewFragment extends BaseWorkFragment {

    private String imageUrl;

    private Drawable mDefaultThumbDrawable;
    private PhotoView mImageView;

    private ProgressBar mProgressBar;

    private GlideDrawable mGlideDrawable;

    private SimpleDateFormat mSimplerDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageUrl = getArguments().getString(EXTRA_DATA);
    }

    private void refresh() {
        if (mImageView != null && isAdded()) {
            if (mDefaultThumbDrawable == null) {
                mDefaultThumbDrawable = getResources().getDrawable(R.drawable.img_default);
            }

            Glide.with(this)
                    .load(imageUrl)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            mProgressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            mProgressBar.setVisibility(View.GONE);
                            mGlideDrawable = resource;
                            return false;
                        }
                    })
                    .placeholder(mDefaultThumbDrawable)
                    .override(1920, 1920)
                    .fitCenter()
                    .into(mImageView);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refresh();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_view, null);
        mImageView = view.findViewById(R.id.photo_view);
        mProgressBar = view.findViewById(R.id.pb_progress);
        mProgressBar.setVisibility(View.VISIBLE);
        mImageView.setScale(1);
        mImageView.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                getActivity().onBackPressed();
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }
        });

        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mGlideDrawable == null) {
                    return true;
                }
                List<String> menus = new ArrayList<>();
                menus.add("保存");
                menus.add("分享");
                final MenuMenuPopup menuPopup = new MenuMenuPopup(getActivity(), menus);
                menuPopup.setOnMenuItemClickListener(new BaseMenuPopupWindow.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        switch (position) {
                            case 0:
                                getMyActivity().requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new BaseActivity.IPermissionRequestCallback() {
                                    @Override
                                    public void onFinished(String permission, boolean isSuccess) {

                                        if (isSuccess) {
                                            if (mGlideDrawable != null) {
                                                if (mGlideDrawable instanceof GlideBitmapDrawable) {
                                                    saveSDCard(((GlideBitmapDrawable) mGlideDrawable).getBitmap());
                                                } else if (mGlideDrawable instanceof GifDrawable) {
                                                    saveSDCard(((GifDrawable) mGlideDrawable).getData());
                                                } else {
                                                    ToastUtil.showToast(getMyActivity(), "站不支持该类型保存");
                                                }
                                            }

                                        }

                                    }
                                });

                                break;
                            case 1:
                                getMyActivity().requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new BaseActivity.IPermissionRequestCallback() {
                                    @Override
                                    public void onFinished(String permission, boolean isSuccess) {

                                        if (isSuccess) {
                                            if (mGlideDrawable != null) {
                                                if (mGlideDrawable instanceof GlideBitmapDrawable) {
                                                    Bitmap bitmap = ((GlideBitmapDrawable) mGlideDrawable).getBitmap();
                                                    byte[] rawData = PictureUtils.Bitmap2Bytes(bitmap);
                                                    shareOuter(rawData);
                                                } else {
                                                    ToastUtil.showToast(getMyActivity(), "站不支持该类型保存");
                                                }
                                            }

                                        }

                                    }
                                });
                                break;
                            default:
                                break;

                        }
                        menuPopup.dismiss();
                    }
                });
                menuPopup.showPopupWindow(v);
                return true;
            }
        });
        return view;

    }


    private void shareOuter(byte[] bitmap) {
        File file = CameraHelper.getOutputMediaFile(getMyActivity(), CameraHelper.MEDIA_TYPE_IMAGE);
        PictureUtils.savePic(bitmap, file);
        CommonUtils.shareFile(getMyActivity(), file);
    }

    private void saveSDCard(Bitmap bitmap) {
        byte[] rawData = PictureUtils.Bitmap2Bytes(bitmap);
        saveSDCard(rawData);
    }

    private void notifyGalleryUpdate(File file) {
//        try {
//            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
//                    file.getAbsolutePath(), file.getName(), null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        // 最后通知图库更新
        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
    }


    private File getOutputDir() {
        File fileFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String fileName = mSimplerDateFormat.format(new Date());
        File file = new File(fileFolder.getPath() + "/meiban/", fileName + ".jpg");
        return file;
    }

    private void saveSDCard(final byte[] bitmap) {
        final File file = getOutputDir();
        getMyActivity().tagRxTask(Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                PictureUtils.savePic(bitmap, file);
                return true;

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) {
                        if (getMyActivity() != null) {
                            ToastUtil.showToast(getMyActivity(), "图片已保存至" + file.getAbsolutePath());
                        }
                        notifyGalleryUpdate(file);
                    }
                }));


    }
}
