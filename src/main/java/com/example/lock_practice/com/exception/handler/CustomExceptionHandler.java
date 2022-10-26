package com.example.lock_practice.com.exception.handler;

import com.example.lock_practice.com.exception.BusinessException;
import com.example.lock_practice.com.exception.DataException;
import com.example.lock_practice.com.exception.message.com.CommonExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<String> handleBusinessException(RuntimeException exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }

    @ExceptionHandler({DataException.class})
    public ResponseEntity<String> handleDataException(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonExceptionMessage.SERVER_ERROR);
    }
}
