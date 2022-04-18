package com.example.jwttutorial.com.exception.handler.impl;

import com.example.jwttutorial.com.exception.handler.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MethodArgumentNotValidExceptionHandler implements ExceptionHandler {

    @Override
    public ResponseEntity<Object> handleException(Exception exception) {
        List<String> errorMsgList = getErrorMsgList((MethodArgumentNotValidException) exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsgList);
    }

    private List<String> getErrorMsgList(MethodArgumentNotValidException exception){
        List<ObjectError> allErrors = exception.getAllErrors();
        return allErrors.stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
    }

}
