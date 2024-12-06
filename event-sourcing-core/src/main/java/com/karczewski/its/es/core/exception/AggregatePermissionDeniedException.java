package com.karczewski.its.es.core.exception;

public class AggregatePermissionDeniedException extends RuntimeException {

    public AggregatePermissionDeniedException(String message) {
        super(message);
    }
}
