package com.gome.work.core.persistence;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Create by liupeiquan on 2018/11/20
 */
public class ThreadPoolFactory {

    private static ExecutorService executorForIO = null;

    public static synchronized ExecutorService getIoExecutor() {
        if (executorForIO == null) {
            executorForIO = Executors.newSingleThreadExecutor();
        }
        return executorForIO;
    }


}
