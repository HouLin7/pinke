package com.gome.work.common.widget;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;

import com.gome.work.common.R;
import com.gome.work.common.adapter.PopMenuRecyclerAdapter;

import java.util.List;

public class SlideFromBottomPopup extends MyBasePopupWindow {

    private PopMenuRecyclerAdapter mAdapter;

    private View animationView;

    public SlideFromBottomPopup(Context context, List<String> menuList) {
        super(context);
        if (mAdapter != null) {
            mAdapter.setItemList(menuList);
        }
    }

    @Override
    public View onCreateContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.popup_slide_from_bottom_listview, null);
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        PopMenuRecyclerAdapter adapter = new PopMenuRecyclerAdapter((FragmentActivity) getContext());
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onMenuItemClickListener != null) {
                    onMenuItemClickListener.onMenuItemClick(position);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        animationView = recyclerView;
        mAdapter = adapter;
        return contentView;
    }

    @Override
    protected View onCreateAnimateView() {
        return animationView;
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }

}
