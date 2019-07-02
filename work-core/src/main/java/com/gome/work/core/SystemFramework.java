
package com.gome.work.core;

import android.app.Application;
import android.content.Context;

import com.gome.work.core.net.GomeOARetrofitManager;


/**
 * 系统工作框架集
 *
 * @author kongtao
 */
public class SystemFramework {

    private Context globalContext;// 应用全局实例句柄

    private Environment environment;

    private static SystemFramework instance = null;

    public static SystemFramework getInstance() {

        if (instance == null) {
            synchronized (SystemFramework.class) {
                if (instance == null) {
                    instance = new SystemFramework();
                }
            }
        }
        return instance;
    }

    public void init(Application app, Environment environment) {
        this.environment = environment;
        this.globalContext = app;
        GomeOARetrofitManager.getInstance().init(environment);
    }


    public Context getGlobalContext() {
        return globalContext;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public enum Environment {
        TEST,//300
        PRE_RELEASE,//预生产环境500
        RELEASE//800
    }


}
