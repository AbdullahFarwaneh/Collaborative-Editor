package com.example.collabeditor.Security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationMillis;

     private Key getSigningKey(){
         return Keys.hmacShaKeyFor(secretKey.getBytes());
     }

  public String  generateToken(Long userid){
         return Jwts.builder().setSubject(String.valueOf(userid)).
                 setIssuedAt(new Date(System.currentTimeMillis()))
                 .setExpiration(new Date(System.currentTimeMillis()+expirationMillis))
                 .signWith(getSigningKey()).compact();
  }
    public Long extractUserId(String token) {
        String subject = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return Long.parseLong(subject);
    }

    public boolean isTokenValid(String token) {

         try{
              Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                     .parseClaimsJws(token).getBody().getSubject();
             return true;
         }catch (JwtException|IllegalArgumentException e){
             return false ;
         }
    }

}

