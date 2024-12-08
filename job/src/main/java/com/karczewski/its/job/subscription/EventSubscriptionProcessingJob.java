package com.karczewski.its.job.subscription;

import com.karczewski.its.es.core.service.EventSubscriptionProcessor;
import com.karczewski.its.es.core.service.event.AsyncEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventSubscriptionProcessingJob {

    private final List<AsyncEventHandler> eventHandlers;
    private final EventSubscriptionProcessor eventSubscriptionProcessor;

    @Scheduled(
            fixedDelayString = "${events.subscriptions.polling-interval}",
            initialDelayString = "${events.subscriptions.polling-initial-delay}"
    )
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
