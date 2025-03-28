package com.karczewski.its.es.core.exception;

public class UnsupportedHandlerException extends RuntimeException {
    public UnsupportedHandlerException(String aggregateType,
                                       String methodName,
                                       String messageType) {
        super("Aggregate '%s' doesn't support %s(%s)".formatted(aggregateType, methodName, messageType));
    }
}
