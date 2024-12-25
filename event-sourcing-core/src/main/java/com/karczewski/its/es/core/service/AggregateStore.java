package com.karczewski.its.es.core.service;

import com.karczewski.its.es.core.config.EventSourcingProperties;
import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.event.Event;
import com.karczewski.its.es.core.domain.event.EventWithId;
import com.karczewski.its.es.core.exception.OptimisticConcurrencyControlException;
import com.karczewski.its.es.core.repository.AggregateRepository;
import com.karczewski.its.es.core.repository.EventRepository;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Component
@RequiredArgsConstructor
@Slf4j
public class AggregateStore {

    private final AggregateRepository aggregateRepository;
    private final EventRepository eventRepository;
    private final AggregateFactory aggregateFactory;
    private final EventSourcingProperties properties;

    public Collection<EventWithId<Event>> saveAggregate(Aggregate aggregate) {
        log.debug("Saving aggregate {}", aggregate);
        initializeAggregateIfAbsent(aggregate);
        handleVersionUpdate(aggregate);
        return appendAggregateEvents(aggregate);
    }

    private void initializeAggregateIfAbsent(Aggregate aggregate) {
        aggregateRepository.createAggregateIfAbsent(aggregate.getAggregateType(), aggregate.getAggregateId());
    }

    private void handleVersionUpdate(Aggregate aggregate) {
        if (!aggregateRepository.updateAggregateVersion(aggregate.getAggregateId(), aggregate.getBaseVersion(), aggregate.getVersion())) {
            log.warn("Optimistic concurrency control error in aggregate {} {}: actual version doesn't match expected version {}",
                    aggregate.getAggregateType(), aggregate.getAggregateId(), aggregate.getBaseVersion());
            throw new OptimisticConcurrencyControlException(aggregate.getBaseVersion());
        }
    }

    private List<EventWithId<Event>> appendAggregateEvents(Aggregate aggregate) {
        List<Event> changes = aggregate.getChanges();
        List<EventWithId<Event>> newEvents = new ArrayList<>();
        EventSourcingProperties.SnapshottingProperties snapshotting = properties.getSnapshottingProperties(aggregate.getAggregateType());
        for (Event event : changes) {
            log.info("Appending {} event: {}", aggregate.getAggregateType(), event);
            EventWithId<Event> newEvent = eventRepository.appendEvent(event);
            newEvents.add(newEvent);
            maybeCreateSnapshot(snapshotting, aggregate);
        }
        return newEvents;
    }

    private void maybeCreateSnapshot(EventSourcingProperties.SnapshottingProperties snapshotting, Aggregate aggregate) {
        if (shouldCreateSnapshot(snapshotting, aggregate)) {
            log.info("Creating {} aggregate {} version {} snapshot",
                    aggregate.getAggregateType(), aggregate.getAggregateId(), aggregate.getVersion());
            aggregateRepository.createAggregateSnapshot(aggregate);
        }
    }

    private boolean shouldCreateSnapshot(EventSourcingProperties.SnapshottingProperties snapshotting, Aggregate aggregate) {
        return snapshotting.enabled() && snapshotting.nthEvent() > 1 && aggregate.getVersion() % snapshotting.nthEvent() == 0;
    }

    public Aggregate readAggregate(String aggregateType, UUID aggregateId) {
        return readAggregate(aggregateType, aggregateId, null);
    }

    public Aggregate readAggregate(@NonNull String aggregateType, @NonNull UUID aggregateId, @Nullable Integer version) {
        log.debug("Reading {} aggregate {}", aggregateType, aggregateId);

        EventSourcingProperties.SnapshottingProperties snapshotting = properties.getSnapshottingProperties(aggregateType);
        Aggregate aggregate = snapshotting.enabled()
                ? readFromSnapshotOrEvents(aggregateType, aggregateId, version)
                : readAggregateFromEvents(aggregateType, aggregateId, version);

        log.debug("Read aggregate {}", aggregate);
        return aggregate;
    }

    private Aggregate readFromSnapshotOrEvents(String aggregateType, UUID aggregateId, @Nullable Integer version) {
        return readAggregateFromSnapshot(aggregateId, version)
                .orElseGet(() -> {
                    log.debug("Aggregate {} snapshot not found", aggregateId);
                    return readAggregateFromEvents(aggregateType, aggregateId, version);
                });
    }

    private Optional<Aggregate> readAggregateFromSnapshot(UUID aggregateId, @Nullable Integer aggregateVersion) {
        return aggregateRepository.findAggregateSnapshot(aggregateId, aggregateVersion)
                .map(snapshot -> {
                    loadEventsAfterSnapshot(snapshot, aggregateVersion);
                    return snapshot;
                });
    }

    private void loadEventsAfterSnapshot(Aggregate aggregate, @Nullable Integer aggregateVersion) {
        int snapshotVersion = aggregate.getVersion();
        if (aggregateVersion == null || snapshotVersion < aggregateVersion) {
            List<Event> events = eventRepository.readEvents(aggregate.getAggregateId(), snapshotVersion, aggregateVersion)
                    .stream()
                    .map(EventWithId::event)
                    .toList();
            log.debug("Read {} events after version {} for aggregate {}", events.size(), snapshotVersion, aggregate.getAggregateId());
            aggregate.loadFromHistory(events);
        }
    }

    private Aggregate readAggregateFromEvents(String aggregateType, UUID aggregateId, @Nullable Integer aggregateVersion) {
        List<Event> events = eventRepository.readEvents(aggregateId, null, aggregateVersion)
                .stream()
                .map(EventWithId::event)
                .toList();
        log.debug("Read {} events for aggregate {}", events.size(), aggregateId);

        Aggregate aggregate = aggregateFactory.newInstance(aggregateType, aggregateId);
        aggregate.loadFromHistory(events);
        return aggregate;
    }
}
