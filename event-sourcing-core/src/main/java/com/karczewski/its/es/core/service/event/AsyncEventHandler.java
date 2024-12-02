package com.karczewski.its.es.core.service.event;

import com.karczewski.its.es.core.domain.event.Event;
import com.karczewski.its.es.core.domain.event.EventWithId;
import jakarta.annotation.Nonnull;

public interface AsyncEventHandler {

    void handleEvent(EventWithId<Event> event);

    @Nonnull
    String getAggregateType();

    default String getSubscriptionName() {
        return getClass().getName();
    }

}
