package com.example.jwttutorial.login.controller;

import com.example.jwttutorial.com.jwt.dto.JwtTokenDto;
import com.example.jwttutorial.login.dto.LoginDto;
import com.example.jwttutorial.com.jwt.util.service.AuthorizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class LoginController {

    private final AuthorizeService authorizeService;

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDto> authorize(@Valid @RequestBody LoginDto loginDto){
        String jwt = authorizeService.getJwtToken(loginDto);
        HttpHeaders httpHeaders = authorizeService.getHeadersByJwt(jwt);
        return new ResponseEntity<>(new JwtTokenDto(jwt),httpHeaders, HttpStatus.OK);
    }
}
