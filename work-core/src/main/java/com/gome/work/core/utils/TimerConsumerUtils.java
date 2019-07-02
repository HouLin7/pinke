package com.gome.work.core.utils;

/**
 * Created by libo on 2017/10/17.
 */

public class TimerConsumerUtils {
    private static final ThreadLocal<Long> TIME_THREADLOCAL=new ThreadLocal<Long>(){
        @Override
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };

    public static final void begin(){
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    public static final long end(){
        return System.currentTimeMillis()-TIME_THREADLOCAL.get();
    }

}
