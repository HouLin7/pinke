package com.gome.work.common.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.scwang.smartrefresh.layout.util.DensityUtil;

/**
 * Create by liupeiquan on 2018/10/20
 */
public class GlideIMTransform extends BitmapTransformation {
    private int maxHeight;
    private int maxWidth;
    private int minLength;
    public GlideIMTransform(Context context) {
        super(context);
        maxHeight = DensityUtil.dp2px(150);
        maxWidth = DensityUtil.dp2px(150);
        minLength = DensityUtil.dp2px(90);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap bitmap = wrapAndCreateImage(toTransform, toTransform.getWidth(), toTransform.getHeight());
        if (bitmap != null) {
            if (bitmap != toTransform) {
                toTransform.recycle();
            }
            return bitmap;
        } else {
            return toTransform;
        }
    }

    @Override
    public String getId() {
        return null;
    }


    private Bitmap wrapAndCreateImage(Bitmap source, int width, int height) {

        float scale = 16.0f / 9.0f;
        float originScale;
        if (width > height) {
            originScale = (float) width / (float) height;
        } else {
            originScale = (float) height / (float) width;
        }
        if (scale > originScale) {
            scale = originScale;
        }
        if (width > height) {
            //横图
            if (height > maxHeight) {
                height = maxHeight;
            } else if (height < minLength) {
                height = minLength;
            }
            width = (int) (scale * height);
        } else if (width < height) {
            //竖图
            if (width > maxWidth) {
                width = maxWidth;
            } else if (width < minLength) {
                width = minLength;
            }
            height = (int) (scale * width);
        } else {
            //方图
            if (width > maxHeight) {
                width = maxHeight;
                height = maxHeight;
            } else if (width < minLength) {
                width = minLength;
                height = minLength;
            }
        }
        return zoomImg(source, width, height);
    }

    // 缩放图片
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return bitmap;
    }
}
