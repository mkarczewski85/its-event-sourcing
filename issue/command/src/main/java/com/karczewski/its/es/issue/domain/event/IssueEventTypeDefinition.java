package com.karczewski.its.es.issue.domain.event;

import com.karczewski.its.es.core.domain.event.Event;
import com.karczewski.its.es.core.domain.event.EventTypeDefinition;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class IssueEventTypeDefinition implements EventTypeDefinition {
    @Override
    public boolean supports(String type) {
        return Arrays.stream(EventType.values())
                .anyMatch(e -> e.name().equals(type));
    }

    @Override
    public Class<? extends Event> getClassByEventType(String eventType) {
        return EventType.valueOf(eventType).getEventClass();
    }
}
