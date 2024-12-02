package com.karczewski.its.es.core.domain.event;

import java.math.BigInteger;

public record EventWithId<T extends Event>(
        long id,
        BigInteger transactionId,
        T event
) {
}
