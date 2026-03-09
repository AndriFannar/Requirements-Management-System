package dev.andrifannar.requirements.infrastructure.web;

import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;

import dev.andrifannar.requirements.infrastructure.security.SecurityUser;
import dev.andrifannar.requirements.infrastructure.security.JwtService;
import dev.andrifannar.requirements.application.dto.LoginRequest;

/**
 * REST controller for authentication endpoints.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  /**
   * Authenticates a user and returns a JWT token.
   * 
   * @param request the login credentials.
   * @return a JWT token if authentication succeeds.
   */
  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password()));

    SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
    String token = jwtService.generateToken(securityUser);

    return ResponseEntity.ok(Map.of("token", token));
  }
}
