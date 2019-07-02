package com.gome.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPOutputStream;

public class PictureUtils {

    public static final String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
            + "CarDoctor";

    public static Bitmap getSmallBitmap(FileDescriptor fileDesc) {
        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDesc, null, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 1920, 1920);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fileDesc, null, options);
    }

    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 1920, 1920);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    // 计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    //
    // public static byte[] compressBitmap(Bitmap bitmap) {
    // byte[] gZip = gZip(bitmap);
    // // creatTempFile(gZip, "gzip");
    // byte[] encode = Base64.encode(gZip, Base64.DEFAULT);
    // // creatTempFile(encode, "base64");
    // return encode;
    // }

    public static byte[] gZip(Bitmap bm) {
        byte[] b = null;
        byte[] data = Bitmap2Bytes(bm);
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(data);
            gzip.finish();
            gzip.close();
            b = bos.toByteArray();
            bos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return b;
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        return Bitmap2Bytes(bm, Bitmap.CompressFormat.JPEG);
    }

    public static byte[] Bitmap2Bytes(Bitmap bm, Bitmap.CompressFormat format) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(format, 90, baos);
        return baos.toByteArray();
    }


    public static int getAngle(Context context, Uri uri) {
        int angle = 0;
        try {
            ContentResolver cr = context.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);// 根据Uri从数据库中找
            if (cursor != null) {
                cursor.moveToFirst();// 把游标移动到首位，因为这里的Uri是包含ID的所以是唯一的不需要循环找指向第一个就是了
                String orientation = cursor.getString(cursor.getColumnIndex("orientation"));// 获取旋转的角度
                cursor.close();
                if (orientation != null && !"".equals(orientation)) {
                    angle = Integer.parseInt(orientation);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return angle;
    }

    /**
     * 读取照片exif信息中的旋转角度
     *
     * @param path 照片路径
     * @return角度
     */
    public static int getAngle(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
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

    public static Bitmap loadNetPic(String url, int maxHeight, int maxWidth) {
        InputStream in = null;
        ByteArrayOutputStream bos = null;
        Bitmap value = null;
        try {
            HttpURLConnection connUrl = (HttpURLConnection) new URL(url).openConnection();
            connUrl.setConnectTimeout(5000);
            connUrl.setRequestMethod("GET");
            if (connUrl.getResponseCode() == 200) {
                in = connUrl.getInputStream();
                byte[] buffer = new byte[1024];
                int index = -1;
                bos = new ByteArrayOutputStream();
                while ((index = in.read(buffer)) > -1) {
                    bos.write(buffer, 0, index);
                }
                byte[] totalData = bos.toByteArray();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(totalData, 0, totalData.length, options);
                int sampleSize = 1;
                while (options.outHeight > maxHeight || options.outWidth > maxWidth) {
                    options.outHeight /= 2;
                    options.outWidth /= 2;
                    sampleSize++;
                }
                options.inSampleSize = sampleSize;
                options.inJustDecodeBounds = false;
                value = BitmapFactory.decodeByteArray(totalData, 0, totalData.length, options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }
        return value;
    }

    // 获取指定Activity的截屏，保存到png文件
    private static Bitmap takeScreenShot(Activity activity) {

        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = null;
        if (b1.getHeight() < height) {
            return b1;
        } else {
            b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        }
        view.destroyDrawingCache();
        return b;
    }


    // 保存到sdcard
    public static boolean savePic(byte[] data, File file) {
        if (file.exists()) {
            file.delete();
        }

        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static Bitmap shoot(Activity a) {
        Bitmap takeScreenShot = takeScreenShot(a);
        return takeScreenShot;
    }

    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }
}
