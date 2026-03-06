package dev.andrifannar.requirements.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import dev.andrifannar.requirements.domain.model.repository.RoleRepository;
import dev.andrifannar.requirements.domain.model.Role;

/**
 * Adapter that implements the domain's {@link RoleRepository} port
 * using Spring Data JPA.
 */
@Repository
public class RoleRepositoryAdapter implements RoleRepository {

  private final SpringDataRoleRepository jpa;

  public RoleRepositoryAdapter(SpringDataRoleRepository jpa) {
    this.jpa = jpa;
  }

  @Override
  public Optional<Role> findByName(String name) {
    return jpa.findByName(name);
  }
}
