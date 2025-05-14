package net.dunice.mstool.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import net.dunice.mstool.constants.ConstantFields;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long jwtExpirationInMs;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email) {
        return ConstantFields.BEARER +
                Jwts.builder()
                        .subject(email)
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                        .signWith(getSigningKey())
                        .compact();
    }

    public Boolean validateToken(String token) {
        try {
            // Remove "Bearer " prefix if present
            String cleanToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(cleanToken);
            return true;
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        try {
            // Remove "Bearer " prefix if present
            String cleanToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(cleanToken)
                    .getPayload();

            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw e;
        }
    }

    public String getUsernameFromToken(String jwtToken) {
        try {
            // Remove "Bearer " prefix if present
            String cleanToken = jwtToken.startsWith("Bearer ") ? jwtToken.substring(7) : jwtToken;
            
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(cleanToken)
                    .getPayload()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw e;
        }
    }

    public String extractRole(String token) {
        try {
            // Remove "Bearer " prefix if present
            String cleanToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(cleanToken)
                    .getPayload()
                    .get("role", String.class);
        } catch (ExpiredJwtException e) {
            throw e;
        }
    }
}
