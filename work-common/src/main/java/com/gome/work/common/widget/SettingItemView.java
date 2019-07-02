
package com.gome.work.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.gome.work.common.R;
import com.gome.work.common.databinding.SettingItemViewBinding;

public class SettingItemView extends FrameLayout {
    private SettingItemViewBinding mSettingItemViewBinding;

    public SettingItemView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        mSettingItemViewBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.setting_item_view, null, false);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ItemSettingStyle,
                defStyle, 0);

        mSettingItemViewBinding.tvLeftText.setText(a
                .getString(R.styleable.ItemSettingStyle_leftTextSetting));
        mSettingItemViewBinding.tvRightText.setText(a
                .getString(R.styleable.ItemSettingStyle_rightTextSetting));
        Drawable leftBg = a.getDrawable(R.styleable.ItemSettingStyle_leftIconSetting);
        if (leftBg != null) {
            mSettingItemViewBinding.ivLeftIcon.setBackgroundDrawable(leftBg);
        } else {
            mSettingItemViewBinding.ivLeftIcon.setVisibility(View.GONE);
        }
        Drawable rightBg = a.getDrawable(R.styleable.ItemSettingStyle_rightIconSetting);
        if (rightBg != null) {
            mSettingItemViewBinding.rightIcon.setBackgroundDrawable(rightBg);
        }
        addView(mSettingItemViewBinding.getRoot());
        a.recycle();
    }

    public void bind(int resLeftIcon, String leftText, int resRightIcon) {
        mSettingItemViewBinding.tvLeftText.setText(leftText);
        if (resLeftIcon > 0) {
            mSettingItemViewBinding.ivLeftIcon.setVisibility(View.VISIBLE);
            mSettingItemViewBinding.ivLeftIcon.setImageResource(resLeftIcon);
        } else {
            mSettingItemViewBinding.ivLeftIcon.setVisibility(View.GONE);
        }
        if (resRightIcon > 0)
            mSettingItemViewBinding.rightIcon.setImageResource(resRightIcon);
    }

}
