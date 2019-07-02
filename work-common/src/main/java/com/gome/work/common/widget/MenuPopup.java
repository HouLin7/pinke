package com.gome.work.common.widget;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;

import com.gome.work.common.R;
import com.gome.work.common.adapter.PopMenuRecyclerAdapter;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;


/**
 * A base class to show a pop view.
 *
 * @author Administrator
 */
public class MenuPopup extends MyBasePopupWindow {

    private PopMenuRecyclerAdapter mAdapter;

    public MenuPopup(Context context, String... menus) {
        this(context, Arrays.asList(menus));
    }

    public MenuPopup(Context context, List<String> menuList) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setAlignBackground(false);
        if (mAdapter != null) {
            mAdapter.setItemList(menuList);
        }
    }

    @Override
    protected Animation onCreateShowAnimation() {
        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new DecelerateInterpolator());
//        set.addAnimation(getScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0));
        set.addAnimation(getDefaultAlphaAnimation());
        return set;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new DecelerateInterpolator());
//        set.addAnimation(getScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0));
        set.addAnimation(getDefaultAlphaAnimation(false));
        return set;
    }

    @Override
    public void showPopupWindow(View v) {
        setOffsetX(-(getWidth() - v.getWidth()) / 2);
        setOffsetY(-v.getHeight() / 2);
        super.showPopupWindow(v);
    }

    @Override
    public View onCreateContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.popup_menu_listview, null);
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new PopMenuRecyclerAdapter((FragmentActivity) getContext());
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onMenuItemClickListener != null) {
                    onMenuItemClickListener.onMenuItemClick(position);
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
        return contentView;
    }


}
