package dev.andrifannar.requirements.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.andrifannar.requirements.domain.model.Role;

/**
 * Spring Data JPA interface for {@link Role} persistence.
 * Not used directly by the application layer, but rather accessed via
 * {@link RoleRepositoryAdapter}.
 */
interface SpringDataRoleRepository extends JpaRepository<Role, Integer> {

  Optional<Role> findByName(String name);
}
