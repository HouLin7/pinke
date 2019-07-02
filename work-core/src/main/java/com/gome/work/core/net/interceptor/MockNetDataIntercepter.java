
package com.gome.work.core.net.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by songzhiyang on 2017/2/7.
 */

public class MockNetDataIntercepter implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;
        Response.Builder responseBuilder = new Response.Builder()
                .code(200)
                .message("")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .addHeader("content-type", "application/json");
        Request request = chain.request();
        if (request.url().toString().contains("check_version")) { // 拦截指定地址
            String responseString = "{\"code\":0,\"msg\":\"OK\",\"data\":{\"downloadPath\":\"https://work-d.gomeplus.com/download/android/GomeWork_1_1_0.apk\",\"updateVersion\":\"1.1.0\",\"description\":\"Android1.1.0新版本\",\"title\":\"1.1.0新版本\",\"isNeedUpdate\":true,\"content\":\"Android1.1.0新版本\",\"updateType\":2}}";
            responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"),
                    responseString.getBytes()));// 将数据设置到body中
            response = responseBuilder.build(); // builder模式构建response
        } else {
            response = chain.proceed(request);
        }
        return response;
    }
}
