package com.example.lock_practice.com.utils;

import com.example.lock_practice.com.vo.RestTemplateVO;
import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;

public class ResTemplateUtils {

    private final static Gson gson = new Gson();
    private final RestTemplate restTemplate;

    public ResTemplateUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T,U> U callAPIByGet(RestTemplateVO restTemplateVO, Type returnType){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(restTemplateVO.getUrl(), String.class);
        return gson.fromJson(responseEntity.getBody(), returnType);
    }



}
