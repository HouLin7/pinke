package com.gome.work.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.gome.work.common.R;

/**
 * Created by liubomin on 2016/8/29.
 * TabLayout tab text 颜色渐变
 */
public class GradientTextView extends AppCompatTextView {
    int mAlpha;
    TextPaint mTextPaint;
    Rect mTextBounds;
    public GradientTextView(Context context) {
        super(context);
        initPaint();
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(getTextSize());
        mTextBounds = new Rect();
        String text = getText().toString();
        mTextPaint.getTextBounds(text, 0, text.length(), mTextBounds);
        mTextPaint.setTextSize(getTextSize());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {

        mTextPaint.setColor(getResources().getColor(R.color.black));
        mTextPaint.setAlpha(255 - mAlpha);
        canvas.drawText(getText().toString(), 0, getBaseline(), mTextPaint);
        mTextPaint.setColor(getResources().getColor(R.color.blue_bar));
        mTextPaint.setAlpha(mAlpha);
        canvas.drawText(getText().toString(), 0, getBaseline(), mTextPaint);
    }
    public void setAlpha(float alpha) {
        int ceil = (int) Math.ceil(255 * alpha);
        mAlpha = ceil;
        invalidate();
    }
}
