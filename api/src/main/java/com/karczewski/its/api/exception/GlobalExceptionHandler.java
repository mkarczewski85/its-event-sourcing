package com.karczewski.its.api.exception;

import com.karczewski.its.es.core.exception.AggregatePermissionDeniedException;
import com.karczewski.its.es.core.exception.AggregateStateException;
import com.karczewski.its.es.core.exception.OptimisticConcurrencyControlException;
import com.karczewski.its.query.exception.IssueNotFoundException;
import com.karczewski.its.query.exception.QueryPermissionDeniedException;
import com.karczewski.its.user.exception.InvalidPasswordResetTokenException;
import com.karczewski.its.user.exception.UserAccountAlreadyExistsException;
import com.karczewski.its.user.exception.UserAccountNotFoundException;
import com.karczewski.its.user.exception.UserAccountPermissionDeniedException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { UserAccountNotFoundException.class, IssueNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(final RuntimeException ex, final WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { UserAccountAlreadyExistsException.class, InvalidPasswordResetTokenException.class})
    protected ResponseEntity<Object> handleConflict(final RuntimeException ex, final WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = { AggregateStateException.class, OptimisticConcurrencyControlException.class })
    protected ResponseEntity<Object> handleIllegalOperation(final RuntimeException ex, final WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = { AggregatePermissionDeniedException.class, UserAccountPermissionDeniedException.class, QueryPermissionDeniedException.class })
    protected ResponseEntity<Object> handlePermissionDenied(final RuntimeException ex, final WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value = { ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(final RuntimeException ex, final WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
