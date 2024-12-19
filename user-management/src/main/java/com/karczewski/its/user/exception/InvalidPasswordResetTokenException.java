package com.karczewski.its.user.exception;

public class InvalidPasswordResetTokenException extends RuntimeException {

    public InvalidPasswordResetTokenException(String message) {
        super(message);
    }
}
