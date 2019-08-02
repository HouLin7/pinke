package com.gome.work.common.widget;

import android.content.Context;
import razerdp.basepopup.BasePopupWindow;

public abstract class BaseMenuPopupWindow extends BasePopupWindow {

    protected OnMenuItemClickListener onMenuItemClickListener;

    public BaseMenuPopupWindow(Context context) {
        super(context);
    }

    public BaseMenuPopupWindow(Context context, int w, int h) {
        super(context, w, h);
    }

    protected BaseMenuPopupWindow(Context context, int w, int h, boolean initImmediately) {
        super(context, w, h, initImmediately);
    }


    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClick(int position);
    }




}
