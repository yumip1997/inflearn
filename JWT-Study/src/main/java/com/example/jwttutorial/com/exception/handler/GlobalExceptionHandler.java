package com.example.jwttutorial.com.exception.handler;

import com.example.jwttutorial.com.exception.handler.impl.MethodArgumentNotValidExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MethodArgumentNotValidExceptionHandler exceptionHandler;

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(Exception exception){
        return exceptionHandler.handleException(exception);
    }
}
