package dev.andrifannar.requirements.domain.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;

/**
 * Represents a system user who can authenticate and interact with projects.
 * 
 * <p>
 * Global platform roles are managed via the {@link Role} association.
 * Project-scoped roles are managed separately through project membership.
 * </p>
 * 
 * @author Andri Fannar Kristjansson, Andri.Fannar@Live.com
 * @since 2026/03/04
 */
@Entity
@Table(name = "users")
public class User {
  /**
   * Internal database ID. Not exposed via the API, use rather {@code publicId}.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Public-facing identifier used in API responses and JWT tokens. */
  @Column(name = "public_id", nullable = false, updatable = false, unique = true)
  private UUID publicId;

  @Column(nullable = false, unique = true, length = 100)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(name = "first_name", length = 100)
  private String firstName;

  @Column(name = "last_name", length = 100)
  private String lastName;

  @Column(nullable = false)
  private boolean enabled = true;

  @Column(nullable = false)
  private boolean locked = false;

  @Column(name = "credentials_expired", nullable = false)
  private boolean credentialsExpired = false;

  @Column(name = "account_expired", nullable = false)
  private boolean accountExpired = false;

  @Column(name = "last_login_at")
  private Instant lastLoginAt;

  @Column(name = "failed_login_attempts", nullable = false)
  private int failedLoginAttempts = 0;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @Column(name = "created_by")
  private Long createdBy;

  @Column(name = "updated_by")
  private Long updatedBy;

  @Version
  private int version;

  /**
   * The user's system roles, which apply throughout the system, but not to
   * individual projects.
   */
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  /** Empty Hibernate constructor. */
  protected User() {
  }

  /**
   * Create a new User.
   * 
   * @param username the User's username.
   * @param email    the User's email.
   * @param password the User's encrypted password.
   */
  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.publicId = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    User user = (User) o;
    return publicId != null && publicId.equals(user.publicId);
  }

  @Override
  public int hashCode() {
    return publicId != null ? publicId.hashCode() : 0;
  }
}
