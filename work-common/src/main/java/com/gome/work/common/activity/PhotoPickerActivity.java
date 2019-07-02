package com.gome.work.common.activity;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.gome.work.core.event.EventDispatcher;
import com.gome.work.core.event.model.EventInfo;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TResult;

/**
 * Create by liupeiquan on 2018/10/16
 */
public class PhotoPickerActivity extends TakePhotoActivity {
    public static final String PHOTO_TYPE = "PHOTO_TYPE";
    public static final int PHOTO_TYPE_PICKER = 1;  //从相册选择
    public static final int PHOTO_TYPE_CAMERA = 2;  //从拍照获取
    private static final String TAG = PhotoPickerActivity.class.getSimpleName();
    private int photo_type;
    private TakePhoto takePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        takePhoto = getTakePhoto();
        takePhoto.onEnableCompress(getCompressConfig(),false);
        photo_type = getIntent().getIntExtra(PHOTO_TYPE, -1);
        if (photo_type == PHOTO_TYPE_PICKER) {
            takePhoto.onPickMultiple(9);
        } else if (photo_type == PHOTO_TYPE_CAMERA) {
            takePhoto.onPickFromCapture(createImageUri(this));
        } else {
            finish();
        }
    }


    private CompressConfig getCompressConfig(){
        CompressConfig config=new CompressConfig.Builder()
                .enablePixelCompress(true)
                .enableQualityCompress(true)
                .create();
        return config;
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        switch (photo_type) {
//            case PHOTO_TYPE_PICKER:
//                EventDispatcher.postEvent(EventInfo.FLAG_CHAT_KEY_BROAD_PICKER, result.getImages());
//                break;
            case PHOTO_TYPE_CAMERA:
                EventDispatcher.postEvent(EventInfo.FLAG_CHAT_KEY_BROAD_CAMERA, result.getImage().getPath());
                break;
        }
        finish();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        takeCancel();
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
        finish();
    }

    private static Uri createImageUri(Context context) {
        String name = "takePhoto" + System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, name);
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, name + ".png");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        return uri;
    }
}
