package com.gome.work.common.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public int position;

    public T dataItem;

    private Object target;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void bind(T t, int position) {

    }

}