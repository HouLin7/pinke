package com.gome.work.common.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by chaergongzi on 2017/8/1.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    protected FragmentActivity mActivity;

    protected List<T> mDataList;

    private SearchFilter mFilter;

    public BaseRecyclerAdapter(FragmentActivity activity, List<T> dataSource) {
        mActivity = activity;
        mDataList = dataSource;
        if (activity == null) {
            throw new AndroidRuntimeException("activity can not be null");
        }
    }

    public BaseRecyclerAdapter(FragmentActivity activity) {
        this(activity, new ArrayList<T>());
    }

    public void clearData() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public void addItem(T t) {
        mDataList.add(t);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeItem(T t) {
        int index = mDataList.indexOf(t);
        removeItem(index);
    }

    public void removeItem(int position) {
        if (position >= 0 && position < getItemCount()) {
            if (position != getItemCount()) {
                notifyItemRangeRemoved(position, getItemCount() - position);
            }
            mDataList.remove(position);
        }
    }

    public List<T> getAllItems() {
        return new ArrayList<>(mDataList);
    }

    public boolean isEmpty() {
        return mDataList == null || mDataList.isEmpty();
    }

    public void notifyItemChanged(T t) {
        int index = mDataList.indexOf(t);
        notifyItemChanged(index);
    }

    public void setItemList(List<T> t) {
        mDataList.clear();
        if (t != null) {
            mDataList.addAll(t);
        }
        notifyDataSetChanged();
    }

    public void appendItemList(List<T> t) {
        mDataList.addAll(t);
        notifyDataSetChanged();
    }


    public int getItemIndex(T t) {
        return mDataList.indexOf(t);
    }

    private AdapterView.OnItemClickListener mOnItemClickListener;

    private AdapterView.OnItemLongClickListener mOnItemLongClickListener;

    /**
     * 是否可点击
     *
     * @param position
     * @return
     */
    public boolean isEnable(int position) {
        return true;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener clickListener) {
        mOnItemClickListener = clickListener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener clickListener) {
        mOnItemLongClickListener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder rv = onCreateMyViewHolder(parent, viewType);
        rv.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                if (mOnItemClickListener != null && isEnable(position)) {
                    mOnItemClickListener.onItemClick(null, view, position, getItemId(position));
                }
            }
        });

        rv.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = (int) view.getTag();
                if (mOnItemLongClickListener != null && isEnable(position)) {
                    mOnItemLongClickListener.onItemLongClick(null, view, position, getItemId(position));
                }
                return true;
            }
        });
        return rv;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        onBindMyViewHolder(holder, mDataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public T getItem(int position) {
        if (position >= 0 && position < mDataList.size()) {
            return mDataList.get(position);
        }
        return null;
    }

    public abstract RecyclerView.ViewHolder onCreateMyViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindMyViewHolder(RecyclerView.ViewHolder holder, T dataItem, int position);


    public boolean matchFilter(T t, String keyword) {
        return false;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new SearchFilter();
        }
        return mFilter;
    }

    /**
     * 恢复到删选之间的数据
     */
    public void resetFilter() {
        if (mFilter != null) {
            if (mFilter.originalData != null) {
                mDataList.clear();
                mDataList.addAll(mFilter.originalData);
                notifyDataSetChanged();
            }
        }
    }

    class SearchFilter extends Filter {

        List<T> originalData = null;

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (originalData == null) {
                originalData = new ArrayList<>(mDataList);
            }

            if (TextUtils.isEmpty(constraint)) {
                filterResults.values = originalData;
            } else {
                String filterString = constraint.toString().trim().toLowerCase(Locale.CHINA);
                List<T> newValues = new ArrayList<>();
                for (T item : originalData) {
                    if (matchFilter(item, filterString)) {
                        newValues.add(item);
                    }
                }
                filterResults.values = newValues;
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mDataList = (List<T>) results.values;
            notifyDataSetChanged();
        }

    }

}
