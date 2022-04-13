package com.example.jwttutorial.com.jwt.filter;

import com.example.jwttutorial.com.jwt.JwtConstants;
import com.example.jwttutorial.com.jwt.util.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter extends GenericFilter {

    private JwtTokenProvider jwtTokenProvider;

    public JwtFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        setAuthenticationByToken((HttpServletRequest) servletRequest);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void setAuthenticationByToken(HttpServletRequest httpServletRequest){
        String token = getTokenFromRequest(httpServletRequest);
        if(!StringUtils.hasText(token)) return;
        jwtTokenProvider.validate(token);
        //TODO 토근을 재생성하는 건가??
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearToken = request.getHeader(JwtConstants.AUTHORIZATION_HEADER);
        return StringUtils.hasText(bearToken) && bearToken.startsWith("Bearer ") ? bearToken.substring(7) : "";
    }
}
