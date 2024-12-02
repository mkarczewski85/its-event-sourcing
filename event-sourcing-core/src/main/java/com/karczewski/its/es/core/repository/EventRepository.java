package com.karczewski.its.es.core.repository;

import com.karczewski.its.es.core.domain.event.Event;
import com.karczewski.its.es.core.domain.event.EventWithId;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface EventRepository {

    <T extends Event> EventWithId<T> appendEvent(Event event);

    Collection<EventWithId<Event>> readEvents(UUID aggregateId, Integer fromVersion, Integer toVersion);

    List<EventWithId<Event>> readEventsAfterCheckpoint(String aggregateType,
                                                       BigInteger lastProcessedTransactionId,
                                                       long lastProcessedEventId);

}
