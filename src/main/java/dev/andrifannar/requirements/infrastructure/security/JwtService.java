package dev.andrifannar.requirements.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * Service for creating and validating JWT tokens.
 * 
 * <p>
 * Tokens are signed with HMAC-SHA256 and contain the user's public identifier
 * and roles as claims.
 * </p>
 */
@Service
public class JwtService {

  private final SecretKey signingKey;
  private final long expiration;

  public JwtService(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.expiration}") long expiration) {
    this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expiration = expiration;
  }

  /**
   * Generates a JWT token for the given user.
   * 
   * @param securityUser the authenticated user.
   * @return the signed JWT string.
   */
  public String generateToken(SecurityUser securityUser) {
    var now = new Date();
    var expiryDate = new Date(now.getTime() + expiration);

    String roles = securityUser.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    return Jwts.builder()
        .subject(securityUser.getUser().getPublicId().toString())
        .claim("roles", roles)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(signingKey)
        .compact();
  }

  /**
   * Extracts the user's public UUID from a token.
   * 
   * @param token the JWT string.
   * @return the public UUID stored as the token subject.
   */
  public UUID extractPublicId(String token) {
    return UUID.fromString(extractClaims(token).getSubject());
  }

  /**
   * Validates a token against the given security user.
   * 
   * @param token        the JWT string.
   * @param securityUser the user to validate against.
   * @return true if the token is valid and belongs to the user.
   */
  public boolean isTokenValid(String token, SecurityUser securityUser) {
    UUID publicId = extractPublicId(token);
    return publicId.equals(securityUser.getUser().getPublicId()) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractClaims(token).getExpiration().before(new Date());
  }

  private Claims extractClaims(String token) {
    return Jwts.parser()
        .verifyWith(signingKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
