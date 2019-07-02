
package com.gome.work.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.gome.utils.LogUtil;
import com.gome.work.common.R;
import com.gome.work.common.utils.UiUtils;

/**
 * 显示气泡外形的imageview 兼容fresco Created by xzx on 2016/4/14
 */
public class ChatDraweeImageView extends android.support.v7.widget.AppCompatImageView {
    private final static String TAG=ChatDraweeImageView.class.getSimpleName();
    private int mMaskResId;
    private Bitmap mBitmap;
    private Bitmap mMaskBmp;
    private NinePatchDrawable mMaskDrawable;

    private Paint mPaint;
    private Paint mMaskPaint;

    private boolean shadeShow = false;
    private Matrix mMatrix;

    private int maxWidth = 130;
    private int maxHeight = 150;
    private int minSize = 80;

    private boolean isWrapContent = true;
    private ViewType viewType;


    public enum ViewType {
        Image,
        Location
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }

    public ChatDraweeImageView(Context context) {
        this(context, null);
        selfInit();
    }

    public ChatDraweeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChatImageView, 0, 0);
        if (a != null) {
            mMaskResId = a.getResourceId(R.styleable.ChatImageView_chat_image_mask, 0);
            shadeShow = a.getBoolean(R.styleable.ChatImageView_shadeShow, false);
            a.recycle();
        }
        selfInit();
    }

    private void selfInit() {
        maxWidth = UiUtils.dip2px(getContext(), maxWidth);
        maxHeight = UiUtils.dip2px(getContext(), maxHeight);
        minSize = UiUtils.dip2px(getContext(), minSize);
//        if (mDraweeHolder == null) {
//            final GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(
//                    getResources())
//                    .setPlaceholderImage(
//                            getContext().getResources().getDrawable(R.drawable.ic_load_img_failed),
//                            ScalingUtils.ScaleType.CENTER_CROP)
//                    .setFailureImage(
//                            getContext().getResources().getDrawable(R.drawable.ic_load_img_failed),
//                            ScalingUtils.ScaleType.CENTER_CROP)
//                    .build();
//            mDraweeHolder = DraweeHolder.create(hierarchy, getContext());
//        }
        init();
    }

    private void init() {
        mMatrix = new Matrix();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (mMaskResId <= 0) {
            return;
        }
        mMaskBmp = BitmapFactory.decodeResource(getResources(), mMaskResId);
        byte[] ninePatchChunk = mMaskBmp.getNinePatchChunk();
        if (ninePatchChunk != null && NinePatch.isNinePatchChunk(ninePatchChunk)) {
            mMaskDrawable = new NinePatchDrawable(getResources(), mMaskBmp, ninePatchChunk,
                    new Rect(), null);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        LogUtil.e(TAG,"onDraw");
        Drawable mDrawable = getDrawable();
        if (mDrawable == null) {
            canvas.drawColor(Color.TRANSPARENT);
            return; // couldn't resolve the URI
        }

        if (mDrawable.getBounds().width() == 0 || mDrawable.getBounds().height() == 0) {
            canvas.drawColor(Color.TRANSPARENT);
            return; // nothing to draw (empty bounds)
        }
        mBitmap = getBitmapFromDrawable(mDrawable);
        if (mBitmap == null) {
            return;
        }
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        Bitmap mResult = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas resultCanvas = new Canvas(mResult);
        // CENTER_CROP Bitmap
        mMatrix.reset();
        float scale;
        float dx = 0, dy = 0;
        int bmpWidth = mBitmap.getWidth();
        int bmpHeight = mBitmap.getHeight();
        if (bmpWidth * height > width * bmpHeight) {
            scale = (float) height / (float) bmpHeight;
            dx = (width - bmpWidth * scale) * 0.5f;
        } else {
            scale = (float) width / (float) bmpWidth;
            dy = (height - bmpHeight * scale) * 0.5f;
        }
        mMatrix.setScale(scale, scale);
        mMatrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));
        resultCanvas.save();
        resultCanvas.concat(mMatrix);
        resultCanvas.drawBitmap(mBitmap, 0, 0, mPaint);
        if (viewType == ViewType.Location) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor("#000000"));
            paint.setAlpha(153);
            resultCanvas.drawRect(0, height - UiUtils.dip2px(getContext(), 30), width, height,
                    paint);
        }
        if (shadeShow) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor("#000000"));
            paint.setAlpha(153);
            resultCanvas.drawRect(0, 0, bmpWidth, bmpHeight, paint);
            LogUtil.e(TAG,"shadeShow width: "+width+"height: "+height);
        }
        resultCanvas.restore();

        if (mMaskDrawable != null) {
            mMaskDrawable.getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            mMaskDrawable.setBounds(0, 0, width, height);
            mMaskDrawable.draw(resultCanvas);
        } else if (mMaskBmp != null) {
            if (mMaskPaint == null) {
                mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            }
            resultCanvas.drawBitmap(mMaskBmp, 0, 0, mMaskPaint);
        }

        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawBitmap(mResult, 0, 0, new Paint(Paint.ANTI_ALIAS_FLAG));
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
            } else if (drawable instanceof NinePatchDrawable) {
                bitmap = Bitmap.createBitmap(maxWidth,
                        maxHeight, Bitmap.Config.ARGB_8888);
            } else {
                if (drawable.getIntrinsicWidth() > 0 && drawable.getIntrinsicHeight() > 0) {
                    bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                } else if (drawable.getBounds().width() > 0 && drawable.getBounds().height() > 0) {
                    bitmap = Bitmap.createBitmap(drawable.getBounds().width(),
                            drawable.getBounds().height(), Bitmap.Config.ARGB_8888);
                } else {
                    bitmap = null;

                }

            }
            if (bitmap != null) {
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
            }
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        mDraweeHolder.onDetach();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        mDraweeHolder.onAttach();
    }

    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
