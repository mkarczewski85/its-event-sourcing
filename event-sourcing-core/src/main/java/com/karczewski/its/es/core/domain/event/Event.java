package com.karczewski.its.es.core.domain.event;

import jakarta.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public abstract class Event {

    protected final UUID aggregateId;
    protected final int version;
    protected final LocalDateTime createdDate = LocalDateTime.now();

    @Nonnull
    public abstract String getEventType();

    public boolean isTypeOf(String type) {
        return getEventType().equals(type);
    }
}
