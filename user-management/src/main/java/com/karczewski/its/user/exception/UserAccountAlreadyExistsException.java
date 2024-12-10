package com.karczewski.its.user.exception;

public class UserAccountAlreadyExistsException extends RuntimeException {

    public UserAccountAlreadyExistsException(String message) {
        super(message);
    }
}
