
package com.gome.work.core.net;


import com.gome.work.core.Constants;
import com.gome.work.core.model.*;
import com.gome.work.core.model.im.*;
import com.gome.work.core.model.schedule.ScheduleInfo;
import com.gome.work.core.model.schedule.ScheduleRemindInfo;
import com.gome.work.core.net.api.ApiService;
import com.gome.work.core.upload.FileUploadManager;
import com.gome.work.core.upload.IUploadListener;
import com.gome.work.core.utils.Base64;
import com.gome.work.core.utils.SharedPreferencesHelper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class WebApiImpl extends WebApi {

    private ApiService service;


    public WebApiImpl() {
        service = GomeOARetrofitManager.getInstance().getService(ApiService.class);
    }

    @Override
    public void login(String account, String password, IResponseListener<AccessTokenBean> listener) {
        String authStr = account + "|" + password;
        String base64Str = "Basic " + Base64.encode(authStr.getBytes());
        SharedPreferencesHelper.commitString(Constants.PreferKeys.REQUEST_TOKEN, base64Str);
        Call<BaseRspInfo<AccessTokenBean>> result = service.login();
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void addFriends(UserInfo user, IResponseListener<String> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", user.getId() + "");

        Call<BaseRspInfo<String>> result = service.addFriend(params);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void removeFriends(UserInfo user, IResponseListener<String> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", user.getId() + "");
        Call<BaseRspInfo<String>> result = service.removeFriend(params);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void getUserDetail(String userId, IResponseListener<UserInfo> listener) {
        Call<BaseRspInfo<UserInfo>> result = service.getUserDetail(userId);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void updateUserAvatar(File file, IUploadListener<String> listener) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        RequestBody requestProgressFile = new FileUploadManager.FileRequestBody(requestFile, listener);
        MultipartBody.Part body = MultipartBody.Part.createFormData("myfile", file.getName(), requestProgressFile);
        Call<BaseRspInfo<String>> result = service.updateUserAvatar(body);
        result.enqueue(new MyCallback(listener));
    }



    @Override
    public void commitCommentData(String appId, int score, String content, IResponseListener<String> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", appId);
        params.put("score", score + "");
        params.put("content", content);
        Call<BaseRspInfo<String>> result = service.submitCommentData(params);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void getAppFavoriteData(String appId, IResponseListener<String> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", appId);
        Call<BaseRspInfo<String>> result = service.getAppFavoriteData(params);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void cancelAppFavoriteData(String appId, IResponseListener<String> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", appId);
        Call<BaseRspInfo<String>> result = service.cancelAppFavoriteData(params);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void getAppPraiseData(String appId, IResponseListener<String> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", appId);
        Call<BaseRspInfo<String>> result = service.getAppPraisData(params);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void cancelAppPraisData(String appId, IResponseListener<String> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", appId);
        Call<BaseRspInfo<String>> result = service.cancelAppPraisData(params);
        result.enqueue(new MyCallback(listener));
    }


    @Override
    public void updateFavoriteSort(List<FavoriteSortBean> list, IResponseListener<String> listener) {
//        String json = new Gson().toJson(list);
//        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"), json);
//        Call<BaseRspInfo<String>> result = service.updateFavoriteSort(requestBody);
        Call<BaseRspInfo<String>> result = service.updateFavoriteSort(list);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void getBannerList(IResponseListener<List<BannerBean>> listener) {
        Call<BaseRspInfo<List<BannerBean>>> result = service.getBannerList();
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void getAdData(IResponseListener<AdBean> listener) {
        Call<BaseRspInfo<AdBean>> result = service.getAdData();
        result.enqueue(new MyCallback(listener));
    }


    @Override
    public void uploadFile(File file, IUploadListener<UploadFileResultInfo> listener) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        RequestBody requestProgressFile = new FileUploadManager.FileRequestBody(requestFile, listener);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestProgressFile);
        Call<BaseRspInfo<UploadFileResultInfo>> result = service.uploadFile(body);
        result.enqueue(new MyCallback(listener));

    }


    @Override
    public void getDataSource(String userName, IResponseListener<List<DataSourceItem>> listener) {
        Call<BaseRspInfo<List<DataSourceItem>>> result = service.getDataSource(userName);
        result.enqueue(new MyCallback(listener));
    }


    @Override
    public void getIMChatGroupMenberList(String groupId, long lastPullTimestamp, int status, int page, int pageSize, IResponseListener<GroupMemberInfo> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("lastPullTimestamp", lastPullTimestamp + "");
        params.put("status", status + "");
        params.put("page", page + "");
        params.put("pageSize", pageSize + "");
        Call<BaseRspInfo<GroupMemberInfo>> result = service.getIMChatGroupMenberList(params);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void getImGroupInfo(String groupId, IResponseListener<GroupInfo> listener) {
        Call<BaseRspInfo<GroupInfo>> result = service.getImGroupInfo(groupId);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void getImGroupInfoByQrCode(String qrcode, IResponseListener<GroupInfo> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("qrCode", qrcode);
        Call<BaseRspInfo<GroupInfo>> result = service.getImGroupInfoByQrcode(params);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void imCreateGroupChat(String groupName, List<UserInfo> list, IResponseListener<GroupInfo> listener) {
        if (list != null && !list.isEmpty()) {
            List<EditGroupInfo> groupInfoList = new ArrayList<>();
            for (UserInfo info : list) {
                EditGroupInfo editGroupInfo = new EditGroupInfo();
                editGroupInfo.setUserId(info.getId());
                editGroupInfo.setNickName(info.getName());
                groupInfoList.add(editGroupInfo);
            }
            GroupSetRequestInfo info = new GroupSetRequestInfo();
            info.setGroupName(groupName);
            info.setMemberInfos(groupInfoList);
            Call<BaseRspInfo<GroupInfo>> result = service.imCreateGroupChat(info);
            result.enqueue(new MyCallback(listener));
        }

    }

    @Override
    public void imUpdateGroupChatName(String groupName, String groupId, IResponseListener<GroupInfo> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("groupName", groupName);
        params.put("groupId", groupId);
        Call<BaseRspInfo<GroupInfo>> result = service.imUpdateGroupChatName(params);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void imGroupAddMembers(String groupId, List<UserInfo> list, IResponseListener<String> listener) {
        if (list != null && !list.isEmpty()) {
            List<EditGroupInfo> groupInfoList = new ArrayList<>();
            for (UserInfo info : list) {
                EditGroupInfo editGroupInfo = new EditGroupInfo();
                editGroupInfo.setUserId(info.getId());
                editGroupInfo.setNickName(info.getName());
                groupInfoList.add(editGroupInfo);
            }
            GroupSetRequestInfo bean = new GroupSetRequestInfo();
            bean.setGroupId(groupId);
            bean.setMemberInfos(groupInfoList);
            Call<BaseRspInfo<String>> result = service.imGroupAddMembers(bean);
            result.enqueue(new MyCallback(listener));
        }
    }

    @Override
    public void imJoinGroup(String groupId, String nickName, IResponseListener<String> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("nickName", nickName);
        Call<BaseRspInfo<String>> result = service.imJoinGroup(params);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void imQuitGroup(String groupId, IResponseListener<String> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("groupId", groupId);
        Call<BaseRspInfo<String>> result = service.imQuitGroup(params);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void imRemoveMemberByGroup(String groupId, List<UserInfo> list, IResponseListener<String> listener) {
        if (list != null && !list.isEmpty()) {
            List<EditGroupInfo> groupInfoList = new ArrayList<>();
            for (UserInfo info : list) {
                EditGroupInfo editGroupInfo = new EditGroupInfo();
                editGroupInfo.setUserId(info.getId());
                editGroupInfo.setNickName(info.getName());
                groupInfoList.add(editGroupInfo);
            }
            GroupSetRequestInfo bean = new GroupSetRequestInfo();
            bean.setGroupId(groupId);
            bean.setMemberInfos(groupInfoList);
            Call<BaseRspInfo<String>> result = service.imRemoveMemberByGroup(bean);
            result.enqueue(new MyCallback(listener));
        }
    }

    @Override
    public void imDissolveGroup(String groupId, String nickName, IResponseListener<String> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("groupId", groupId);
//        params.put("nickName", nickName);
        Call<BaseRspInfo<String>> result = service.imDissolveGroup(params);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void imCreateGroupQrcode(String groupId, IResponseListener<GroupQrcode> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("groupId", groupId);
        Call<BaseRspInfo<GroupQrcode>> result = service.imCreateGroupQrcode(params);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void imJoinGroupByQrcode(String qrCode, IResponseListener<GroupInfo> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("qrCode", qrCode);
        Call<BaseRspInfo<GroupInfo>> result = service.imJoinGroupByQrcode(params);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void imAlterationGroupOwner(String groupId, String imUserId, IResponseListener<String> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("imUserId", imUserId);
        Call<BaseRspInfo<String>> result = service.imAlterationGroupOwner(params);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void imModifyGroupNotice(String groupId, String noticeContent, IResponseListener<String> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("noticeContent", noticeContent);
        Call<BaseRspInfo<String>> result = service.imModifyGroupNotice(params);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void imGetGroupNotice(String groupId, IResponseListener<GroupNoticeBean> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("groupId", groupId);
        Call<BaseRspInfo<GroupNoticeBean>> result = service.imGetGroupNotice(params);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void imGetNoticeList(String appId, long since_id, long max_id, int count, IResponseListener<List<NoticeInfo>> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", appId);
        if (since_id > 0) {
            params.put("since_id", String.valueOf(since_id));
        }
        if (since_id > 0) {
            params.put("max_id", String.valueOf(max_id));
        }
        if (count > 0) {
            params.put("count", String.valueOf(count));
        }

        Call<BaseRspInfo<List<NoticeInfo>>> result = service.imGetNoticeList(params);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void imGetRefreshToken(IResponseListener<IMLoginBean> listener) {

        Call<BaseRspInfo<IMLoginBean>> result = service.imGetRefreshToken();
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void getBackLogList(IResponseListener<List<BackLogItem>> listener) {
        Call<BaseRspInfo<List<BackLogItem>>> result = service.getBackLogListData();
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void getCompleteTaskList(IResponseListener<List<BackLogItem>> listener) {
        Call<BaseRspInfo<List<BackLogItem>>> result = service.getCompleteTaskList();
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void getRequestGrant(String requestToken, IResponseListener<RequestGrantBean> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("request_token", requestToken);
        Call<BaseRspInfo<RequestGrantBean>> result = service.getRequestGrant(map);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void grantAccessToken(String requestToken, String captcha, IResponseListener<RequestGrantBean> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("request_token", requestToken);
        map.put("captcha", captcha);
        Call<BaseRspInfo<RequestGrantBean>> result = service.GrantAccessToken(map);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void getRequestGeantLogin(String requestToken, String captcha, IResponseListener<AccessTokenBean> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("request_token", requestToken);
        map.put("captcha", captcha);
        Call<BaseRspInfo<AccessTokenBean>> result = service.getRequestGeantLogin(map);
        result.enqueue(new MyCallback(listener));
    }

    @Override
    public void getAppCategory(IResponseListener<List<CategoryBean>> listener) {
        Call<BaseRspInfo<List<CategoryBean>>> result = service.getAppCategory();
        result.enqueue(new MyCallback(listener));
    }


    @Override
    public void getScheduleDateList(int userId, String startDate, String endDate, IResponseListener<List<String>> listener) {
        Call<BaseRspInfo<List<String>>> call = service.getScheduleDateList(userId, startDate, endDate);
        call.enqueue(new MyCallback(listener));
    }

    @Override
    public void saveOrUpdateSchedule(ScheduleInfo data, IResponseListener<String> listener) {
        Map<String, Object> params = new HashMap<>();
        if (data.getId() > 0) {
            params.put("id", data.getId());
        }
        params.put("userId", data.getUserId());
        params.put("title", data.getTitle());
        params.put("type", data.getType());
        params.put("scheduleTime", data.getScheduleTime());
        params.put("remindValue", data.getRemindValue());
        params.put("validType", data.getValidType());
        if (data.getValidType() > 1) {
            params.put("validValue", data.getValidValue());
        }
        Call<BaseRspInfo<String>> call = service.saveOrUpdateSchedule(params);
        call.enqueue(new MyCallback(listener));
    }

    @Override
    public void getScheduleDetail(int id, IResponseListener<ScheduleInfo> listener) {
        Call<BaseRspInfo<ScheduleInfo>> call = service.getScheduleDetail(id);
        call.enqueue(new MyCallback(listener));
    }

    @Override
    public void getScheduleRemindMinute(IResponseListener<List<ScheduleRemindInfo>> listener) {
        Call<BaseRspInfo<List<ScheduleRemindInfo>>> call = service.getScheduleRemindMinute();
        call.enqueue(new MyCallback(listener));
    }

    @Override
    public void deleteScheduleById(int id, IResponseListener<String> listener) {
        Call<BaseRspInfo<String>> call = service.deleteScheduleById(id);
        call.enqueue(new MyCallback(listener));
    }

    @Override
    public void submitFeedback(FeedBackBean feedBackBean, IResponseListener<String> listener) {
        RequestDataInfo<FeedBackBean> requestDataInfo = new RequestDataInfo<>();
        requestDataInfo.setData(feedBackBean);
        Call<BaseRspInfo<String>> call = service.submitFeedBack(requestDataInfo);
        call.enqueue(new MyCallback(listener));
    }


    static class MyCallback<T> implements Callback<BaseRspInfo<T>> {

        public IResponseListener<T> listener;

        public MyCallback(IResponseListener<T> listener) {
            this.listener = listener;
        }

        @Override
        public void onResponse(Call<BaseRspInfo<T>> call, retrofit2.Response<BaseRspInfo<T>> response) {
            if (listener != null) {
                if (response.isSuccessful()) {
                    BaseRspInfo baseRspInfo = response.body();
                    if (baseRspInfo.isSuccess()) {
                        listener.onSuccess(response.body().data);
                    } else {
                        listener.onError(baseRspInfo.code, baseRspInfo.message);
                    }

                } else {
                    listener.onError(String.valueOf(response.code()), response.message());
                }
            }
        }

        @Override
        public void onFailure(Call<BaseRspInfo<T>> call, Throwable t) {
            if (listener != null) {
                listener.onError("-1", t.getMessage());
            }
        }
    }


}
