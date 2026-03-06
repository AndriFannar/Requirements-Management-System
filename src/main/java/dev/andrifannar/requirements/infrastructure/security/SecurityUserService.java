package dev.andrifannar.requirements.infrastructure.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import dev.andrifannar.requirements.domain.model.repository.UserRepository;

/**
 * Loads user-specific data for Spring Security authentication.
 * 
 * <p>
 * Bridges the domain's {@link UserRepository} with Spring Security's
 * {@link UserDetailsService} contract by wrapping domain users in
 * {@link SecurityUser} adapters.
 * </p>
 */
@Service
public class SecurityUserService implements UserDetailsService {

  private final UserRepository userRepository;

  public SecurityUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .map(SecurityUser::new)
        .orElseThrow(() -> new UsernameNotFoundException(
            "User not found: " + username));
  }

}
