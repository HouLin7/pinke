package com.gome.work.common.webview.jsbridge.runner;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import com.gome.work.common.activity.BaseGomeWorkActivity;
import com.gome.work.common.webview.jsbridge.JsActions;
import com.gome.work.common.webview.jsbridge.JsNativeImpl;
import com.gome.work.common.webview.jsbridge.JsTask;
import com.gome.work.common.webview.model.JsShareInfo;
import com.gome.work.core.Constants;
import com.gome.work.core.event.EventDispatcher;
import com.gome.work.core.event.model.EventInfo;
import com.gome.work.core.event.model.ToChatWithChatBeanParamInfo;
import com.gome.work.core.event.model.UserChooseParamInfo;
import com.gome.work.core.model.AccessTokenInfo;
import com.gome.work.core.model.ISelectableItem;
import com.gome.work.core.model.UserInfo;
import com.gome.work.core.utils.GsonUtil;
import com.gome.work.core.utils.SharedPreferencesHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定义执行jsTask的接口
 */

public class JsUserRunner extends MyBaseJsRunner {

    private List<UserInfo> mSelectUsers;

    private List<ISelectableItem> mSelectableItems;


    public JsUserRunner(BaseGomeWorkActivity activity) {
        super(activity);
    }

    @Override
    public String execute(JsTask task) throws InterruptedException, JSONException {
        switch (task.action) {
            case JsActions.ACTION_GET_TOKEN:
                JSONObject jsonObject = new JSONObject();
                String token = SharedPreferencesHelper.getAccessToken();
                jsonObject.put("token", token);
                return jsonObject.toString();
            case JsActions.ACTION_GET_USER:
                UserInfo user = SharedPreferencesHelper.getUserDetailInfo();
                AccessTokenInfo tokenInfo = SharedPreferencesHelper.getAccessTokenInfo();
                jsonObject = new JSONObject();
                jsonObject.put("name", user.getNickname());
                jsonObject.put("email", user.getEmail());
                jsonObject.put("userId", tokenInfo.userInfo.getId());
                jsonObject.put("account", SharedPreferencesHelper.getString(Constants.PreferKeys.ACCOUNT));
                return jsonObject.toString();
            case JsActions.ACTION_CHOOSE_USER:
                int type = 1;
                int count = 100;
                if (!TextUtils.isEmpty(task.param)) {
                    JSONObject jsonObj = new JSONObject(task.param);
                    type = jsonObj.optInt("type");
                    count = jsonObj.optInt("max_count", 100);
                }
                UserChooseParamInfo info = new UserChooseParamInfo(mActivity);
                info.chooseModel = type > 0 ? Constants.MODEL_PICK_MULTI : Constants.MODEL_PICK_SINGLE;
                info.maxCount = count;
                info.requestCode = JsNativeImpl.REQUEST_CODE_CHOOSE_USER;
                EventDispatcher.postEvent(EventInfo.FLAG_USER_CHOOSE, info);
                synchronized (JsUserRunner.this) {
                    JsUserRunner.this.wait();
                }
                List<Map<String, String>> response = new ArrayList<>();
                if (mSelectUsers != null) {
                    for (UserInfo userItem : mSelectUsers) {
                        Map<String, String> item = new HashMap<>();
                        item.put("user_id", userItem.getId() + "");
                        item.put("user_name", userItem.getNickname());
                        if (!TextUtils.isEmpty(userItem.getAvatar())) {
                            item.put("user_avatar", userItem.getAvatar());
                        }
                        response.add(item);
                    }
                }
                return GsonUtil.GsonString(response);
            case JsActions.ACTION_SHARE_CONTENT:
                jsonObject = new JSONObject();
                if (TextUtils.isEmpty(task.param) || !handlerShareMsg(task.param)) {
                    jsonObject.put("errorMsg", "未选择");
                } else {
                    jsonObject.put("result", "OK");

                }
                return jsonObject.toString();
            case JsActions.ACTION_CHAT_TO:
                jsonObject = new JSONObject();
//                if (TextUtils.isEmpty(task.param) || !handleChatMsg(task.param)) {
//                    jsonObject.put("errorMsg", "未选择");
//                } else {
//                    jsonObject.put("result", "OK");
//
//                }
                return jsonObject.toString();
            default:
                break;
        }
        return "";

    }


    private boolean handlerShareMsg(String paramData) {
        if (TextUtils.isEmpty(paramData)) {
            return false;
        }

        JsShareInfo jsShareInfo = GsonUtil.jsonToBean(paramData, JsShareInfo.class);
        if (jsShareInfo == null || !jsShareInfo.isValid()) {
            return false;
        }

        UserChooseParamInfo info = new UserChooseParamInfo(mActivity);
        info.chooseModel = Constants.MODEL_PICK_SINGLE;
        info.maxCount = 1;
        info.requestCode = JsNativeImpl.REQUEST_CODE_FORWARDING;
        EventDispatcher.postEvent(EventInfo.FLAG_FORWARDING, info);
        synchronized (JsUserRunner.this) {
            try {
                JsUserRunner.this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (mSelectableItems != null && !mSelectableItems.isEmpty()) {
            ISelectableItem selectableItem = mSelectableItems.get(0);
//            ShareExtraData shareExtraData = new ShareExtraData();
//            shareExtraData.title = jsShareInfo.title;
//            shareExtraData.content = jsShareInfo.content;
//            shareExtraData.linkUrl = jsShareInfo.linkUrl;
//            shareExtraData.imgUrl = jsShareInfo.imageUrl;
//            startChat(selectableItem, shareExtraData);
            return true;
        }
        return false;
    }

    private void startChat(ISelectableItem selectableItem, Serializable extraData) {
        ToChatWithChatBeanParamInfo paramInfo = new ToChatWithChatBeanParamInfo(mActivity);
        paramInfo.chatObj = selectableItem;
        paramInfo.extraData = extraData;
        EventInfo eventInfo;
        eventInfo = EventInfo.obtain(EventInfo.FLAG_IM_TO_CHAT_WITH_USERBEAN);
        eventInfo.data = paramInfo;
        EventDispatcher.postEvent(eventInfo);
    }


    @Override
    public String[] getActionList() {
        return new String[]{JsActions.ACTION_GET_USER,
                JsActions.ACTION_CHOOSE_USER,
                JsActions.ACTION_CHAT_TO,
                JsActions.ACTION_GET_TOKEN};
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == JsNativeImpl.REQUEST_CODE_CHOOSE_USER) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                mSelectUsers = (List<UserInfo>) data.getSerializableExtra(BaseGomeWorkActivity.EXTRA_DATA);
            }
            synchronized (JsUserRunner.this) {
                notify();
            }
            return true;
        } else if (requestCode == JsNativeImpl.REQUEST_CODE_FORWARDING) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                mSelectableItems = (List<ISelectableItem>) data.getSerializableExtra(BaseGomeWorkActivity.EXTRA_DATA);
            }
            synchronized (JsUserRunner.this) {
                notify();
            }
            return true;
        }
        return false;
    }
}
