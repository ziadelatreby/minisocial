package com.minisocial.minisocialapi.common;

import com.minisocial.minisocialapi.enums.USER_ROLE;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;


public class JwtUtil {
    private static final String secret = "this-is-a-very-secret-key-123456"; // TODO: move to env file
    private static final Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    public static String generateToken(Long userId) {
        return Jwts.builder().setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public static Claims decodeToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }

}
