package com.gome.work.common.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.AndroidRuntimeException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gome.work.common.R;
import com.gome.work.common.databinding.AdapterPopMenuItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaergongzi on 2017/8/1.
 */

public class PopMenuRecyclerAdapter extends DataBindRecyclerAdapter<String, AdapterPopMenuItemBinding> {

    public PopMenuRecyclerAdapter(FragmentActivity activity) {
        super(activity);
    }


    @Override
    public void onBindBindingHolder(DataBindViewHolder<String, AdapterPopMenuItemBinding> holder, String dataItem, int position) {
        holder.binding.tvName.setText(dataItem);
    }

    @Override
    public DataBindViewHolder onCreateBindingHolder(ViewGroup parent, int viewType) {

        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.adapter_pop_menu_item, parent, false);
        return new DataBindViewHolder(contentView);
    }


}
