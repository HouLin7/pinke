
package com.gome.work.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gome.work.common.R;
import com.gome.work.common.databinding.CustomDialogLoadingBinding;

/**
 * Created by liubomin on 2016/6/14.
 */
public class LoadingDialog extends Dialog {
    private String tip;

    private AnimationDrawable mAnimationDrawable;
    private CustomDialogLoadingBinding mLoadingBinding;

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public LoadingDialog(Context context, int themeResId, String tip) {
        super(context, themeResId);
        this.tip = tip;
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        // 得到加载view
        mLoadingBinding = DataBindingUtil.inflate(inflater, R.layout.custom_dialog_loading, null,
                false);
        mLoadingBinding.tvContent.setText(tip);
        mAnimationDrawable = (AnimationDrawable) mLoadingBinding.ivLoading.getBackground();
        setCanceledOnTouchOutside(false);
        setContentView(mLoadingBinding.getRoot(), new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAnimationDrawable.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAnimationDrawable.start();
    }
}
