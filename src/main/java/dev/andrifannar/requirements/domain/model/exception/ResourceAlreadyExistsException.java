package dev.andrifannar.requirements.domain.model.exception;

/**
 * Thrown when attempting to create a resource that conflicts
 * with an existing one (e.g., duplicate username or email).
 */
public class ResourceAlreadyExistsException extends RuntimeException {

  public ResourceAlreadyExistsException(String message) {
    super(message);
  }
}
