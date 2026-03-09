package dev.andrifannar.requirements.application.dto;

/**
 * Request DTO for logging in a {@link User}.
 */
public record LoginRequest(
    String username,
    String password) {
}
