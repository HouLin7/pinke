package com.gome.work.core.event;

import com.gome.work.core.event.model.EventInfo;

/**
 * Created by chenhang01 on 2016/11/30.
 */

public interface IEventConsumer {

    void attach();

    void detach();

    void handleEvent(EventInfo event);

}
