package com.karczewski.its.job.subscription;

import com.karczewski.its.es.core.service.EventSubscriptionProcessor;
import com.karczewski.its.es.core.service.event.AsyncEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
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
            fixedDelayString = "${event-sourcing.subscriptions.polling-interval}",
            initialDelayString = "${event-sourcing.subscriptions.polling-initial-delay}"
    )
    @SchedulerLock(
            name = "EventSubscriptionTaskLock",
            lockAtMostFor = "${event-sourcing.subscriptions.polling-interval}",
            lockAtLeastFor = "${event-sourcing.subscriptions.polling-interval}"
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
