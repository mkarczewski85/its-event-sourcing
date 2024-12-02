package com.karczewski.its.es.core.repository;

import com.karczewski.its.es.core.domain.aggregate.Aggregate;

import java.util.Optional;
import java.util.UUID;

public interface AggregateRepository {

    void createAggregateIfAbsent(String aggregateType, UUID aggregateId);

    boolean updateAggregateVersion(UUID aggregateId, int expectedVersion, int newVersion);

    void createAggregateSnapshot(Aggregate aggregate);

    Optional<Aggregate> findAggregateSnapshot(UUID aggregateId, Integer version);



}
