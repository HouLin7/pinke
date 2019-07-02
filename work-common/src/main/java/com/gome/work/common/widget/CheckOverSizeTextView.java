package com.gome.work.common.widget;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;

/**
 * Created by liubomin on 2017/3/9.
 */

public class CheckOverSizeTextView extends TextView {

    protected boolean isOverSize;
    private OnOverSizeChangedListener changedListener;

    public CheckOverSizeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CheckOverSizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CheckOverSizeTextView(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (changedListener != null) {
            changedListener.onChanged(checkOverLine());
        }
    }

    private void init() {
        // invalidate when layout end
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (changedListener != null) {
                    changedListener.onChanged(checkOverLine());
                }
            }
        });
    }

    /**
     * <p>
     * <span style="color: purple;">
     * <em>check if the text content has ellipsis </em></span>
     * </p>
     *
     * @return if the text content over maxlines
     */
    public boolean checkOverLine() {
        int maxLine = 2;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            maxLine = getMaxLines();
        }
        try {
            Field field = getClass().getSuperclass().getDeclaredField("mLayout");
            field.setAccessible(true);
            Layout mLayout = (Layout) field.get(this);
            if (mLayout == null)
                return false;
            isOverSize = mLayout.getEllipsisCount(maxLine - 1) > 0;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return isOverSize;
    }

    public boolean isOverSize() {
        return isOverSize;
    }

    public void displayAll() {
        setMaxLines(Integer.MAX_VALUE);
        setEllipsize(null);
    }

    public void hide(int maxlines) {
        setEllipsize(TruncateAt.END);
        setMaxLines(maxlines);
    }

    // set a listener for callback
    public OnOverSizeChangedListener getChangedListener() {
        return changedListener;
    }

    public void setOnOverLineChangedListener(OnOverSizeChangedListener changedListener) {
        this.changedListener = changedListener;
    }

    public interface OnOverSizeChangedListener {
        void onChanged(boolean isOverSize);
    }
}
