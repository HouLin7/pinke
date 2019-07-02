package com.gome.work.common.webview.jsbridge.runner;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.MenuItem;
import android.webkit.WebView;

import com.gome.core.greendao.AppItemBeanDao;
import com.gome.utils.CommonUtils;
import com.gome.utils.GsonUtil;
import com.gome.work.common.activity.QRCodeActivity;
import com.gome.work.common.webview.CommonWebActivity;
import com.gome.work.common.webview.jsbridge.JsActions;
import com.gome.work.common.webview.jsbridge.JsNativeImpl;
import com.gome.work.common.webview.jsbridge.JsTask;
import com.gome.work.common.webview.model.JsMenuInfo;
import com.gome.work.common.webview.model.JsMenuWrapper;
import com.gome.work.core.event.EventDispatcher;
import com.gome.work.core.event.model.BaseParamInfo;
import com.gome.work.core.event.model.EventInfo;
import com.gome.work.core.model.appmarket.AppItemBean;
import com.gome.work.core.persistence.DaoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用js原生方法的实现类
 */

public class JsCommonRunner extends MyBaseJsRunner {

    private WebView mWebView;

    private String mScanQRCodeResult;

    private CommonWebActivity mActivity;

    private String mAppID;

    private AppItemBean mAppItem;

    private DaoUtil mDaoUtil;

    private String mJsBackFunc = "";

    private boolean mIsFaceIdPassed = false;

    private String[] jsActionList = new String[]{
            JsActions.ACTION_ADD_MENU,
            JsActions.ACTION_REMOVE_MENU,
            JsActions.ACTION_SCAN_QRCODE,
            JsActions.ACTION_SET_TITLE_BG_COLOR,
            JsActions.ACTION_SET_BACK_LISTENER,
            JsActions.ACTION_GET_APP,
            JsActions.ACTION_FACE_ID
    };

    public JsCommonRunner(CommonWebActivity activity, WebView webview, String appId) {
        super(activity);
        mWebView = webview;
        mActivity = activity;
        mAppID = appId;
        mDaoUtil = new DaoUtil(activity);
        if (!TextUtils.isEmpty(mAppID)) {
            mAppItem = mDaoUtil.getAppItemBeanDao().queryBuilder().where(AppItemBeanDao.Properties.AppId.eq(appId)).unique();
        }
    }

    @Override
    public String execute(JsTask task) throws InterruptedException, JSONException {
        final JSONObject jsonObject = new JSONObject();
        switch (task.action) {
            case JsActions.ACTION_ADD_MENU:
                final JsMenuWrapper jsMenuWrapper = GsonUtil.jsonToObject(JsMenuWrapper.class, task.param);
                final List<JsMenuInfo> jsMenuInfos = jsMenuWrapper.menuList;
                if (jsMenuInfos != null && !jsMenuInfos.isEmpty()) {

                    mActivity.addMenuList(jsMenuInfos, new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            sendMsgToJs(mWebView, jsMenuWrapper.onMenuClick, String.valueOf(item.getItemId()));
                            return true;
                        }
                    });
                }
                jsonObject.put("code", "0");
                return jsonObject.toString();
            case JsActions.ACTION_REMOVE_MENU:
                mActivity.addMenuList(new ArrayList<JsMenuInfo>(), null);
                break;
            case JsActions.ACTION_SCAN_QRCODE:
                Intent intent = new Intent(mActivity, QRCodeActivity.class);
                mActivity.startActivityForResult(intent, JsNativeImpl.REQUEST_CODE_SCAN_QRCODE);
                synchronized (JsCommonRunner.this) {
                    JsCommonRunner.this.wait();
                }
                jsonObject.put("result", mScanQRCodeResult == null ? "" : mScanQRCodeResult);
                return jsonObject.toString();
            case JsActions.ACTION_SET_TITLE_BG_COLOR:
//                    JSONObject json = new JSONObject(task.param);
//                    final String color = json.optString("color");
//                    mTitleBarView.setBackgroundColor(Color.parseColor(color));
                break;
            case JsActions.ACTION_SET_BACK_LISTENER:
                mJsBackFunc = task.param;
                break;
            case JsActions.ACTION_GET_APP:
                jsonObject.put("versionName", CommonUtils.getVersionName(mActivity));
                jsonObject.put("versionCode", CommonUtils.getVersionCode(mActivity));
                break;
            case JsActions.ACTION_FACE_ID:
                BaseParamInfo paramInfo = new BaseParamInfo(mActivity);
                paramInfo.requestCode = JsNativeImpl.REQUEST_CODE_FACE_ID;
                EventDispatcher.postEvent(EventInfo.FLAG_FACE_ID_REQUEST, paramInfo);
                synchronized (JsCommonRunner.this) {
                    JsCommonRunner.this.wait();
                }
                jsonObject.put("result", mIsFaceIdPassed);
                break;
            default:
                break;
        }
        return jsonObject.toString();
    }


    public boolean onBackPressed() {
        if (!TextUtils.isEmpty(mJsBackFunc)) {
            sendMsgToJs(mWebView, mJsBackFunc, "");
            return true;
        }
        return false;
    }

    @Override
    public String[] getActionList() {
        return jsActionList;
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == JsNativeImpl.REQUEST_CODE_SCAN_QRCODE) {
            if (resultCode == Activity.RESULT_OK) {
                mScanQRCodeResult = data.getStringExtra(CommonWebActivity.EXTRA_DATA);
            }
            synchronized (JsCommonRunner.this) {
                JsCommonRunner.this.notify();
            }
            return true;
        } else if (requestCode == JsNativeImpl.REQUEST_CODE_FACE_ID) {
            mIsFaceIdPassed = resultCode == Activity.RESULT_OK;
            synchronized (JsCommonRunner.this) {
                JsCommonRunner.this.notify();
            }
            return true;
        }
        return super.onActivityResult(requestCode, resultCode, data);
    }


}
