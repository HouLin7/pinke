package com.gome.work.common.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import com.gome.work.common.KotlinViewHolder;
import com.gome.work.common.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 可选择的RecylerView适配器，只支持选择器的布局自定义，以及选择事件的触发范围设定
 */

public abstract class SelectRecyclerAdapter<T, SB extends BaseViewHolder<T>> extends BaseRecyclerAdapter<T> {

    public static final int CHECK_GRAVITY_LEFT = 100;
    public static final int CHECK_GRAVITY_RIGHT = 200;
    public static final int CHECK_GRAVITY_RIGHT_TOP = 300;

    /**
     * 全局相应选择事件
     */
    public static final int SELECT_REGION_GLOBAL = 1;

    /**
     * 仅选择控件相应选择事件
     */
    public static final int SELECT_REGION_LOCAL = 2;

    private Set<T> mSelectItem = new HashSet<>();

    /**
     * 不可点击列表
     */
    private List<T> mDisableList = new ArrayList<>();

    protected OnItemSelectedChangeListener<T> onItemSelectedChangeListener;

    /**
     * 是否是选择状态
     */
    protected boolean isSelectState = false;

    private int checkGravity = CHECK_GRAVITY_LEFT;

    private int selectRegionFlag = SELECT_REGION_GLOBAL;

    private AdapterView.OnItemClickListener onOuterItemClickListener;

    /**
     * {@link #CHECK_GRAVITY_LEFT},{@link #CHECK_GRAVITY_RIGHT},{@link #CHECK_GRAVITY_RIGHT_TOP}
     *
     * @param activity
     * @param gravity  选择图标的位置
     */
    public SelectRecyclerAdapter(FragmentActivity activity, int gravity, int selectRegionFlag) {
        super(activity);
        this.checkGravity = gravity;
        this.selectRegionFlag = selectRegionFlag;
        init();
    }

    public SelectRecyclerAdapter(FragmentActivity activity, int gravity) {
        this(activity, gravity, SELECT_REGION_GLOBAL);
    }

    public SelectRecyclerAdapter(FragmentActivity activity) {
        this(activity, CHECK_GRAVITY_LEFT, SELECT_REGION_GLOBAL);
    }

