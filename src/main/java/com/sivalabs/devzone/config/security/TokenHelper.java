package com.sivalabs.devzone.config.security;

import com.sivalabs.devzone.common.exceptions.DevZoneException;
import com.sivalabs.devzone.config.ApplicationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
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
            final Claims claims = this.getAllClaimsFromToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public String generateToken(String username) {
        String secretString = properties.getJwt().getSecret();
        SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .issuer(properties.getJwt().getIssuer())
                .subject(username)
                .issuedAt(new Date())
                .expiration(generateExpirationDate())
                .signWith(key)
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            String secretString = properties.getJwt().getSecret();
            SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));

            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new DevZoneException(e);
        }
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + properties.getJwt().getExpiresIn() * 1000);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
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
