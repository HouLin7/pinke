package com.gome.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.gome.applibrary.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class GraffitiView extends View {

    private static final String TAG = GraffitiView.class.getSimpleName();
    private Path mPath;

    private Paint mPaint;

    private Bitmap mBitmap;
    // 用于存放每一个paint属性信息
    private List<PaintBean> mPaintBeanList;

    // 后进先出，用于做回退功能
    private Stack<PaintBean> mStack;

    private float mLastX = 0f;

    private float mLastY = 0f;

    public GraffitiView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setHapticFeedbackEnabled(false);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.red));
        mPaint.setShadowLayer(2, 3, 3, 0x781ab2bc);
        mPaint.setStyle(Style.STROKE);
        mPaint.setAntiAlias(true);
        float density = getResources().getDisplayMetrics().density;
        mPaint.setStrokeWidth(2.5f * density);
        mPaintBeanList = new ArrayList<PaintBean>();
        mStack = new Stack<PaintBean>();
        // mBitmap = ((BitmapDrawable) getResources().getDrawable(
        // R.drawable.bg_login)).getBitmap();
    }

    public Paint getPaint() {
        return mPaint;
    }

    public GraffitiView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GraffitiView(Context context) {
        this(context, null);
    }

    public boolean isBackGroundExit() {
        if (mBitmap == null) {
            return true;
        }
        return false;
    }

    public Bitmap getBitmap() {
        return newGraffitiBitmap();
    }

    public Stack<PaintBean> getStack() {
        return this.mStack;
    }

    public List<PaintBean> getPBList() {
        return mPaintBeanList;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Path path = new Path();
                mPath = path;
                // mPath.re
                mLastX = x;
                mLastY = y;

                mPath.moveTo(x, y);

                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                // 如果移动距离小于5.0dip，则忽略，从而提高效率
                if (Math.abs(x - mLastX) < 5.0f && Math.abs(y - mLastY) < 5.0f) {
                    return true;
                }
                mPath.quadTo(mLastX, mLastY, (x + mLastX) / 2.0f, (y + mLastY) / 2.0f);

                mLastX = x;
                mLastY = y;
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                PaintBean pb = new PaintBean();

                pb.path = this.mPath;
                pb.color = mPaint.getColor();
                pb.paintWidth = mPaint.getStrokeWidth();

                mPaintBeanList.add(pb);

                mStack.push(pb);
                this.mPath = null;
                invalidate();
                return true;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap == null) {
            if (this.mBitmapFilePath != null && this.mBitmapFilePath.length() > 0) {
                // 根据屏幕大小 生成图片
                this.mBitmap = measureFitBitmap(mBitmapFilePath, getWidth(), getHeight());
            }
        }
        if (mBitmap != null) {
            int mBitMapWidth = mBitmap.getWidth();
            int mBitMapHeight = mBitmap.getHeight();
            int width = getWidth();
            int height = getHeight();
            Rect rect = new Rect((width - mBitMapWidth) / 2, (height - mBitMapHeight) / 2, (width + mBitMapWidth) / 2,
                    (height + mBitMapHeight) / 2);
            // Rect rect = new Rect(0, 0, getWidth(), getHeight());
            canvas.drawBitmap(mBitmap, null, rect, null);
        }

        Iterator<PaintBean> iterator = mPaintBeanList.iterator();
        PaintBean pb;
        mPaint.setStyle(Style.STROKE);
        while (iterator.hasNext()) {
            pb = iterator.next();
            mPaint.setColor(pb.color);
            mPaint.setStrokeWidth(pb.paintWidth);
            canvas.drawPath(pb.path, mPaint);
        }

        if (this.mPath != null) {
            canvas.drawPath(mPath, mPaint);
        }
    }

    /**
     * 把涂鸦生成图片
     *
     * @return
     */
    private Bitmap newGraffitiBitmap() {

        Bitmap resultBitmap;

        Rect rect = new Rect();
        int bitmapWidth = 0;
        int bitmapHeight = 0;

        // 如果有 背景图，根据背景图测试出要画的RECT的大小
        if (this.mBitmap != null) {
            bitmapWidth = mBitmap.getWidth();
            bitmapHeight = mBitmap.getHeight();

            int viewWidth = getWidth();
            int viewHeight = getHeight();
            rect.set(Math.abs((viewWidth - bitmapWidth)) / 2, Math.abs((viewHeight - bitmapHeight)) / 2,
                    (getWidth() + bitmapWidth) / 2, (getHeight() + bitmapHeight) / 2);
        }

        RectF rectF = new RectF();
        Iterator<PaintBean> pbIterator = this.mPaintBeanList.iterator();
        PaintBean pb;

        // 根据保存的path，测试出RECT大小和宽高
        while (pbIterator.hasNext()) {
            pb = pbIterator.next();
            if (pb.path.isEmpty()) {
                continue;
            }
            pb.path.computeBounds(rectF, true);
            if (rect.left > rectF.left) {
                rect.left = (int) rectF.left;
            }
            if (rect.right < rectF.right) {
                rect.right = (int) rectF.right;
            }
            if (rect.top > rectF.top) {
                rect.top = (int) rectF.top;
            }
            if (rect.bottom >= rectF.bottom) {
                continue;
            }
            rect.bottom = (int) rectF.bottom;
        }

        // 根据Rect的宽和高画图
        resultBitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(resultBitmap);
        // 是否画背景方框
        // Paint paintR = new Paint();
        // paintR.setColor(Color.WHITE);
        // canvas.drawRect(0, 0, rect.width(), rect.height(), paintR);

        // 把背景图画到canvas上
        if (this.mBitmap != null) {
            bitmapWidth = mBitmap.getWidth();
            bitmapHeight = mBitmap.getHeight();
            Rect rect2 = new Rect();
            rect2.set((rect.width() - bitmapWidth) / 2, (rect.height() - bitmapHeight) / 2,
                    (rect.width() + bitmapWidth) / 2, (rect.height() + bitmapHeight) / 2);

            canvas.drawBitmap(this.mBitmap, null, rect2, new Paint());
        }

        // 画path
        pbIterator = this.mPaintBeanList.iterator();
        while (pbIterator.hasNext()) {
            pb = pbIterator.next();
            if (pb.path.isEmpty()) {
                continue;
            }
            // 防止path画偏
            pb.path.offset(-rect.left, -rect.top);
            Paint paint = this.mPaint;
            paint.setColor(pb.color);
            paint.setStrokeWidth(pb.paintWidth);
            canvas.drawPath(pb.path, paint);
        }
        return resultBitmap;
    }

    // 此 listener监听draw的动作,用于实现界面上ImageView状态的改变
    public interface OnGraffitiViewDrawListener {
        // 使ImageView可点击
        public void makeViewClickable();

        // 使ImageView不可点击
        public void disClickable();
    }

    // 每一个画的状态的bean
    public class PaintBean {
        private Path path;
        private float paintWidth;
        private int color;
    }

    public void setBackGround(Bitmap bitmap) {
        this.mBitmap = bitmap;
        this.mBitmapFilePath = "";

    }

    public void setPaintColor(int i) {
        mPaint.setColor(i);
    }

    public void setPaintSize(int size) {
        mPaint.setStrokeWidth(size);
    }

    private String mBitmapFilePath;

    public void setBitmap(String fileAbs) {
        Bitmap bitmap = null;
        Bitmap result_bitmap = null;
        if (getWidth() <= 0 || getHeight() <= 0) {
            this.mBitmapFilePath = fileAbs;
            return;
        }
        int bitmapWidth = 0;
        int bitmapHeight = 0;

        int viewWidth = 0;
        int viewHeight = 0;

        try {
            viewWidth = getWidth();
            viewHeight = getHeight();

            bitmap = measureFitBitmap(fileAbs, viewWidth, viewHeight);
            if (bitmap == null) {
                return;
            }
            bitmapWidth = bitmap.getWidth();
            bitmapHeight = bitmap.getHeight();

            if (bitmapWidth < viewWidth && bitmapHeight < viewHeight) {
                this.mBitmap = bitmap;
                this.mPaintBeanList.clear();
                this.mStack.clear();
                return;
            }
        } catch (OutOfMemoryError e) {
            return;
        }

        if (bitmapWidth * viewHeight > bitmapHeight * viewWidth) {
            bitmapHeight *= viewWidth;
            bitmapHeight = bitmapHeight / bitmapWidth;
            bitmapWidth = viewWidth;
        } else {
            bitmapWidth = bitmapWidth * viewHeight / bitmapHeight;
            bitmapHeight = viewHeight;
        }
        Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect detRect = new Rect(0, 0, bitmapWidth, bitmapHeight);

        result_bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(result_bitmap);
        canvas.drawBitmap(bitmap, srcRect, detRect, new Paint());

        this.mPaintBeanList.clear();
        this.mStack.clear();
        this.mBitmap = result_bitmap;
        // this.mBitmap = BitmapFactory.decodeFile(fileAbs);
    }

    private Bitmap measureFitBitmap(String filePath, int maxWidth, int maxHeight) {
        Bitmap bitmap;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(filePath, opts);

        int bitmapWidth = opts.outWidth;
        int bitmapHeight = opts.outHeight;

        int fitBitmapSize = 1;
        while ((bitmapHeight > maxHeight) && (bitmapWidth > maxWidth)) {
            fitBitmapSize <<= 1;
            bitmapHeight >>= 1;
            bitmapWidth >>= 1;
        }
        opts.inSampleSize = fitBitmapSize;
        opts.inJustDecodeBounds = false;

        bitmap = BitmapFactory.decodeFile(filePath, opts);

        try {
            ExifInterface eInterface = new ExifInterface(filePath);

            if (eInterface != null) {
                int att = eInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                int degrees;
                switch (att) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degrees = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degrees = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degrees = 270;
                        break;
                    default:
                        degrees = 0;
                        break;
                }
                Matrix matrix = new Matrix();
                matrix.postRotate(degrees);

                Bitmap reBitmap = Bitmap
                        .createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if (reBitmap != bitmap) {
                    bitmap.recycle();
                }
                return reBitmap;
            }

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        return bitmap;

    }
}
