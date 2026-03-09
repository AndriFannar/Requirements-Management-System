package dev.andrifannar.requirements.domain.model.exception;

/**
 * Thrown when a requested resource cannot be found.
 */
public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message) {
    super(message);
  }
}
