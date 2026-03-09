package dev.andrifannar.requirements.infrastructure.security;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.FilterChain;

import dev.andrifannar.requirements.application.service.UserService;
import dev.andrifannar.requirements.domain.model.User;

/**
 * Filter that validates JWT tokens on incoming requests.
 * 
 * <p>
 * Extracts the Bearer token from the Authorization header, validates it,
 * and sets up the Spring Security authentication context if valid.
 * Runs once per request before the controller is invoked.
 * </p>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserService userService;

  public JwtAuthenticationFilter(JwtService jwtService, UserService userService) {
    this.jwtService = jwtService;
    this.userService = userService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = authHeader.substring(7);

    try {
      UUID publicId = jwtService.extractPublicId(token);

      if (SecurityContextHolder.getContext().getAuthentication() == null) {
        User user = userService.findByPublicId(publicId).orElse(null);

        if (user != null) {
          var securityUser = new SecurityUser(user);

          if (jwtService.isTokenValid(token, securityUser)) {
            var authToken = new UsernamePasswordAuthenticationToken(
                securityUser,
                null,
                securityUser.getAuthorities());
            authToken.setDetails(
                new WebAuthenticationDetailsSource()
                    .buildDetails(request));
            SecurityContextHolder.getContext()
                .setAuthentication(authToken);
          }
        }
      }
    } catch (Exception e) {
      // Token is invalid.
      // Request will continue unauthenticated and will be rejected by Spring
      // Security's authorization rules if endpoint requires authentication.
    }

    filterChain.doFilter(request, response);
  }
}
