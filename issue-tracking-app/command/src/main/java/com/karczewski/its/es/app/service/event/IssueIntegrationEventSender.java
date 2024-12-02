package com.karczewski.its.es.app.service.event;

import com.karczewski.its.es.app.domain.aggregate.AggregateType;
import com.karczewski.its.es.core.domain.event.Event;
import com.karczewski.its.es.core.domain.event.EventWithId;
import com.karczewski.its.es.core.service.event.AsyncEventHandler;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class IssueIntegrationEventSender implements AsyncEventHandler {
    @Override
    public void handleEvent(EventWithId<Event> event) {
        log.debug("Sending integration event {}", event);
        // TODO
    }

    @Nonnull
    @Override
    public String getAggregateType() {
        return AggregateType.ISSUE.toString();
    }
}
