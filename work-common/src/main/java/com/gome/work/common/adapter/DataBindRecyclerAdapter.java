package com.gome.work.common.adapter;

import android.databinding.ViewDataBinding;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by chaergongzi on 2017/8/1.
 */

public abstract class DataBindRecyclerAdapter<T, K extends ViewDataBinding> extends BaseRecyclerAdapter<T> {

    public DataBindRecyclerAdapter(FragmentActivity activity, List<T> dataSource) {
        super(activity, dataSource);
    }

    public DataBindRecyclerAdapter(FragmentActivity activity) {
        super(activity);
    }

    @Override
    public void onBindMyViewHolder(BaseViewHolder<T> holder, T dataItem, int position) {
        DataBindViewHolder<T, K> myHolder = (DataBindViewHolder<T, K>) holder;
        myHolder.dataItem = dataItem;
        onBindBindingHolder(myHolder, dataItem, position);
    }

    @Override
    public BaseViewHolder<T> onCreateMyViewHolder(ViewGroup parent, int viewType) {
        return onCreateBindingHolder(parent, viewType);
    }

    public abstract void onBindBindingHolder(DataBindViewHolder<T, K> holder, T dataItem, int position);

    public abstract DataBindViewHolder onCreateBindingHolder(ViewGroup parent, int viewType);

}
