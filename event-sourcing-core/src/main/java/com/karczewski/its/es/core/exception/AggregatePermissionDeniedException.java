package com.karczewski.its.es.core.exception;

public class AggregatePermissionDeniedException extends AggregateStateException {

    public AggregatePermissionDeniedException(String message) {
        super(message);
    }
}
