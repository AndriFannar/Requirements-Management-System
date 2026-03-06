package dev.andrifannar.requirements.application.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import dev.andrifannar.requirements.application.dto.UserResponse;
import dev.andrifannar.requirements.domain.model.Role;
import dev.andrifannar.requirements.domain.model.User;

/**
 * Maps between {@link User} domain entities and API DTOs.
 */
@Component
public class UserMapper {

  public UserResponse toResponse(User user) {
    return new UserResponse(
        user.getPublicId(),
        user.getUsername(),
        user.getEmail(),
        user.getFirstName(),
        user.getLastName(),
        user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toSet()),
        user.getCreatedAt());
  }
}
