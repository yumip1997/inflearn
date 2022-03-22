package com.example.jwttutorial.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

//JWT Token 발급 클래스
//JWT : Header, Payload, Signature로 구성되어 있고 JSON 객체 사용
//Header : typ(토큰의 타입), alg(해싱 알고리즘, 토큰을 검증할 때 사용하는 Signature 부분에서 사용)
//Payload : 토큰들에 담을 정보가 들어있으며 정보의 조각을 클레임(claim)이라 함
//Signature : Header, Payload의 값을 BASE64로 인코딩한 해쉬 값을 담고 있음 (서버에서 만들어내며, 이를 통해 조작된 JWT Token이 요청되었는 지를 확인)

@Component
public class JwtTokenProvider implements InitializingBean {

    private final String secret;
    private final long tokenValidityInMilliseconds;

    private Key key;

    //yml에 설정되어 있는 secret, tokenValidityInSeconds 으로 필드 값을 초기화
    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds){

        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }

    @Override
    public void afterPropertiesSet(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //JWT Token 생성
    public String createToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(JwtConstants.AUTHORITIES_KEY, getAuthorities(authentication, ","))
                .setExpiration(getExpireDate(new Date(), this.tokenValidityInMilliseconds)) //토근 만료 시간 설정
                .signWith(key, SignatureAlgorithm.ES512)
                .compact();
    }

    private String getAuthorities(Authentication authentication, String delimiter){
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(delimiter));
    }

    private Date getExpireDate(Date now, long time){
        return new Date(now.getTime() + time);
    }

    public Authentication getAuthentication(String token){
        Claims claims = getClaims(token);
        List<SimpleGrantedAuthority> authorities = getAuthoritiesClaims(claims);
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private List<SimpleGrantedAuthority> getAuthoritiesClaims(Claims claims){
        return Arrays.stream(claims.get(JwtConstants.AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
