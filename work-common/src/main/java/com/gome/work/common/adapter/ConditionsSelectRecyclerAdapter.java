package com.gome.work.common.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gome.work.common.R;
import com.gome.work.common.databinding.AdapterFilterConditionBinding;
import com.gome.work.core.model.dao.FileItemInfo;

import java.util.List;

/**
 * 按钮形式的 条件选择器
 */

public class ConditionsSelectRecyclerAdapter extends DataBindRecyclerAdapter<String, AdapterFilterConditionBinding> {

    private int mSelectIndex;

    private AdapterView.OnItemClickListener onItemClickListener;

    public ConditionsSelectRecyclerAdapter(FragmentActivity activity) {
        super(activity);
        init();
    }

    public ConditionsSelectRecyclerAdapter(FragmentActivity activity, List<String> dataSource) {
        super(activity, dataSource);
        init();
    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    void init() {
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectIndex = position;
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(parent, view, position, id);
                }
            }
        });
    }

    @Override
    public void onBindBindingHolder(DataBindViewHolder<String, AdapterFilterConditionBinding> holder, String dataItem, int position) {
        holder.binding.tvName.setSelected(position == mSelectIndex);
        holder.binding.tvName.setText(dataItem);
    }

    @Override
    public DataBindViewHolder onCreateBindingHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.adapter_filter_condition, parent, false);
        final DataBindViewHolder<FileItemInfo, AdapterFilterConditionBinding> holder = new DataBindViewHolder(contentView);

        return holder;
    }

}
