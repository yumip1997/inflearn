package com.example.jwttutorial.auth.service.impl;

import com.example.jwttutorial.jwt.JwtConstants;
import com.example.jwttutorial.jwt.util.JwtTokenProvider;
import com.example.jwttutorial.login.dto.LoginDto;
import com.example.jwttutorial.auth.service.AuthorizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Override
    public String getJwtToken(LoginDto loginDto){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.createToken(authentication);
    }

    @Override
    public HttpHeaders getHeadersByJwt(String jwt){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtConstants.AUTHORIZATION_HEADER, "Bearer " +  jwt);
        return httpHeaders;
    }
}
