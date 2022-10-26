package com.example.lock_practice.com.vo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.MediaType;

@Getter
@Builder
public class RestTemplateVO{

    private String url;
    private MediaType mediaType;
    private Object body;

}
