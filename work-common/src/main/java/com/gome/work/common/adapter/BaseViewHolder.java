package com.gome.work.common.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public T dataItem;

    private Object target;

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }


}