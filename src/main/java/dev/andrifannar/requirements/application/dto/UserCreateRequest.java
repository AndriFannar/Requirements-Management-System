package dev.andrifannar.requirements.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a new {@link User}.
 */
public record UserCreateRequest(

    @NotBlank(message = "Username is required.") @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters.") String username,

    @NotBlank(message = "E-Mail is required.") @Email(message = "E-Mail must be valid.") String email,

    @NotBlank(message = "Password is required.") @Size(min = 8, message = "Password must be at least 8 characters.") String password) {
}
