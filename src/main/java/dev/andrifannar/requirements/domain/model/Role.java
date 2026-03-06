package dev.andrifannar.requirements.domain.model;

import jakarta.persistence.*;

/**
 * Represents system roles a {@link User} can have.
 * 
 * @author Andri Fannar Kristjansson, Andri.Fannar@Live.com
 * @since 2026/03/04
 */
@Entity
@Table(name = "roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, unique = true, length = 50)
  private String name;

  /** Empty Hibernate constructor. */
  protected Role() {
  }

  /**
   * Create a new system role.
   * 
   * @param name the name of the role.
   */
  public Role(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Role role = (Role) o;
    return name != null && name.equals(role.name);
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }
}
