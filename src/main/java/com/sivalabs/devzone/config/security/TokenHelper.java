package com.sivalabs.devzone.config.security;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.sivalabs.devzone.config.ApplicationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenHelper {
    private final ApplicationProperties properties;

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = this.getAllClaimsFromToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public String generateToken(String username) {
        String secretString = properties.getJwt().getSecret();
        SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(UTF_8));
        Date iat = new Date();
        return Jwts.builder()
                .issuer(properties.getJwt().getIssuer())
                .subject(username)
                .issuedAt(iat)
                .expiration(generateExpirationDate(iat))
                .signWith(key)
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        String secretString = properties.getJwt().getSecret();
        SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(UTF_8));
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    private Date generateExpirationDate(Date date) {
        return new Date(date.getTime() + properties.getJwt().getExpiresIn() * 1000);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username != null && username.equals(userDetails.getUsername());
    }

    public String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(properties.getJwt().getHeader());
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring("Bearer ".length());
        }
        return null;
    }
}
