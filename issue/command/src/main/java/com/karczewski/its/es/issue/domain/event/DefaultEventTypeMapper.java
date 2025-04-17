package com.karczewski.its.es.issue.domain.event;

import com.karczewski.its.es.core.domain.event.Event;
import com.karczewski.its.es.core.domain.event.EventTypeMapper;
import org.springframework.stereotype.Component;

@Component
public class DefaultEventTypeMapper implements EventTypeMapper {

    @Override
    public Class<? extends Event> getClassByEventType(String eventType) {
        return EventType.valueOf(eventType).getEventClass();
    }
}
