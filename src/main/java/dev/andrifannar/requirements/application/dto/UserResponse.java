package dev.andrifannar.requirements.application.dto;

import java.time.Instant;
import java.util.UUID;
import java.util.Set;

/**
 * Response DTO for {@link User} data returned by the API.
 */
public record UserResponse(
    UUID publicId,
    String username,
    String email,
    String firstName,
    String lastName,
    Set<String> roles,
    Instant createdAt) {
}
