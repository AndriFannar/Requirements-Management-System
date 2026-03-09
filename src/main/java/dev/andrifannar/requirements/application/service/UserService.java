package dev.andrifannar.requirements.application.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import dev.andrifannar.requirements.domain.model.exception.ResourceAlreadyExistsException;
import dev.andrifannar.requirements.domain.model.repository.UserRepository;
import dev.andrifannar.requirements.domain.model.repository.RoleRepository;
import dev.andrifannar.requirements.domain.model.User;
import dev.andrifannar.requirements.domain.model.Role;

/**
 * Application service for {@link User} management operations.
 * 
 * <p>
 * Orchestrates {@link User} creation, retrieval, and profile updates.
 * Delegates persistence to the {@link UserRepository} domain port.
 * </p>
 */
@Service
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Creates a new {@link User} with the provided credentials.
   * 
   * @param username the desired username.
   * @param email    the user's e-mail address.
   * @param password the plaintext password (will be hashed before storage).
   * @return the created user.
   * @throws ResourceAlreadyExistsException if username or e-mail is already
   *                                        taken.
   */
  @Transactional
  public User createUser(String username, String email, String password) {
    if (userRepository.existsByUsername(username)) {
      throw new ResourceAlreadyExistsException("Username already taken: " + username);
    }
    if (userRepository.existsByEmail(email)) {
      throw new ResourceAlreadyExistsException("E-Mail is already taken: " + email);
    }

    User user = new User(username, email, passwordEncoder.encode(password));

    Role defaultRole = roleRepository.findByName("ROLE_USER")
        .orElseThrow(() -> new IllegalStateException(
            "Default role ROLE_USER not configured in database"));

    user.addRole(defaultRole);

    return userRepository.save(user);
  }

  /**
   * Finds a {@link User} by their public-facing identifier.
   * 
   * @param publicId the user's public UUID.
   * @return an optional containing the user, or empty if not found.
   */
  public Optional<User> findByPublicId(UUID publicId) {
    return userRepository.findByPublicId(publicId);
  }

  /**
   * Finds a {@link User} by their username.
   * 
   * @param username the username to look up.
   * @return an optional containing the user, or empty if not found.
   */
  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }
}
