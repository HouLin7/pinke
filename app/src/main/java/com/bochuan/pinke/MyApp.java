package com.bochuan.pinke;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.gome.work.common.imageloader.ImageLoader;
import com.gome.work.core.SystemFramework;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.*;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.umeng.commonsdk.UMConfigure;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initSDK();
        SystemFramework.getInstance().init(this, SystemFramework.Environment.valueOf(BuildConfig.SERVER_FLAG));
        intViewConfig();
    }

    private void intViewConfig() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.bg_gray, R.color.text_gray);//全局设置主题颜色
                return new MaterialHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.bg_gray, R.color.text_gray);//全局设置主题颜色
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }


    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


    private void initSDK() {
        Logger.addLogAdapter(new AndroidLogAdapter());

        UMConfigure.init(this,"5d10a1d04ca357fa8f000377","umeng",UMConfigure.DEVICE_TYPE_PHONE,"");

//        JPushInterface.init(this)
//        JPushInterface.setDebugMode(BuildConfig.DEBUG);

        ImageLoader.init(this, R.mipmap.ic_launcher, R.drawable.img_default);

        SystemFramework.Environment environment = SystemFramework.getInstance().getEnvironment();


    }


}
