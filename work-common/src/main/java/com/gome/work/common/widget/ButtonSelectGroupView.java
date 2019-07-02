
package com.gome.work.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gome.work.common.R;
import com.gome.work.common.adapter.SelectRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ButtonSelectGroupView extends HorizontalScrollView {

    private SelectRecyclerAdapter.OnItemSelectedChangeListener<Integer> outerClickListener;

    private List<View> mItemViews = new ArrayList<>();

    private ViewGroup mContainerView;

    private int mCurrSelectIndex = 0;

    private int spacing = 10;

    private float density = 1;

    public ButtonSelectGroupView(Context context) {
        super(context);
    }

    public ButtonSelectGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = context.getResources().getDisplayMetrics().density;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleBarView);
        spacing = typedArray.getResourceId(R.styleable.ButtonSelectGroupView_itemSpacing, (int) (10 * density));
    }

    public ButtonSelectGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        density = context.getResources().getDisplayMetrics().density;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleBarView);
        spacing = typedArray.getResourceId(R.styleable.ButtonSelectGroupView_itemSpacing, (int) (10 * density));
    }


    private void initView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        addView(ll, layoutParams);
        mContainerView = ll;
    }


    public void setOnItemSelectListener(SelectRecyclerAdapter.OnItemSelectedChangeListener<Integer> listener) {
        this.outerClickListener = listener;
    }

    public void setDataList(List<String> dataList) {
        String[] items = new String[dataList.size()];
        dataList.toArray(items);
        setDataList(items);
    }

    public int getSelectIndex() {
        return mCurrSelectIndex;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    public void setDataList(String[] dataList) {
        int index = 0;
        for (String item : dataList) {
            View childView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_filter_condition, null);
            final TextView textView = childView.findViewById(R.id.tv_name);
            textView.setText(item);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newIndex = (int) v.getTag();
                    if (outerClickListener != null) {
                        if (newIndex != mCurrSelectIndex) {
                            outerClickListener.onSelectedChanged(newIndex, true);
                        }
                    }
                    mCurrSelectIndex = newIndex;
                    refreshView();
                }
            });
            textView.setTag(index);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            if (index > 0) {
                layoutParams.leftMargin = spacing;
            }
            mContainerView.addView(childView, layoutParams);
            mItemViews.add(childView);
            index++;
        }

        refreshView();
    }


    private void refreshView() {
        int index = 0;
        for (View view : mItemViews) {
            final TextView textView = view.findViewById(R.id.tv_name);
            textView.setSelected(index == mCurrSelectIndex);
            index++;
        }

    }
}
