package com.gome.work.common.widget;

import android.content.Context;
import android.view.View;

import razerdp.basepopup.BasePopupWindow;

public abstract class MyBasePopupWindow extends BasePopupWindow {

    protected OnMenuItemClickListener onMenuItemClickListener;

    public MyBasePopupWindow(Context context) {
        super(context);
    }

    public MyBasePopupWindow(Context context, int w, int h) {
        super(context, w, h);
    }

    protected MyBasePopupWindow(Context context, int w, int h, boolean initImmediately) {
        super(context, w, h, initImmediately);
    }


    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClick(int position);
    }


}
