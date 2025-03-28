package com.karczewski.its.es.core.exception;

public class HandlerInvocationException extends RuntimeException {
    public HandlerInvocationException(String aggregateType,
                                      String methodName,
                                      String messageType,
                                      Throwable cause) {
        super("Failed to invoke %s(%s) on aggregate '%s'".formatted(methodName, messageType, aggregateType), cause);
    }
}
