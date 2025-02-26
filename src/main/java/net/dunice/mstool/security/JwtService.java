package net.dunice.mstool.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import net.dunice.mstool.constants.ConstantFields;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

import static java.security.KeyRep.Type.SECRET;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long jwtExpirationInMs;

    public String generateToken(String subject) {
        return ConstantFields.BEARER +
                Jwts.builder()
                        .subject(subject)
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                        .signWith(SignatureAlgorithm.HS512, secretKey)
                        .compact();
    }

    public Boolean validateToken(String token) {

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));

        return token != null && Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .after(new Date());
    }
    public UUID extractUserId(String token) {
        // Парсим токен и извлекаем claims
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Предполагаем, что идентификатор пользователя хранится в поле subject
        String userIdStr = claims.getSubject();

        // Преобразуем строку в UUID и возвращаем
        return UUID.fromString(userIdStr);
    }
    public String getUsernameFromToken(String jwtToken) {

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));

        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody()
                .getSubject();
    }

    public String extractRole(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody().get("role", String.class);
    }
}
