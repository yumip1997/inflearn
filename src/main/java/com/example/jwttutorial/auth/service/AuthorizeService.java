package com.example.jwttutorial.auth.service;

import com.example.jwttutorial.login.dto.LoginDto;
import org.springframework.http.HttpHeaders;

public interface AuthorizeService {

    String getJwtToken(LoginDto loginDto);

    HttpHeaders getHeadersByJwt(String jwt);
}
