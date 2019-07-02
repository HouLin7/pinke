package com.gome.update;

public interface ICheckCallback {

    /**
     * 服务器的最新版本大于当前运行版本一致
     */
    void onGetNewVer(UpdateInfo info);

    /**
     * 服务器的最新版本与当前运行版本一致
     */
    void onNoLastVer(UpdateInfo info);

    /**
     * 请求失败
     * @param code
     * @param errMsg
     */
    void onFailed(String code, String errMsg);

}
