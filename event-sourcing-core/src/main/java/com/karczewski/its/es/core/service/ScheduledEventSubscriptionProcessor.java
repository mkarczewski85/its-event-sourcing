package com.karczewski.its.es.core.service;

import com.karczewski.its.es.core.service.event.AsyncEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(name = "event-sourcing.subscriptions", havingValue = "polling")
@RequiredArgsConstructor
@Slf4j
public class ScheduledEventSubscriptionProcessor {

    private final List<AsyncEventHandler> eventHandlers;
    private final EventSubscriptionProcessor eventSubscriptionProcessor;

    public void processNewEvents() {
        eventHandlers.forEach(this::processNewEvents);
    }

    private void processNewEvents(AsyncEventHandler eventHandler) {
        try {
            eventSubscriptionProcessor.processNewEvents(eventHandler);
        } catch (Exception e) {
            log.warn("Failed to handle new events for subscription {}", eventHandler.getSubscriptionName(), e);
        }
    }
}
