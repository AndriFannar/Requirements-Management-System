package dev.andrifannar.requirements.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import dev.andrifannar.requirements.domain.model.repository.UserRepository;
import dev.andrifannar.requirements.domain.model.User;

/**
 * Adapter that implements the domain's {@link UserRepository} port
 * using Spring Data JPA.
 */
@Repository
public class UserRepositoryAdapter implements UserRepository {

  private final SpringDataUserRepository jpa;

  public UserRepositoryAdapter(SpringDataUserRepository jpa) {
    this.jpa = jpa;
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return jpa.findByUsername(username);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return jpa.findByEmail(email);
  }

  @Override
  public Optional<User> findByPublicId(UUID publicId) {
    return jpa.findByPublicId(publicId);
  }

  @Override
  public boolean existsByUsername(String username) {
    return jpa.existsByUsername(username);
  }

  @Override
  public boolean existsByEmail(String email) {
    return jpa.existsByEmail(email);
  }

  @Override
  public User save(User user) {
    return jpa.save(user);
  }

  @Override
  public void delete(User user) {
    jpa.delete(user);
  }
}
