package com.example.shoppingmall.com.exception.handler;

import com.example.shoppingmall.com.dto.ResponseDto;
import com.example.shoppingmall.com.exception.BusinessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({BusinessException.class})
    public ResponseDto handleBusinessException(BusinessException exception){
        return ResponseDto.failResponse("400", exception.getMessage());
    }

}
