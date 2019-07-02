package com.gome.work.core.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.gome.core.greendao.TagDataInfoDao;
import com.gome.utils.CommonUtils;
import com.gome.utils.GsonUtil;
import com.gome.utils.LogUtil;
import com.gome.utils.PhoneStateUtils;
import com.gome.work.core.BuildConfig;
import com.gome.work.core.model.dao.TagDataInfo;
import com.gome.work.core.persistence.DaoUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 网站埋点接口工具类
 *
 * @author Administrator
 */
public class GomeBpUtils {

    /**
     * 300
     */

//    public final static String API_URL = "http://10.122.251.9:8081/XC.gif";

    /**
     * 500
     */
//    public final static String API_URL = "http://10.128.11.82:8081/XC.gif";


//    public final static String API_URL = "http://10.112.101.155:8089/xcbatch.gif";

//    public final static String API_URL = "http://10.112.101.155:8089/XC.gif ";

    /**
     * 800
     */
//    public final static String API_URL = "http://wa.laigome.com/XC.gif";

//    public final static String HOST_URL = "http://wa.laigome.com/";

    public final static String HOST_URL_800 = "https://mars.corp.gome.com.cn/im-backstage-data-server/";

    public final static String HOST_URL_500 = "http://im-work.gome.inc/backstage-data-server/";

    public final static String HOST_URL_300 = "http://10.122.2.18:8091/";


    private static DaoUtil mDaoUti;
    private static Context appContext = null;
    private static String imei = null;// 手机唯一标识

    private static Map<String, Long> enterPageTimeMap = new HashMap<String, Long>();// 进入页面时间
    private static String ostype = "android";// 系统类型
    private static String osversion = null;// 系统版本
    private static String lang = null;// 语言
    private static String screen = null;// 分辨率
    private static String deviceModel = null;// 手机型号

    private static String gomeImPkgName = "com.gome.ecloud";
    private static String deviceBrand = null;// 手机品牌
    private static String nettype = null;// 网络类型
    private static String appversion = null;// 应用版本

    public static final String ACTION_CLICK = "Click";
    public static final String ACTION_ENTER = "Enter";
    public static final String ACTION_LEAVE = "Leave";
    public static final String ACTION_EVENT = "Event";

    private static ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    private static Timer mTimer;

    private static ApiService mApiService;

    private static BlockingQueue<TagDataInfo> mTagDataQueue = new LinkedBlockingDeque<>();

    /**
     * 初始化
     * 在userName和pushId都获取到之后进行调用，并且可以多次调用，当新传入的值为空时不会覆盖上一次的值，在调用其他接口前确保已调用该接口
     * <p>
     * 接收埋点数据服务器的请求地址
     *
     * @param context 上下文
     */
    public synchronized static void init(Context context) {
        if (appContext == null) {
            appContext = context;
            imei = PhoneStateUtils.getDeviceID(context);
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.MINUTES)
                .sslSocketFactory(createSSLSocketFactory(), new TrustAllManager());
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .client(builder.build())
                .baseUrl(HOST_URL_800)
                .build();

        mApiService = retrofit.create(ApiService.class);
        mDaoUti = new DaoUtil(context);
        if (mTimer == null) {
            mTimer = new Timer();
            long period = BuildConfig.DEBUG ? 10 * 1000L : 5 * 60 & 1000L;
            mTimer.scheduleAtFixedRate(new ReportDataBatch(), 10 * 1000L, period);
        }
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

    private static Gson buildGson() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setLenient()
                .create();
        return gson;
    }

