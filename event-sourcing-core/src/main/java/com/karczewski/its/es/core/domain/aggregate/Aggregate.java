package com.karczewski.its.es.core.domain.aggregate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.core.domain.event.Event;
import com.karczewski.its.es.core.exception.HandlerInvocationException;
import com.karczewski.its.es.core.exception.UnsupportedHandlerException;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
@Slf4j
public abstract class Aggregate {

    protected final UUID aggregateId;
    @JsonIgnore
    protected final List<Event> changes = new ArrayList<>();

    protected int version;
    @JsonIgnore
    protected int baseVersion;

    protected Aggregate(@NonNull UUID aggregateId, int version) {
        this.aggregateId = aggregateId;
        this.baseVersion = this.version = version;
    }

    @Nonnull
    public abstract String getAggregateType();

    public void loadFromHistory(List<Event> events) {
        assertEmptyChanges();

        for (Event event : events) {
            validateEventVersion(event);
            applyEvent(event);
            baseVersion = version = event.getVersion();
        }
    }

    private void assertEmptyChanges() {
        if (!changes.isEmpty()) {
            throw new IllegalStateException("Aggregate has non-empty changes");
        }
    }

    private void validateEventVersion(Event event) {
        int expectedVersion = version + 1;
        if (event.getVersion() != expectedVersion) {
            throw new IllegalStateException(
                    "Event version %s doesn't match expected version %s".formatted(event.getVersion(), expectedVersion));
        }
    }

    protected int getNextVersion() {
        return version + 1;
    }

    protected void applyChange(Event event) {
        validateEventVersion(event);
        applyEvent(event);
        changes.add(event);
        version = event.getVersion();
    }

    protected void applyEvent(Event event) {
        log.debug("Applying event {}", event);
        invokeHandler(event, "apply");
    }

    public void processCommand(Command command) {
        log.debug("Processing command {}", command);
        invokeHandler(command, "process");
    }

    private void invokeHandler(Object message, String methodName) {
        try {
            Method method = this.getClass().getMethod(methodName, message.getClass());
            method.invoke(this, message);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new UnsupportedHandlerException(
                    this.getClass().getSimpleName(), methodName, message.getClass().getSimpleName());
        } catch (ReflectiveOperationException e) {
            throw new HandlerInvocationException(
                    this.getClass().getSimpleName(), methodName, message.getClass().getSimpleName(), e);
        }
    }
}
