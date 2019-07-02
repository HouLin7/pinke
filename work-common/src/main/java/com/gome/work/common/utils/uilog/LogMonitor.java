package com.gome.work.common.utils.uilog;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

/**
 * Created by songzhiyang on 2017/6/9.
 */

public class LogMonitor {
    private static LogMonitor sInstance = new LogMonitor();
    private HandlerThread mLogThread = new HandlerThread("log");
    private Handler mIoHandler;
//    private static final long TIME_BLOCK = 30L;

    private LogMonitor() {
        mLogThread.start();
        mIoHandler = new Handler(mLogThread.getLooper());
    }

    private static Runnable mLogRunnable = new Runnable() {
        @Override
        public void run() {
            StringBuilder sb = new StringBuilder();
            StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
            for (StackTraceElement s : stackTrace) {
                sb.append(s.toString() + "\n");
            }
            Log.w(BlockDetectByPrinter.LOG_TAG, sb.toString());
        }
    };

    public static LogMonitor getInstance() {
        return sInstance;
    }

    public boolean isMonitor() {
//        return mIoHandler.hasCallbacks(mLogRunnable);
        return true;
    }

    public void startMonitor() {
        mIoHandler.postDelayed(mLogRunnable, BlockDetectByPrinter.TIME_BLOCK);
    }

    public void removeMonitor() {
        mIoHandler.removeCallbacks(mLogRunnable);
    }
}
