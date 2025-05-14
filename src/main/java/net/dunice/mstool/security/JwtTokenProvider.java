package net.dunice.mstool.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getEmailFromToken(String token) {
        // Remove "Bearer " prefix if present
        String cleanToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(cleanToken)
                .getPayload();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            // Remove "Bearer " prefix if present
            String cleanToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(cleanToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
} 