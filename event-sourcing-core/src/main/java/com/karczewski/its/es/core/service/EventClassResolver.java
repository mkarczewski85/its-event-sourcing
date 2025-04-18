package com.karczewski.its.es.core.service;

import com.karczewski.its.es.core.domain.event.Event;
import com.karczewski.its.es.core.domain.event.EventTypeDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class EventClassResolver {

    private final Set<EventTypeDefinition> eventDefinitions;

    public Class<? extends Event> getClassByEventType(String eventType) {
        return eventDefinitions.stream()
                .filter(eventTypeDefinition -> eventTypeDefinition.supports(eventType))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unknown event type: " + eventType))
                .getClassByEventType(eventType);
    }
}
