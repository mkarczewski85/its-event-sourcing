package com.karczewski.its.es.core.service.event;

import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.event.Event;
import com.karczewski.its.es.core.domain.event.EventWithId;
import jakarta.annotation.Nonnull;

import java.util.Collection;

public interface SyncEventHandler {

    void handleEvents(Collection<EventWithId<Event>> events, Aggregate aggregate);

    @Nonnull
    String getAggregateType();

}
