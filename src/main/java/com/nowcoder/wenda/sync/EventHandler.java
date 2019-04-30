package com.nowcoder.wenda.sync;

import java.util.List;

public interface EventHandler {
    void doHandle(EventModel model);

    List<EventType> getSupportEventType();
}
