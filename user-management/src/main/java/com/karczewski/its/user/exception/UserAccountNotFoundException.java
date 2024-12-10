package com.karczewski.its.user.exception;

public class UserAccountNotFoundException extends RuntimeException {

    public UserAccountNotFoundException() {
    }

    public UserAccountNotFoundException(String message) {
        super(message);
    }
}
