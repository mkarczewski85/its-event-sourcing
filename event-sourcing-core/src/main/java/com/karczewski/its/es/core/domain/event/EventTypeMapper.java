package com.karczewski.its.es.core.domain.event;

public interface EventTypeMapper {

    Class<? extends Event> getClassByEventType(String eventType);

}
