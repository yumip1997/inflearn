package com.example.jwttutorial.jwt.filter;

import com.example.jwttutorial.jwt.JwtConstants;
import com.example.jwttutorial.jwt.util.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter extends GenericFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private JwtTokenProvider jwtTokenProvider;

    public JwtFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //토근의 인증정보를 SecurityContext에 저장함
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = getTokenFromRequest(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        try{
            jwtTokenProvider.validate(token);
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (Exception e){
            logger.debug("유효한 JWT 토근이 없습니다.{}, uri : {}", e.getMessage(), requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    //HttpServletRequest의 header에서 jwt token을 가져옴
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearToken = request.getHeader(JwtConstants.AUTHORIZATION_HEADER);
        return StringUtils.hasText(bearToken) && bearToken.startsWith("Bearer ") ? bearToken.substring(7) : "";
    }
}