    private void init() {
        super.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                T t = getItem(position);
                if (isSelectState && isSelectable(t)) {
                    if (SELECT_REGION_GLOBAL == selectRegionFlag) {
                        if (mSelectItem.contains(t)) {
                            mSelectItem.remove(t);
                            if (onItemSelectedChangeListener != null) {
                                onItemSelectedChangeListener.onSelectedChanged(t, false);
                            }
                        } else {
                            mSelectItem.add(t);
                            if (onItemSelectedChangeListener != null) {
                                onItemSelectedChangeListener.onSelectedChanged(t, true);
                            }
                        }
                        notifyItemChanged(t);

                    } else {
                        if (onOuterItemClickListener != null) {
                            onOuterItemClickListener.onItemClick(parent, view, position, id);
                        }
                    }
                } else {
                    if (onOuterItemClickListener != null) {
                        onOuterItemClickListener.onItemClick(parent, view, position, id);
                    }
                }

            }
        });
    }


    public void setOnItemSelectedChangeListener(OnItemSelectedChangeListener<T> onItemSelectedChangeListener) {
        this.onItemSelectedChangeListener = onItemSelectedChangeListener;
    }


    public void setSelectState(boolean flag) {
        isSelectState = flag;
        notifyDataSetChanged();
    }

    /**
     * 清空所有已选择的条目
     */
    public void clearSelectItem() {
        mSelectItem.clear();
        if (isSelectState) {
            notifyDataSetChanged();
        }
    }

    /**
     * 设置特定的条目为选中状态
     * @param items
     * @param isSelect
     */
    public void setItemsSelectState(List<T> items, boolean isSelect) {
        if (items == null) {
            return;
        }
        for (T t : items) {
            if (isSelect) {
                mSelectItem.add(t);
            } else {
                mSelectItem.remove(t);
            }
        }

        if (isSelectState) {
            notifyDataSetChanged();
        }
    }


    public void removeSelectItem(T t) {
        mSelectItem.remove(t);
        notifyDataSetChanged();
    }


    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener clickListener) {
        onOuterItemClickListener = clickListener;
    }


    @Override
    public void onBindMyViewHolder(BaseViewHolder<T> holder, T dataItem, int position) {
        MyViewHolder<T> myHolder = (MyViewHolder<T>) holder;
        myHolder.dataItem = dataItem;
        myHolder.checkBox.setVisibility(isSelectState && isSelectable(dataItem) ? View.VISIBLE : View.GONE);
        myHolder.checkBox.setEnabled(isSelectState && isEnable(position));
        if (isSelectState) {
            if (!isEnable(position)) {
                myHolder.checkBox.setChecked(true);
            } else {
                myHolder.checkBox.setChecked(mSelectItem.contains(dataItem) && isSelectable(dataItem));
            }
        }

        onBindSelectBindingHolder((KotlinViewHolder<T>) myHolder.getTarget(), dataItem);
    }


    /**
     * 是否可选中
     *
     * @param t
     * @return
     */
    protected boolean isSelectable(T t) {
        return true;
    }

    @Override
    public boolean isEnable(int position) {
        return mDisableList == null || mDisableList.isEmpty() || !mDisableList.contains(getItem(position));
    }

    public void setDisableList(List<T> disableList) {
        mDisableList.clear();
        if (disableList != null) {
            mDisableList.addAll(disableList);
        }
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder<T> onCreateMyViewHolder(ViewGroup parent, int viewType) {

        int layoutId;
        switch (checkGravity) {
            case CHECK_GRAVITY_LEFT:
                layoutId = R.layout.adapter_select_frame_list_left;
                break;
            case CHECK_GRAVITY_RIGHT:
                layoutId = R.layout.adapter_select_frame_list_right;
                break;
            case CHECK_GRAVITY_RIGHT_TOP:
                layoutId = R.layout.adapter_select_frame_grid;
                break;
            default:
                layoutId = R.layout.adapter_select_frame_list_left;
                break;
        }

        View view = LayoutInflater.from(mActivity).inflate(layoutId, parent, false);
        final MyViewHolder<T> parentHolder = new MyViewHolder<>(view);

        final KotlinViewHolder<T> childHolder = onCreateSelectBindingHolder(parent, viewType);
        parentHolder.setTarget(childHolder);

        parentHolder.frameView.addView(childHolder.getContainerView());

        parentHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();
                boolean isEnable = isEnable(getItemIndex(parentHolder.dataItem));
                if (isSelectState && isEnable) {
                    if (isChecked) {
                        mSelectItem.add(parentHolder.dataItem);
                    } else {
                        mSelectItem.remove(parentHolder.dataItem);
                    }
                    if (onItemSelectedChangeListener != null) {
                        onItemSelectedChangeListener.onSelectedChanged(parentHolder.dataItem, isChecked);
                    }
                }
            }
        });

        return parentHolder;
    }


    public abstract KotlinViewHolder<T> onCreateSelectBindingHolder(ViewGroup parent, int viewType);

    public abstract void onBindSelectBindingHolder(KotlinViewHolder<T> holder, T dataItem);


    public List<T> getSelectItems() {
        List<T> result = new ArrayList<>();
        for (T item : mSelectItem) {
            result.add(item);
        }
        return result;
    }


    /**
     * 获取一个特定格式的选择对象
     *
     * @param converter 对象转换器
     * @param <D>
     * @return
     */
    public <D> List<D> getSpecialSelectItems(IDataConverter<T, D> converter) {
        List<D> result = new ArrayList<>();
        for (T item : mSelectItem) {
            if (converter != null) {
                D d = converter.convert(item);
                if (d != null) {
                    result.add(d);
                }
            }
        }
        return result;
    }

    /**
     * 数据转换接口
     *
     * @param <D>
     * @param <D>
     */
    public interface IDataConverter<T, D> {
        D convert(T t);
    }


    public interface OnItemSelectedChangeListener<T> {
        void onSelectedChanged(T dataItem, boolean isSelect);
    }


    static class MyViewHolder<T> extends BaseViewHolder<T> {

        private CheckBox checkBox;
        private ViewGroup frameView;

        public MyViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            frameView = itemView.findViewById(R.id.frame_view);
        }
    }

}
