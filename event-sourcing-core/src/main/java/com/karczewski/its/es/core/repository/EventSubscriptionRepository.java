package com.karczewski.its.es.core.repository;

import com.karczewski.its.es.core.domain.event.EventSubscriptionCheckpoint;

import java.math.BigInteger;
import java.util.Optional;

public interface EventSubscriptionRepository {

    void createSubscriptionIfAbsent(String subscriptionName);

    Optional<EventSubscriptionCheckpoint> findCheckpointAndLockSubscription(String subscriptionName);

    boolean updateEventSubscription(String subscriptionName, BigInteger lastProcessedTransactionId, long lastProcessedEventId);

}
