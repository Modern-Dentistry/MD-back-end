package com.rustam.modern_dentistry.util;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtUtil {

    @Value("${spring.application.jwt.secret-key}")
    private String secretKey;
    @Value("${spring.application.jwt.expiration}")
    private long jwtExpiration;
    @Value("${spring.application.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String createToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String getUserIdAsUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String getUserIdAsUsernameFromTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            // Expired olan tokenin body hissəsinə daxil oluruq
            Claims claims = e.getClaims();
            return claims.getSubject(); // userId və ya username burada olacaq
        }
    }

    public boolean isValidUserIdFromToken(String token) {
        try {
            // Token-ı boşluqlardan təmizləyin
            token = token.trim();  // Token başında və sonunda olan boşluqları silir

            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            // User ID və ya username-i əldə edin
            String userId = claims.getSubject();

            // Burada userId yoxlayın (null və ya boş deyil)
            return userId != null && !userId.isEmpty();
        } catch (ExpiredJwtException e) {
            // Token vaxtı keçmişdir
            return false; // Burada false qaytarılır
        } catch (SignatureException | IllegalArgumentException e) {
            // Token səhvdir
            return false; // Burada false qaytarılır
        }
    }
}
