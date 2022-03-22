package com.example.jwttutorial.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.stereotype.Component;

import java.security.Key;

public class JwtTokenValidator {

    private String token;
    private Key key;

    JwtTokenValidator(String token, Key key){
        this.token = token;
        this.key = key;
    }

    //TODO 수정 필요
    public void validate() throws Exception {

        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            throw new Exception("잘못된 JWT 서명입니다.");
        }catch(ExpiredJwtException e){
            throw new Exception("만료된 JWT 서명입니다.");
        }catch (UnsupportedJwtException e){
            throw new Exception("지원되지 않는 JWT 토근입니다.");
        }catch(IllegalArgumentException e){
            throw new Exception("JWT 토근이 잘못되었습니다.");
        }

    }

}
