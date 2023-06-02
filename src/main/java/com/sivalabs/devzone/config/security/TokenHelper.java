package com.sivalabs.devzone.config.security;

import com.sivalabs.devzone.common.exceptions.DevZoneException;
import com.sivalabs.devzone.config.ApplicationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private static final String AUDIENCE_WEB = "web";

    private final ApplicationProperties applicationProperties;

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
        String secretString = applicationProperties.getJwt().getSecret();
        SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setIssuer(applicationProperties.getJwt().getIssuer())
                .setSubject(username)
                .setAudience(AUDIENCE_WEB)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith(key, SIGNATURE_ALGORITHM)
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            String secretString = applicationProperties.getJwt().getSecret();
            SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));

            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new DevZoneException(e);
        }
    }

    private Date generateExpirationDate() {
        return new Date(
                System.currentTimeMillis() + applicationProperties.getJwt().getExpiresIn() * 1000);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username != null && username.equals(userDetails.getUsername());
    }

    public String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(applicationProperties.getJwt().getHeader());
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
