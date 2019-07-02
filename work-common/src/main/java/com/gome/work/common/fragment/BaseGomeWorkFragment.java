
package com.gome.work.common.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gome.applibrary.activity.BaseActivity;
import com.gome.utils.ToastUtil;
import com.gome.work.common.activity.BaseGomeWorkActivity;
import com.gome.work.core.event.BaseEventConsumer;
import com.gome.work.core.event.IEventConsumer;
import com.gome.work.core.event.model.EventInfo;
import com.gome.work.core.model.AccessTokenBean;
import com.gome.work.core.utils.SharedPreferencesHelper;

public class BaseGomeWorkFragment extends Fragment {

    public static final String EXTRA_DATA = BaseActivity.EXTRA_DATA;
    private IEventConsumer mEventConsumerHolder;
    protected Handler mBaseHandler = new Handler();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public BaseGomeWorkActivity getMyActivity() {
        return (BaseGomeWorkActivity) getActivity();
    }


    public String getLoginUserId() {
        AccessTokenBean bean = SharedPreferencesHelper.getAccessTokenInfo();
        if (bean != null) {
            return bean.userInfo == null ? "" : bean.userInfo.getId();
        }
        return "";
    }

    public void showProgressDlg() {
        if (getMyActivity() != null) {
            getMyActivity().showProgressDlg();
        }
    }

    public void dismissProgressDlg() {
        if (getMyActivity() != null) {
            getMyActivity().dismissProgressDlg();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    public void setToolbar(Toolbar toolbar) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public void showToast(CharSequence message) {
        if (isAdded() && getActivity() != null) {
            ToastUtil.showToast(getActivity(), message);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mEventConsumerHolder != null) {
            mEventConsumerHolder.detach();
        }
    }

    public void observeEvents(final int... flags) {
        if (mEventConsumerHolder != null) {
            mEventConsumerHolder.detach();
        }

        mEventConsumerHolder = new BaseEventConsumer(getActivity(), flags) {

            @Override
            public void handleEvent(EventInfo event) {
                BaseGomeWorkFragment.this.handleEvent(event);
            }
        };
        mEventConsumerHolder.attach();
    }

    protected void handleEvent(EventInfo event) {

    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }


    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }
}
