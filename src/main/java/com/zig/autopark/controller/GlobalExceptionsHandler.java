package com.zig.autopark.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionsHandler {
 @ExceptionHandler(value = { AccessDeniedException.class })
 public ResponseEntity<Object> handleAnyException(AccessDeniedException ex, WebRequest request) {
        return new ResponseEntity<>("My custom error message", new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }
}