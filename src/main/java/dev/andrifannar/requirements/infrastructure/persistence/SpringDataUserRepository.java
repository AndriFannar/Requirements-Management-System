package dev.andrifannar.requirements.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.andrifannar.requirements.domain.model.User;

/**
 * Spring Data JPA interface for {@link User} persistence.
 * Not used directly by the application layer, but rather accessed via
 * {@link UserRepositoryAdapter}.
 */
interface SpringDataUserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  Optional<User> findByPublicId(UUID publicId);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}
