package com.gome.work.core.event;

import android.content.Context;

import com.gome.work.core.event.model.EventInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by chenhang01 on 2016/11/30.
 */

public abstract class BaseEventConsumer implements IEventConsumer {

    private int[] eventWhat;

    protected Context context;

    public BaseEventConsumer(Context context, int... eventWhat) {
        this.eventWhat = eventWhat;
        this.context = context;
    }

    @Override
    public final void attach() {
        EventBus.getDefault().register(this);
    }

    @Override
    public final void detach() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConsume(EventInfo info) {
        boolean flag = false;
        if (eventWhat != null) {
            for (int what : eventWhat) {
                if (what == info.what) {
                    flag = true;
                    break;
                }
            }
        }
        if (flag) {
            handleEvent(info);
        }
    }

}
