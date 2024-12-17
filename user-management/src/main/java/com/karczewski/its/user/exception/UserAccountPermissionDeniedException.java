package com.karczewski.its.user.exception;

public class UserAccountPermissionDeniedException extends RuntimeException {

    public UserAccountPermissionDeniedException(String message) {
        super(message);
    }
}
