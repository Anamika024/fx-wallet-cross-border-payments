package com.fxwallet.auth;

import com.fxwallet.user.AppUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private final SecretKey key;
  private final long jwtMinutes;

  public JwtService(@Value("${app.jwt-secret}") String secret, @Value("${app.jwt-minutes}") long jwtMinutes) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.jwtMinutes = jwtMinutes;
  }

  public String createAccessToken(AppUser user) {
    Instant now = Instant.now();
    return Jwts.builder()
        .subject(user.getEmail())
        .claim("role", user.getRole().name())
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(jwtMinutes * 60)))
        .signWith(key)
        .compact();
  }

  public String subject(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
  }
}
