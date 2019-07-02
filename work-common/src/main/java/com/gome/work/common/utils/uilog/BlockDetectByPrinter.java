package com.gome.work.common.utils.uilog;

import android.os.Looper;
import android.util.Printer;

/**
 * Created by songzhiyang on 2017/6/9.
 */

public class BlockDetectByPrinter {
    static long TIME_BLOCK;
    static String LOG_TAG = "UIThread";

    public static void start(boolean isRunning,String logTag,long blockTime) {
        TIME_BLOCK = blockTime;
        LOG_TAG = logTag;
        if (!isRunning) {
            return;
        }

        Looper.getMainLooper().setMessageLogging(new Printer() {

            private static final String START = ">>>>> Dispatching";
            private static final String END = "<<<<< Finished";

            @Override
            public void println(String x) {
                if (x.startsWith(START)) {
                    LogMonitor.getInstance().startMonitor();
                }
                if (x.startsWith(END)) {
                    LogMonitor.getInstance().removeMonitor();
                }
            }
        });

    }
}
