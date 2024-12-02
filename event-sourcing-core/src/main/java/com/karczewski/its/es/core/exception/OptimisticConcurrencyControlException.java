package com.karczewski.its.es.core.exception;

public class OptimisticConcurrencyControlException extends AggregateStateException {

    public OptimisticConcurrencyControlException(long expectedVersion) {
        super("Current version doesn't match expected version %s", expectedVersion);
    }
}
