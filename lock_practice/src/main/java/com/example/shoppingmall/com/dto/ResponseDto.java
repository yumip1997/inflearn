package com.example.shoppingmall.com.dto;

import lombok.Getter;

@Getter
public class ResponseDto{

    private String code;
    private Object data;

    public ResponseDto(String code, Object data){
        this.code = code;
        this.data = data;
    }

    public static ResponseDto of(Object data){
        return new ResponseDto("200", data);
    }

    public static ResponseDto failResponse(String code, Object data){
        return new ResponseDto(code, data);
    }

}
