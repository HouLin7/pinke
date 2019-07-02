package com.gome.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {

    private Context mContext;


    public CustomScrollView(Context context) {
        super(context);
        this.mContext = context;
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return super.onTouchEvent(ev);
    }


}