//        mDraweeHolder.onDetach();
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
//        mDraweeHolder.onAttach();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        mDraweeHolder.onTouchEvent(event) ||
        return  super.onTouchEvent(event);
    }

    public int wrapImage(int width, int height) {
        ViewGroup.LayoutParams params = getLayoutParams();
        int oldHeight = params.height;
        float scale;
        if (height > maxHeight || width > maxWidth) {

            if (width * maxHeight > maxWidth * height) {
                scale = (float) maxWidth / (float) width;
            } else {
                scale = (float) maxHeight / (float) height;
            }
            params.width = (int) ((width * scale) + 0.5f);
            params.height = (int) ((height * scale) + 0.5f);
        } else if (height < minSize || width < minSize) {
            if (width > height) {
                scale = (float) minSize / (float) height;
            } else {
                scale = (float) minSize / (float) width;
            }
            params.width = (int) ((width * scale) + 0.5f);
            params.height = (int) ((height * scale) + 0.5f);
        } else {
            params.width = width;
            params.height = height;
        }
        setLayoutParams(params);

        //返回新老图片高度差
        return params.height-oldHeight;
    }

//    public void setImageUri(String uri, ResizeOptions options) {
//        ImageRequest imageRequest;
//        if (options != null) {
//            imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
//                    .setProgressiveRenderingEnabled(true)
//                    .setResizeOptions(options)
//
//                    .setAutoRotateEnabled(true)
//                    .build();
//        } else {
//            imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
//                    .setProgressiveRenderingEnabled(true)
//                    .setAutoRotateEnabled(true)
//                    .build();
//        }
//
//        final AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setOldController(mDraweeHolder.getController())
//                .setImageRequest(imageRequest)
//                .setControllerListener(new BaseControllerListener<ImageInfo>() {
//                    @Override
//                    public void onFinalImageSet(String id, ImageInfo imageInfo,
//                            Animatable animatable) {
//                        super.onFinalImageSet(id, imageInfo, animatable);
//
//                        if (isWrapContent) {
//                            wrapImage(imageInfo.getWidth(), imageInfo.getHeight());
//                        }
//                        Drawable drawable = mDraweeHolder.getTopLevelDrawable();
//                        setImageDrawable(drawable);
//                    }
//
//                    @Override
//                    public void onFailure(String id, Throwable throwable) {
//                        super.onFailure(id, throwable);
//                        // 图片加载失败，显示占位图
//                        Drawable drawable = ContextCompat.getDrawable(getContext(),R.drawable.ic_load_img_failed);
//                        setImageDrawable(drawable);
//                    }
//
//                    @Override
//                    public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
//                        super.onIntermediateImageSet(id, imageInfo);
//                        if (isWrapContent) {
//                            wrapImage(imageInfo.getWidth(), imageInfo.getHeight());
//                        }
//                        setImageDrawable(mDraweeHolder.getTopLevelDrawable());
//                    }
//                })
//                .build();
//        mDraweeHolder.setController(controller);
//    }

    /**
     * @param url
     * @param wrapContent 固定大小 还是适配图片大小
     * @param type image location
     */
    public void displayImage(String url, boolean wrapContent, ViewType type) {
        this.viewType = type;
        this.isWrapContent = wrapContent;
//        setImageUri(url, new ResizeOptions(maxWidth / 2, maxHeight / 2));
    }

    public void displayOriginalImage(String url, boolean wrapContent, ViewType type) {
        this.viewType = type;
        this.isWrapContent = wrapContent;
//        setImageUri(url, null);
    }

    public boolean isShadeShow() {
        return shadeShow;
    }

    public void setShadeShow(boolean shadeShow) {
        this.shadeShow = shadeShow;
        invalidate();
    }
}
