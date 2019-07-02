package com.gome.work.core.event;

import com.gome.work.core.event.model.EventInfo;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

/**
 * Created by chenhang01 on 2016/11/30.
 */

public class EventDispatcher {

    private static EventBus eventBus = EventBus.getDefault();

    public static void postEvent(EventInfo event) {
        eventBus.post(event);
    }

    public static void postEvent(int flag) {
        eventBus.post(EventInfo.obtain(flag));
    }

    public static void postEvent(int flag, Serializable data) {
        EventInfo item = new EventInfo(flag, data);
        eventBus.post(item);
    }
}
