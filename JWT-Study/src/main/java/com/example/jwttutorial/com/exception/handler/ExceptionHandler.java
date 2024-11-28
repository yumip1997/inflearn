package com.example.jwttutorial.com.exception.handler;

import org.springframework.http.ResponseEntity;

public interface ExceptionHandler{

    ResponseEntity<Object> handleException(Exception exception);

}
