package com.karczewski.its.query.exception;

public class QueryPermissionDeniedException extends RuntimeException {

    public QueryPermissionDeniedException(String message) {
        super(message);
    }
}
