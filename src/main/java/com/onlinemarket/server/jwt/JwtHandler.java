package com.onlinemarket.server.jwt;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.gson.io.GsonSerializer;

@Component
public class JwtHandler {

    private static final String secretWord = "jwtWord";
    private static final String salt = "3e0f03da3a06863eb528feeca30c4bbc88244918a4a2eae12df2dbb22cf403ad";

    // 7 day in milliseconds
    private static final long EXPIRATION_TIME = 7 * 86400000;

    private static SecretKey secretKey;

    private static SecretKey generateKey() {
        SecretKey secretKey = new SecretKeySpec((secretWord + salt).getBytes(), "HmacSHA256");

        return secretKey;
    }

    private JwtHandler() {
        secretKey = generateKey();
    }

    public String generateToken(int userId) {

        return Jwts.builder()
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .serializeToJsonWith(new GsonSerializer<>(new Gson()))
                .compact();
    }

    public String checkToken(String jwt) {

        try {
            // check jwt valid or not
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            claims.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));

            return "jwt updated:" + Jwts.builder()
                    .setClaims(claims)
                    .signWith(secretKey)
                    .serializeToJsonWith(new GsonSerializer<>(new Gson()))
                    .compact();

        } catch (JwtException e) {
            System.out.println(e);
            return e.toString();
        }
    }

}