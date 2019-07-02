package com.gome.work.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageUtil {
    private static final String TAG = "ImageUtil";

    public static Bitmap RGB565toARGB888(Bitmap img) {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        // canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap drawableToBitmap(BitmapDrawable drawable) {
        Bitmap bitmap=drawable.getBitmap();
        return bitmap;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Matrix m = new Matrix();
        m.setRotate(angle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
        float targetX, targetY;
        if (angle == 90) {
            targetX = bitmap.getHeight();
            targetY = 0;
        } else {
            targetX = bitmap.getHeight();
            targetY = bitmap.getWidth();
        }
        final float[] values = new float[9];
        m.getValues(values);

        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];
        m.postTranslate(targetX - x1, targetY - y1);
        Bitmap bm1 = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bitmap, m, paint);
        return bm1;
    }

    /**
     * 旋转图片，使图片保持正确的方向。
     * @param bitmap 原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap || bitmap.isRecycled()) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (null != bitmap) {
            bitmap.recycle();
            bitmap = null;
        }
        return bmp;
    }

    /**
     * 旋转图片，使图片保持正确的方向。
     *
     * 该方法用来专门使用Fresco的Cache Bitmap
     * 如果清除了bitmap后，会打破Fresco的Cache系统
     *
     * @param bitmap 原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmapForFresco(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap || bitmap.isRecycled()) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bmp;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, float scale) {
        if (bitmap == null || scale <= 0)
            return bitmap;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static void setBitmap(Bitmap bitmap, Activity activity, int maxSize, ImageView mImageView) {
        if (bitmap != null) {
            float imageWidth = 0;
            int picWidth = bitmap.getWidth();
            float dinsity = picWidth / 3;
            float widthPx = DensityUtil.dip2px(activity, dinsity);
            float maxWidth = DensityUtil.dip2px(activity, maxSize);
            if (widthPx > maxWidth) {
                imageWidth = maxWidth;
            } else {
                imageWidth = widthPx;
            }
            if (picWidth != 0) {
                float scale = imageWidth / picWidth;
                if (scale != 1) {
                    mImageView.setImageBitmap(ImageUtil.scaleBitmap(bitmap, scale));
                } else {
                    mImageView.setImageBitmap(bitmap);
                }

            }
        }
    }

    public static int[] getImageWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        return new int[]{options.outWidth, options.outHeight};
    }


    public static void saveBitmap(Drawable drawable, File dir) throws IOException {
        OutputStream os = new FileOutputStream(dir);
        ImageUtil.drawableToBitmap(drawable).compress(Bitmap.CompressFormat.JPEG, 100, os);
        os.flush();
        os.close();
    }


    public static void saveBitmap(Bitmap bitmap, File dir) throws IOException {
        OutputStream os = new FileOutputStream(dir);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        os.flush();
        os.close();
    }

    /**
     * 刷新媒体库
     */
    public static void updataMedia(final Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)//版本号的判断  4.4为分水岭，发送广播更新媒体库
        {
            MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(uri);
                    context.sendBroadcast(mediaScanIntent);
                }
            });
        } else {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(file)));
        }

    }
}
