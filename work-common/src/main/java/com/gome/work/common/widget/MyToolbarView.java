package com.gome.work.common.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.gome.work.common.R;


/**
 * Created by chaergongzi on 2018/7/18.
 */

public class MyToolbarView extends Toolbar {

    private Handler mHandler = new Handler();

    public MyToolbarView(Context context) {
        super(context);
    }

    public MyToolbarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyToolbarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        setContentInsetStartWithNavigation(0);
        setNavigationIcon(R.drawable.ic_title_back);

        View view = findViewById(R.id.tv_title_center);
        if (view != null) {
            view.setVisibility(View.GONE);
        }

        view = findViewById(R.id.iv_close_icon);
        if (view != null) {
            view.setVisibility(View.GONE);
        }

    }

    public void bindActivity(final AppCompatActivity activity) {
        bindActivity(activity, getTitle());
    }

    public void bindActivity(final AppCompatActivity activity, CharSequence title) {
        setTitle(title);
        activity.setSupportActionBar(this);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }


    public void setMiddleTitle(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                setTitle("");
                TextView textView = findViewById(R.id.tv_title_center);
                if (textView == null) {
                    return;
                }
                textView.setVisibility(View.VISIBLE);
                textView.setText(text);
            }
        });
    }


    public void showCloseIcon(final Activity activity) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                View view = findViewById(R.id.iv_close_icon);
                if (view == null) {
                    return;
                }
                view.setVisibility(View.VISIBLE);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                });
            }
        });

    }
//
//    public void setRightText(CharSequence text) {
//        setRightText(text, null);
//    }
//
//    public void setRightText(final CharSequence text, final View.OnClickListener onClickListener) {
//        TextView textView = findViewById(R.id.tv_title_right);
//        if (textView == null) {
//            return;
//        }
//        textView.setTextColor(getResources().getColor(R.color.app_red));
//        textView.setText(text);
//        textView.setVisibility(View.VISIBLE);
//        textView.setOnClickListener(onClickListener);
//
//    }


}
