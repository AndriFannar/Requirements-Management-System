package dev.andrifannar.requirements.infrastructure.web;

import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import jakarta.validation.Valid;

import dev.andrifannar.requirements.application.dto.UserCreateRequest;
import dev.andrifannar.requirements.application.service.UserService;
import dev.andrifannar.requirements.application.mapper.UserMapper;
import dev.andrifannar.requirements.application.dto.UserResponse;
import dev.andrifannar.requirements.domain.model.User;

/**
 * REST controller for {@link User} management endpoints.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final UserMapper userMapper;

  public UserController(UserService userService, UserMapper userMapper) {
    this.userService = userService;
    this.userMapper = userMapper;
  }

  /**
   * Register a new {@link User}.
   * 
   * @param request the user creation request.
   * @return the created user.
   */
  @PostMapping
  public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
    User user = userService.createUser(
        request.username(),
        request.email(),
        request.password());

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userMapper.toResponse(user));
  }

  /**
   * Get a {@link User} by their public identifier.
   * 
   * @param publicId the user's public UUID.
   * @return the user, or 404 if not found.
   */
  @GetMapping("/{publicId}")
  public ResponseEntity<UserResponse> getUser(@PathVariable UUID publicId) {
    return userService.findByPublicId(publicId)
        .map(userMapper::toResponse)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
