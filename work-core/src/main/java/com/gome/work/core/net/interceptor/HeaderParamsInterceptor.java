
package com.gome.work.core.net.interceptor;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.gome.utils.CommonUtils;
import com.gome.utils.PhoneStateUtils;
import com.gome.work.core.SystemFramework;
import com.gome.work.core.utils.SharedPreferencesHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderParamsInterceptor implements Interceptor {

    private Context mContext = SystemFramework.getInstance().getGlobalContext();

    @Override
    public Response intercept(Chain chain)
            throws IOException {
        Request request = chain.request();
        Request.Builder builder = request
                .newBuilder()
                .addHeader("deviceInfo", PhoneStateUtils.getDeviceID(mContext))
                .addHeader("version", CommonUtils.getVersionName(mContext))
                .addHeader("platform", "android");
        if (TextUtils.isEmpty(request.header("Content-Type"))) {
            builder.addHeader("Content-Type", "application/json;charset=utf-8");
        }
        if (SharedPreferencesHelper.isLogin()) {
            builder.addHeader("Authorization", "Bearer " + SharedPreferencesHelper.getAccessToken());
        } else {
            builder.addHeader("Authorization", SharedPreferencesHelper.getRequestToken());
        }

        request = builder.build();
        Response response = chain.proceed(request);
        return response;
    }
}
