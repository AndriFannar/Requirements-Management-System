package dev.andrifannar.requirements.domain.model.repository;

import java.util.Optional;

import dev.andrifannar.requirements.domain.model.Role;

/**
 * Port for {@link Role} persistence operations.
 * 
 * <p>
 * Defines what the domain and application layers need from role storage,
 * without specifying the implementation technology.
 * </p>
 */
public interface RoleRepository {

  Optional<Role> findByName(String name);
}
