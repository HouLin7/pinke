package com.gome.work.common.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;

public class DataBindViewHolder<T, K extends ViewDataBinding> extends BaseViewHolder<T> {

    public K binding;

    public DataBindViewHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

}