
package com.gome.work.core.net;

import android.content.Context;
import android.util.Log;

import com.gome.work.core.BuildConfig;
import com.gome.work.core.SystemFramework;
import com.gome.work.core.net.gsonadapter.IntegerDefaultAdapter;
import com.gome.work.core.net.interceptor.HeaderParamsInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GomeOARetrofitManager {

    public static final String RELEASE_BASE_URL = "https://office.gome.com.cn/";

//    public static final String RELEASE_BASE_URL = "http://125.254.153.78/";

    public static final String PRE_RELEASE_BASE_URL = "https://api.baichuan11.com/api/";

    //    public static final String TEST_BASE_URL = "http://10.112.68.100/portal/";
    public static final String TEST_BASE_URL = "http://10.115.88.29:8080/";

//    public static final String TEST_BASE_URL = "http://10.144.38.164:8080/portal/";

    private Retrofit retrofit;

    private static GomeOARetrofitManager sInstance;

    private Context mContext = SystemFramework.getInstance().getGlobalContext();

    private SystemFramework.Environment environment;

    public static GomeOARetrofitManager getInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        synchronized (GomeOARetrofitManager.class) {
            if (sInstance == null) {
                sInstance = new GomeOARetrofitManager();
            }
        }
        return sInstance;
    }

    private static Gson buildGson() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setLenient()
                .registerTypeAdapter(Integer.class, new IntegerDefaultAdapter())
                .registerTypeAdapter(int.class, new IntegerDefaultAdapter())
                .create();
        return gson;
    }

    public void init(SystemFramework.Environment environment) {
        this.environment = environment;
        boolean sslFlag = getBaseUrl().toLowerCase(Locale.US).startsWith("https");
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(getBaseUrl())
                .client(getOkHttpClient(sslFlag))
                .build();
    }

    public String getBaseUrl() {
        switch (environment) {
            case RELEASE:
                return RELEASE_BASE_URL;
            case TEST:
                return TEST_BASE_URL;
            case PRE_RELEASE:
                return PRE_RELEASE_BASE_URL;
            default:
                return RELEASE_BASE_URL;
        }
    }

    private OkHttpClient getOkHttpClient(boolean certificate) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 添加进度的过滤器 必须第一个加入进度过滤器 因为最后进度过滤器会替换response 同时调用source方法
        if (SystemFramework.getInstance().getEnvironment() != SystemFramework.Environment.RELEASE) {
            //正式环境不打印任何请求网络日志
            builder.interceptors().add(getHttpLoggingIntercepter());
        }
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(1, TimeUnit.MINUTES);
        builder.writeTimeout(1, TimeUnit.MINUTES);
        builder.networkInterceptors().add(getPublicParamIntercepter());
        builder.networkInterceptors().add(new HeaderParamsInterceptor());
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        if (certificate) {
            try {
//                String cerFileName = environment == SystemFramework.Environment.RELEASE ? "server_release.cer" : "server_debug.cer";
//                InputStream cerIns = mContext.getAssets().open(cerFileName);
//                SSLSocketFactory sslSocketFactory = SSLFactoryUtil.getSSLSocketFactory(cerIns);
//
//                X509TrustManager x509TrustManager = Platform.get().trustManager(sslSocketFactory);

                SSLSocketFactory sslSocketFactory = SSLFactoryUtil.getTrustAllFactory();
                X509TrustManager x509TrustManager = new SSLFactoryUtil.AllowX509TrustManager();
                builder.sslSocketFactory(sslSocketFactory, x509TrustManager);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return builder.build();
    }

    private HttpLoggingInterceptor getHttpLoggingIntercepter() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (BuildConfig.DEBUG) {
                    Log.i("API", message);
                }
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
    }


    private Interceptor getPublicParamIntercepter() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                HttpUrl.Builder builder = chain
                        .request()
                        .url()
                        .newBuilder();
                HttpUrl interceptedHttpUrl = builder.build();
                return chain.proceed(chain.request().newBuilder().url(interceptedHttpUrl).build());
            }
        };
    }

    /**
     * 获取service实例方法
     *
     * @param service
     * @param <T>
     * @return
     */
    public <T> T getService(Class<T> service) {
        return retrofit.create(service);
    }


}
