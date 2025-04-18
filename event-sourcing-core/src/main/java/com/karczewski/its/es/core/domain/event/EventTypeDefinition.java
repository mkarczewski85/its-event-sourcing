package com.karczewski.its.es.core.domain.event;

public interface EventTypeDefinition {

    boolean supports(String type);

    Class<? extends Event> getClassByEventType(String eventType);

}
