# JWT-Study
jwt를 이용한 인증, 인가 연습


# Login 과정
1. http request 
2. JwtFilter 동작
    - 실행 이유
        - SecurityConfig에 JwtSecurityConfig를 적용한다고 설정
        - JwtSecurityConfig의 configure 메소드에는 UsernamePasswordAuthenticationFilter 실행 전에 JwtFilter를 실행하는 로직이 설정되어 있음
    - 내용
        - request의 header를 가져와 header 안에 있는 jwt token을 꺼내 validate 실행
        - 꺼내온 token을 가지고 authentication을 가져와 SecurityContextHolder의 context에 해당 authentication을 셋팅함
3. LoginController 동작
    - authorize 실행
4. Authorize Service 실행
    - `getJwtToken`실행
        - LoginDto의 username, password를 가지고 UsernamePasswordAuthentication 객체 생성
        - UsernamePasswordAuthentication으로 인증 수행
            - AuthenticationManagerBuilder 가 AuthenticationManager의 구현체인 ProviderManager에게 인증책임을 넘김
            - ProviderManager는 인증 로직을 수행할 수 있는 AuthenticationProvider의 구현체를 찾아 인증 책임을 넘김
            - 인증 로직을 수행할 수 있는DaoAuthenticationProvider가
                - 이를 받아 authenticate 메소드를 실행하면서,
                    - UserDetailsService의 구현체로부터 loadUserByUsername 메소드를 실행
                    - UserDetailsService를 구현한 CustomUserDetailService의 loadUserByUsername이 실행되고, username, password, 권한정보가 담긴 UserDetails형을 반환
                - 반환받은 UserDetails형 객체로 Authentication를 셋팅하여 반환
        - 인증 수행 완료 후 반환받은 Authentication 형 객체를 SecurityContextHolder의 Context에 저장
        - 해당 Authentication 형 객체로 Jwt 토근을 생성하여 반환
    - `getHeadersByJwt` 실행
        - Jwt 토근을 HttpHeader에 설정하여 반환
