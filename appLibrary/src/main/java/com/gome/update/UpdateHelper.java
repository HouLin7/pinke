package com.gome.update;

import android.app.Activity;
import android.content.Context;

import com.gome.applibrary.R;
import com.gome.utils.CommonUtils;
import com.gome.utils.PhoneStateUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class UpdateHelper {

    public static final String RELEASE_BASE_URL = "https://api.baichuan11.com/app/version";

    public static final String DEV_BASE_URL = "http://10.112.68.100/portal/";

    private Activity mActivity;

    private ApiService mApiService;

    public UpdateHelper(Activity activity) {
        mActivity = activity;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.MINUTES)
                .sslSocketFactory(createSSLSocketFactory(), new TrustAllManager());

        builder.addInterceptor(new HeaderParamsInterceptor(activity));
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .client(builder.build())
                .baseUrl(RELEASE_BASE_URL)
                .build();
        mApiService = retrofit.create(ApiService.class);
    }


    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory sSLSocketFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllManager()},
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sSLSocketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {


        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    public void checkUpdate(String appCode, final ICheckCallback callback) {
        mApiService.checkUpdate(appCode).enqueue(new Callback<BaseRspInfo<UpdateInfo>>() {
            @Override
            public void onResponse(Call<BaseRspInfo<UpdateInfo>> call, Response<BaseRspInfo<UpdateInfo>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        if (CommonUtils.getVersionCode(mActivity) < response.body().data.versionId) {
                            callback.onGetNewVer(response.body().data);
                        } else {
                            callback.onNoLastVer(response.body().data);
                        }
                    } else {
                        callback.onFailed(response.body().code, response.body().message);
                    }
                } else {
                    callback.onFailed("", mActivity.getResources().getString(R.string.network_error));
                }
            }

            @Override
            public void onFailure(Call<BaseRspInfo<UpdateInfo>> call, Throwable t) {
                callback.onFailed("", mActivity.getResources().getString(R.string.network_error));
            }
        });
    }

    private static Gson buildGson() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setLenient()
                .create();
        return gson;
    }


    public interface ApiService {
        @GET("app_shop/app/update/{appCode}.do")
        public Call<BaseRspInfo<UpdateInfo>> checkUpdate(@Path("appCode") String appCode);
    }

    public class HeaderParamsInterceptor implements Interceptor {

        private Context mContext;

        public HeaderParamsInterceptor(Context context) {
            mContext = context;
        }

        @Override
        public okhttp3.Response intercept(Chain chain)
                throws IOException {
            Request request = chain.request();
            request = request
                    .newBuilder()
                    .addHeader("Content-Type", "application/json;charset=utf-8")
                    .addHeader("deviceInfo", PhoneStateUtils.getDeviceID(mContext))
                    .addHeader("version", CommonUtils.getVersionName(mContext))
                    .addHeader("platform", "android")
                    .build();

            okhttp3.Response response = chain.proceed(request);
            return response;
        }
    }


}
