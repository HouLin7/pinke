package com.gome.work.core.net;

/**
 * Created by chaergongzi on 2017/7/20.
 */

/**
 * 网络请求结果回调接口
 */
public interface IResponseListener<T> {

    void onError(String code, String message);

    void onSuccess(T result);

}
