package com.example.jwttutorial.jwt;

import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter extends GenericFilter {

    private JwtTokenProvider jwtTokenProvider;

    public JwtFilter(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //토근의 인증정보를 SecurityContext에 저장함
   @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    private String resolveToken(HttpServletRequest request){
        String bearToken = request.getHeader(JwtConstants.AUTHORIZATION_HEADER);
        return StringUtils.hasText(bearToken) && bearToken.startsWith("Bearer ") ? bearToken.substring(7) : "";
    }
}
