package com.karczewski.its.es.core.service;

import com.karczewski.its.es.core.domain.event.Event;
import com.karczewski.its.es.core.domain.event.EventSubscriptionCheckpoint;
import com.karczewski.its.es.core.domain.event.EventWithId;
import com.karczewski.its.es.core.repository.EventRepository;
import com.karczewski.its.es.core.repository.EventSubscriptionRepository;
import com.karczewski.its.es.core.service.event.AsyncEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Component
@RequiredArgsConstructor
@Slf4j
public class EventSubscriptionProcessor {

    private final EventSubscriptionRepository subscriptionRepository;
    private final EventRepository eventRepository;

    @Async
    public void processNewEvents(AsyncEventHandler eventHandler) {
        String subscriptionName = eventHandler.getSubscriptionName();
        log.debug("Processing new events for subscription: {}", subscriptionName);
        ensureSubscriptionExists(subscriptionName);
        subscriptionRepository.findCheckpointAndLockSubscription(subscriptionName).ifPresentOrElse(
                checkpoint -> processEventsForSubscription(eventHandler, subscriptionName, checkpoint),
                () -> log.debug("Could not acquire lock for subscription: {}", subscriptionName)
        );
    }

    private void ensureSubscriptionExists(String subscriptionName) {
        subscriptionRepository.createSubscriptionIfAbsent(subscriptionName);
        log.debug("Ensured subscription exists: {}", subscriptionName);
    }

    private void processEventsForSubscription(AsyncEventHandler eventHandler, String subscriptionName,
                                              EventSubscriptionCheckpoint checkpoint) {
        log.debug("Acquired lock for subscription: {}, checkpoint: {}", subscriptionName, checkpoint);
        List<EventWithId<Event>> events = fetchEventsAfterCheckpoint(eventHandler, checkpoint);
        if (events.isEmpty()) {
            log.debug("No new events found for subscription: {}", subscriptionName);
            return;
        }
        log.debug("Fetched {} new event(s) for subscription: {}", events.size(), subscriptionName);
        events.forEach(eventHandler::handleEvent);
        updateSubscriptionCheckpoint(subscriptionName, events);
    }

    private List<EventWithId<Event>> fetchEventsAfterCheckpoint(AsyncEventHandler eventHandler,
                                                                EventSubscriptionCheckpoint checkpoint) {
        return eventRepository.readEventsAfterCheckpoint(
                eventHandler.getAggregateType(),
                checkpoint.lastProcessedTransactionId(),
                checkpoint.lastProcessedEventId()
        );
    }

    private void updateSubscriptionCheckpoint(String subscriptionName, List<EventWithId<Event>> events) {
        EventWithId<Event> lastEvent = events.getLast();
        subscriptionRepository.updateEventSubscription(
                subscriptionName,
                lastEvent.transactionId(),
                lastEvent.id()
        );
        log.debug("Updated subscription: {} with last processed event ID: {}", subscriptionName, lastEvent.id());
    }
}
