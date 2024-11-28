package com.example.jwttutorial.com.security.config;

import com.example.jwttutorial.com.jwt.util.JwtAccessDeniedHandler;
import com.example.jwttutorial.com.jwt.util.JwtAuthenticationEntryPoint;
import com.example.jwttutorial.com.jwt.config.JwtSecurityConfig;
import com.example.jwttutorial.com.jwt.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  //@PreAuthorize 어노테이션을 매소드 단위로 추가하기 위해 적용
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers(
                        "/h2-console/**"
                        , "/favicon.ico"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()//HttpServletRequest를 사용하는 요청들에 대한 접근제한
                .antMatchers("/api/hello").permitAll()  // /api/hello request는 인증 없이 허용
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/signup").permitAll()
                .anyRequest().authenticated() //나머지는 인증

                .and()
                .apply(new JwtSecurityConfig(jwtTokenProvider));

    }
}
