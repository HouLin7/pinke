package com.gome.applibrary.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.gome.applibrary.R;
import com.gome.update.ICheckCallback;
import com.gome.update.UpdateHelper;
import com.gome.update.UpdateInfo;
import com.gome.update.UpdateService;
import com.gome.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2014/10/23.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public final static int REQUEST_CODE_REQUEST_PERMISSION = 0x01;

    public final static String EXTRA_DATA = "extra.data";

    protected String mClassName = getClass().getSimpleName();

    protected SharedPreferences mSharedPref;

    private ProgressDialog mProgressDialog;

    protected Handler mBaseHandler = new Handler();

    protected View mWatermarkView;

    private Map<String, Set<IPermissionRequestCallback>> mPermissionCallbacks = new HashMap<>();


    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_str = format.format(new java.util.Date());
        return time_str;
    }

    protected void showWatermask() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getString(R.string.company_copyright_name));
        buffer.append("\n");
        buffer.append(getString(R.string.owner) + getNameForWatermark());
        buffer.append("\n");
        buffer.append(getCurrentTime());
        initWatermarkView(buffer.toString(), Color.argb(29, 99, 99, 99));
    }


    public String getNameForWatermark() {
        return "";
    }

    protected void checkUpdate(final String appCode) {
        UpdateHelper helper = new UpdateHelper(this);
        helper.checkUpdate(appCode, new ICheckCallback() {

            @Override
            public void onGetNewVer(final UpdateInfo info) {
                if (isFinishing()) {
                    return;
                }

                StringBuilder titleBuilder = new StringBuilder();
                titleBuilder.append("发现新版本(");
                titleBuilder.append("V");
                titleBuilder.append(info.versionName);
//                titleBuilder.append(" ");
//                titleBuilder.append(info.fileSize);
                titleBuilder.append(")");

                new AlertDialog.Builder(BaseActivity.this).setMessage(info.content)
                        .setPositiveButton(R.string.confirm, new AlertDialog.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getBaseContext(), UpdateService.class);
                                intent.putExtra(UpdateService.EXTRA_LAUNCHER_CLASS, getClass());
                                intent.putExtra(UpdateService.EXTRA_DOWNLOAD_URL, info.downloadUrl);
                                startService(intent);
                            }
                        }).setTitle(titleBuilder.toString()).setNegativeButton(com.gome.applibrary.R.string.cancel, null).show();
            }

            @Override
            public void onNoLastVer(UpdateInfo info) {

            }

            @Override
            public void onFailed(String code, String errMsg) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        ToastUtil.showToast(this,getClass().getSimpleName());
    }

    /**
     * 请求系统权限回到接口
     */
    public interface IPermissionRequestCallback {

        void onFinished(String permission, boolean isSuccess);
    }

    public String[] filterGrantedPermisson(String[] permissions) {
        List<String> cacheList = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                cacheList.add(permission);
            }
        }
        String[] result = new String[cacheList.size()];
        cacheList.toArray(result);
        return result;

    }

    public void requestPermission(final String[] permissions, final IPermissionRequestCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] newPermissions = filterGrantedPermisson(permissions);
            if (newPermissions.length > 0) {
                requestPermissions(newPermissions, REQUEST_CODE_REQUEST_PERMISSION);
                if (callback != null) {
                    for (String permission : newPermissions) {
                        Set<IPermissionRequestCallback> sets = mPermissionCallbacks.get(permission);
                        if (sets == null) {
                            sets = new ArraySet<>();
                            mPermissionCallbacks.put(permission, sets);
                        }
                        sets.add(callback);
                    }
                }
                return;
            }
        }

        if (callback != null) {
            // 模拟异步回调，故延迟200毫秒
            mBaseHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (String item : permissions) {
                        callback.onFinished(item, true);
                    }
                }
            }, 100);
        }
    }


    public void requestPermission(final String permission, final IPermissionRequestCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{permission}, REQUEST_CODE_REQUEST_PERMISSION);
                if (callback != null) {
                    Set<IPermissionRequestCallback> sets = mPermissionCallbacks.get(permission);
                    if (sets == null) {
                        sets = new ArraySet<>();
                        mPermissionCallbacks.put(permission, sets);
                    }
                    sets.add(callback);
                }
                return;
            }
        }

        if (callback != null) {
            // 模拟异步回调，故延迟200毫秒
            mBaseHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.onFinished(permission, true);
                }
            }, 100);
        }
    }

    public SharedPreferences getSharedPrefer() {
        return mSharedPref;
    }

    public void showProgressDlg() {
        showProgressDlg("", true);
    }

    protected void showProgressDlg(CharSequence text, boolean isCancelable) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        if (TextUtils.isEmpty(text)) {
            text = getString(R.string.loading_hint);
        }
        mProgressDialog.setMessage(text);
        mProgressDialog.setCancelable(isCancelable);
        mProgressDialog.show();
    }

    public void dismissProgressDlg() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public void setMyContentView(View view, boolean isShowWaterMark) {
        super.setContentView(R.layout.activity_frame);
        ViewGroup fl = (ViewGroup) findViewById(R.id.container);
        fl.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWatermarkView = findViewById(R.id.watermark1);
        if (isShowWaterMark) {
            mWatermarkView.setVisibility(View.VISIBLE);
            showWatermask();
        } else {
            mWatermarkView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDlg();
    }

    /**
     * Get a real resource id, declared in integer-arrays
     *
     * @param arrayId
     * @return
     */
    protected int[] getResID(int arrayId) {
        TypedArray ar = getResources().obtainTypedArray(arrayId);
        int len = ar.length();
        int[] resIds = new int[len];
        for (int i = 0; i < len; i++)
            resIds[i] = ar.getResourceId(i, 0);

        ar.recycle();
        return resIds;
    }

    public void showAlertDlg(String text) {
        showAlertDlg(text, null);
    }

    protected void showAlertDlg(String text, AlertDialog.OnClickListener onClickListener) {
        new AlertDialog.Builder(this).setMessage(text).setPositiveButton(R.string.confirm, onClickListener).show();
    }

    private void initWatermarkView(String text, int textColor) {
        TextView txtView = (TextView) mWatermarkView.findViewById(R.id.txtView1);
        txtView.setText(text);
        txtView.setTextColor(textColor);
        txtView = (TextView) mWatermarkView.findViewById(R.id.txtView2);
        txtView.setText(text);
        txtView.setTextColor(textColor);
        txtView = (TextView) mWatermarkView.findViewById(R.id.txtView3);
        txtView.setText(text);
        txtView.setTextColor(textColor);
        txtView = (TextView) mWatermarkView.findViewById(R.id.txtView4);
        txtView.setText(text);
        txtView.setTextColor(textColor);

    }

    protected boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    protected void switchSoftInputState() {
        if (getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void hideSoftInput() {
        if (getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void showSoftInput(View focusView) {
        if (getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(focusView, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void dismissInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (BaseActivity.this.getCurrentFocus() != null) {
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(BaseActivity.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE_REQUEST_PERMISSION == requestCode) {
            int index = 0;
            for (String permission : permissions) {
                Set<IPermissionRequestCallback> callbacks = mPermissionCallbacks.get(permission);
                if (callbacks != null) {
                    for (IPermissionRequestCallback callback : callbacks) {
                        if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                            callback.onFinished(permission, true);
                        } else {
                            callback.onFinished(permission, false);
                        }
                    }
                }
                index++;
            }
        }

    }


    public void showMessage(String message){
        ToastUtil.showToast(this,message);
    }

}
