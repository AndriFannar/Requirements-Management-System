package dev.andrifannar.requirements.domain.model.repository;

import java.util.Optional;
import java.util.UUID;

import dev.andrifannar.requirements.domain.model.User;

/**
 * Port for {@link User} persistence operations.
 * 
 * <p>
 * Defines what the domain and application layers need from user storage,
 * without specifying the implementation technology.
 * </p>
 */
public interface UserRepository {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  Optional<User> findByPublicId(UUID publicId);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  User save(User user);

  void delete(User user);
}
