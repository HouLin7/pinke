package com.gome.work.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.work.common.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liupeiquan on 2018/8/14.
 */
public class TitleBarView extends RelativeLayout implements TitleBarViwAttrInterface {
    private TextView midTextView;
    private TextView rightTextView;
    private TextView leftTextView;
    private TextView tvDivider;
    private float leftTextSize, midTextSize, rightTextSize;
    private int leftTextColor, midTextColor, rightTextColor, backgroundColor;
    private String leftText, midText, rightText;
    private int leftIconId, rightIconId, leftCircleIconId, rightImage;
    /**
     * 是否显示左边按钮图标
     */
    private boolean isShowLeftIcon;
    private Drawable drawableLeft, drawableRight;
    /**
     * 左边按钮是否是圆形图标
     */
    private boolean isLeftCircleIcon;
    /**
     * 是否显示分割线
     */
    private boolean isShowDividerView;
    private CircleImageView leftCircleImageView;
    private ImageView rightImageView;
    private RelativeLayout layout_parent;


    public TitleBarView(Context context) {
        this(context, null);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_title_bar_view, this);
        layout_parent = view.findViewById(R.id.layout_parent);
        leftTextView = view.findViewById(R.id.title_bar_left);
        midTextView = view.findViewById(R.id.title_bar_mid);
        rightTextView = view.findViewById(R.id.title_bar_right);
        tvDivider = view.findViewById(R.id.title_bar_divider);
        leftCircleImageView = view.findViewById(R.id.title_bar_left_img);
        rightImageView = view.findViewById(R.id.title_bar_right_img);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBarView);

        leftTextColor = typedArray.getResourceId(R.styleable.TitleBarView_leftTextColor, -1);
        midTextColor = typedArray.getResourceId(R.styleable.TitleBarView_midTextColor, -1);
        rightTextColor = typedArray.getResourceId(R.styleable.TitleBarView_rightTextColor, -1);

        backgroundColor = typedArray.getResourceId(R.styleable.TitleBarView_backgroundColor, -1);

        leftTextSize = typedArray.getDimension(R.styleable.TitleBarView_leftTextSize, 14);
        midTextSize = typedArray.getDimension(R.styleable.TitleBarView_midTextSize, 18);
        rightTextSize = typedArray.getDimension(R.styleable.TitleBarView_rightTextSize, 14);

        leftText = typedArray.getString(R.styleable.TitleBarView_leftText);
        midText = typedArray.getString(R.styleable.TitleBarView_midText);
        rightText = typedArray.getString(R.styleable.TitleBarView_rightText);

        leftIconId = typedArray.getResourceId(R.styleable.TitleBarView_leftIcon, R.drawable.icon_back_black);
        rightIconId = typedArray.getResourceId(R.styleable.TitleBarView_rightIcon, -1);
        leftCircleIconId = typedArray.getResourceId(R.styleable.TitleBarView_leftCircleIcon, -1);
        rightImage = typedArray.getResourceId(R.styleable.TitleBarView_rightImage, -1);

        isShowLeftIcon = typedArray.getBoolean(R.styleable.TitleBarView_isShowLeftIcon, false);
        isLeftCircleIcon = typedArray.getBoolean(R.styleable.TitleBarView_isLeftCircleIcon, false);
        isShowDividerView = typedArray.getBoolean(R.styleable.TitleBarView_isShowDividerView, true);
        leftTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, leftTextSize);
        midTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, midTextSize);
        rightTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, rightTextSize);


        if (leftTextColor != -1) {
            leftTextView.setTextColor(getResources().getColor(leftTextColor));
        }
        if (midTextColor != -1) {
            midTextView.setTextColor(getResources().getColor(midTextColor));
        }
        if (rightTextColor != -1) {
            rightTextView.setTextColor(getResources().getColor(rightTextColor));
        }


        leftTextView.setText(leftText);
        midTextView.setText(midText);
        rightTextView.setText(rightText);

        if (isShowLeftIcon) {
            drawableLeft = getResources().getDrawable(leftIconId);
            leftTextView.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                    null, null, null);
        } else {
            leftTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, null, null);
        }

        if (rightIconId != -1) {
            drawableRight = getResources().getDrawable(rightIconId);
            rightTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, drawableRight, null);
        }

        if (rightImage != -1) {
            rightImageView.setVisibility(VISIBLE);
            rightTextView.setVisibility(GONE);
            rightImageView.setImageResource(rightImage);
        }


        if (isLeftCircleIcon) {
            leftTextView.setVisibility(GONE);
            leftCircleImageView.setVisibility(VISIBLE);
            if (leftCircleIconId != -1) {
                leftCircleImageView.setImageResource(leftCircleIconId);
            } else {
                leftCircleImageView.setImageResource(R.drawable.ic_launcher);
            }
        }

        if (backgroundColor != -1) {
            layout_parent.setBackgroundResource(backgroundColor);
        }

        if (isShowDividerView) {
            tvDivider.setVisibility(VISIBLE);
        } else {
            tvDivider.setVisibility(GONE);
        }

        typedArray.recycle();
    }


    @Override
    public void setLeftTextSize(@DimenRes int dimensSize) {
        float dimen = getResources().getDimension(dimensSize);
        leftTextView.setTextSize(dimen);
    }

    @Override
    public void setMidTextSize(@DimenRes int dimensSize) {
        float dimen = getResources().getDimension(dimensSize);
        midTextView.setTextSize(dimen);
    }

    @Override
    public void setRightTextSize(@DimenRes int dimensSize) {
        float dimen = getResources().getDimension(dimensSize);
        rightTextView.setTextSize(dimen);
    }

    @Override
    public void setLeftViewText(CharSequence string) {
        if (TextUtils.isEmpty(string)) {
            throw new NullPointerException();
        }
        leftTextView.setText(string.toString());
    }

    @Override
    public void setMidViewText(CharSequence string) {
        if (TextUtils.isEmpty(string)) {
            throw new NullPointerException();
        }
        midTextView.setText(string.toString());
    }

    @Override
    public void setRightViewText(CharSequence string) {
        if (TextUtils.isEmpty(string)) {
            throw new NullPointerException();
        }
        rightTextView.setText(string.toString());
    }

    @Override
    public void setLeftTextColor(@ColorRes int color) {
        leftTextView.setTextColor(getResources().getColor(color));
    }

    @Override
    public void setMidTextColor(@ColorRes int color) {
        midTextView.setTextColor(getResources().getColor(color));
    }

    @Override
    public void setRightTextColor(@ColorRes int color) {
        rightTextView.setTextColor(getResources().getColor(color));
    }


    @Override
    public TextView getLeftView() {
        return leftTextView;
    }

    @Override
    public TextView getMidView() {
        return midTextView;
    }

    @Override
    public TextView getRightView() {
        return rightTextView;
    }

    @Override
    public TextView getDividerView() {
        return tvDivider;
    }

    @Override
    public CircleImageView getCircleImageView() {
        return leftCircleImageView;
    }

    @Override
    public ImageView getRightImageView() {
        return rightImageView;
    }

    @Override
    public void setLeftCircleIcon(int drawableId) {
        leftCircleImageView.setImageResource(drawableId);
    }

    @Override
    public void setLeftCircleIcon(Drawable drawable) {
        leftCircleImageView.setImageDrawable(drawable);
    }

    public void setTitleBarBackground(int color) {
        layout_parent.setBackgroundResource(color);
    }
}
