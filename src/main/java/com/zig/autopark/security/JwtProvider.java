package com.zig.autopark.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    @Value("${app.jwtSecret}")
    private String jwtSecret;
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        Key hmacKey = new SecretKeySpec(jwtSecret.getBytes(), "HMACSHA256");

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .expiration(expiryDate)
                .signWith(hmacKey)
                .compact();
    }
    public String getUserUsernameFromJWT(String token) {
        Key hmacKey = new SecretKeySpec(jwtSecret.getBytes(), "HMACSHA256");

        Claims claims = Jwts.parser().setSigningKey(hmacKey)
                .build()
                .parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }
    public boolean validateToken(String authToken) {
        Key hmacKey = new SecretKeySpec(jwtSecret.getBytes(), "HMACSHA256");
        try {
            Jwts.parser().setSigningKey(hmacKey).build().parseSignedClaims(authToken);
            return true;
        } catch (IllegalArgumentException ex) {
        }
        return false;
    }
}