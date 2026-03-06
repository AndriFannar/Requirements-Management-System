package dev.andrifannar.requirements.infrastructure.security;

import java.util.stream.Collectors;
import java.util.Collection;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import dev.andrifannar.requirements.domain.model.User;

/**
 * Adapter that wraps a domain {@link User} to satisfy Spring Security's
 * {@link UserDetails} contract.
 * 
 * <p>
 * This class exists to keep Spring Security dependencies out of the domain
 * layer.
 * It delegates all calls to the underlying {@link User} entity.
 * </p>
 */
public class SecurityUser implements UserDetails {

  private final User user;

  /**
   * Create a new SecurityUser.
   * 
   * @param user the user containing the information for the SecurityUser.
   */
  public SecurityUser(User user) {
    this.user = user;
  }

  /** Returns the underlying domain entity. */
  public User getUser() {
    return user;
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toSet());
  }

  @Override
  public boolean isAccountNonExpired() {
    return !user.isAccountExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return !user.isLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return !user.isCredentialsExpired();
  }

  @Override
  public boolean isEnabled() {
    return user.isEnabled();
  }
}
