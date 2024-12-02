package com.karczewski.its.es.core.domain.event;

import java.math.BigInteger;

public record EventSubscriptionCheckpoint(
        BigInteger lastProcessedTransactionId,
        long lastProcessedEventId
) {
}