    /**
     * 进入页面 每次进入一个页面时调用，无预留参数
     *
     * @param module 模块名
     * @param page   页面名
     */
    public static void enterPage(final String module, final String page, final Map<String, Object> extraParams) {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    final String param = createParam(module, page, ACTION_ENTER, extraParams);
                    enterPageTimeMap.put(module + "_" + page, new Date().getTime());
                    synchronized (GomeBpUtils.class) {
                        saveTemp(param);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * 离开页面 每次离开一个页面时调用，在调用该接口前应先调用进入页面的接口，无预留参数
     *
     * @param module 模块名
     * @param page   页面名
     */
    public static void leavePage(String module, String page, Map<String, Object> extraParams) {
        final String param = createParam(module, page, ACTION_LEAVE, extraParams);
        enterPageTimeMap.put(module + "_" + page, new Date().getTime());
        saveTemp(param);
    }

    /**
     * 拼接请求参数
     *
     * @param module
     * @param page
     * @param action
     * @return
     */
    private static String createParam(String module, String page, String action, Map<String, Object> extraParams) {
        Locale locale = appContext.getResources().getConfiguration().locale;
        lang = locale.getLanguage();
        nettype = PhoneStateUtils.getNetConnectType(appContext);
        appversion = CommonUtils.getVersionName(appContext);
        DisplayMetrics display = appContext.getResources().getDisplayMetrics();
        int screenWidth = display.widthPixels;
        int screenHeight = display.heightPixels;
        screen = screenWidth + "*" + screenHeight;
        osversion = Build.VERSION.RELEASE;
        deviceModel = Build.MODEL;
        deviceBrand = Build.BRAND;
        Map<String, Object> paramsData = new HashMap<>();
        paramsData.put("appId", gomeImPkgName);
        paramsData.put("imei", imei);
        paramsData.put("lang", lang);
        paramsData.put("osVer", osversion);
        paramsData.put("osname", "Android");
        paramsData.put("moduleVer", appversion);
        paramsData.put("screen", screen);
        paramsData.put("deviceModel", deviceModel);
        paramsData.put("deviceBrand", deviceBrand);

        paramsData.put("nettype", nettype);
        paramsData.put("screen", screen);
        paramsData.put("pageId", page);
        paramsData.put("moduleId", module);
        paramsData.put("ip", PhoneStateUtils.getIPAddress(appContext));

        paramsData.put("action", action);

        if (extraParams != null) {
            for (Map.Entry<String, Object> item : extraParams.entrySet()) {
                if (!TextUtils.isEmpty(item.getKey())) {
                    paramsData.put(item.getKey(), item.getValue());
                }
            }
        }
        String result = GsonUtil.objectToJson(paramsData);
        return result;
    }

    public interface ApiService {
        @POST("xcAnalysis/batchProcessLog")
        public Call<Void> postData(@Body List<Map<String, String>> params);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param param 请求参数name1=value1&name2=value2
     * @return 所代表远程资源的响应结果
     */
    private static void sendPost(final List<TagDataInfo> dataList, List<Map<String, String>> param) {
        try {
            LogUtil.d("report","report a time");
            Response<Void> response = mApiService.postData(param).execute();
            if (response.isSuccessful()) {
                mDaoUti.getTagDataInfo().deleteInTx(dataList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private synchronized static void saveTemp(String tagData) {
        final TagDataInfo dao = new TagDataInfo();
        dao.setData(tagData);
        dao.setTimestamp(System.currentTimeMillis());
        mTagDataQueue.offer(dao);
    }


    static class ReportDataBatch extends TimerTask {

        @Override
        public void run() {
            try {
                synchronized (GomeBpUtils.class) {
                    mDaoUti.getTagDataInfo().saveInTx(mTagDataQueue);
                    mTagDataQueue.clear();
                }

                List<TagDataInfo> dataList = mDaoUti.getTagDataInfo().queryBuilder()
                        .orderDesc(TagDataInfoDao.Properties.Timestamp).limit(20).list();
                if (dataList == null || dataList.isEmpty()) {
                    return;
                }
                List<Map<String, String>> outputDataList = new ArrayList<>();
                for (TagDataInfo item : dataList) {
                    Map<String, String> maps = GsonUtil.jsonToObject(HashMap.class, item.getData());
                    outputDataList.add(maps);
                }
                sendPost(dataList, outputDataList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
