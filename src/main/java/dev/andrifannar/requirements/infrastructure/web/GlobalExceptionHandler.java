package dev.andrifannar.requirements.infrastructure.web;

import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import java.time.Instant;
import java.util.Map;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import dev.andrifannar.requirements.domain.model.exception.ResourceAlreadyExistsException;
import dev.andrifannar.requirements.domain.model.exception.ResourceNotFoundException;

/**
 * Global exception handler that translates domain and validation exceptions
 * into consistent JSON error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<Map<String, Object>> handleAlreadyExists(
      ResourceAlreadyExistsException ex) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(errorBody(HttpStatus.CONFLICT, ex.getMessage()));
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleNotFound(
      ResourceNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(errorBody(HttpStatus.NOT_FOUND, ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(
      MethodArgumentNotValidException ex) {
    var fieldErrors = ex.getBindingResult().getFieldErrors().stream().collect(
        Collectors.toMap(
            error -> error.getField(),
            error -> error.getDefaultMessage(),
            (first, second) -> first));

    var body = errorBody(HttpStatus.BAD_REQUEST, "Validation failed");
    body.put("fieldErrors", fieldErrors);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(body);
  }

  private Map<String, Object> errorBody(HttpStatus status, String message) {
    var body = new LinkedHashMap<String, Object>();
    body.put("timestamp", Instant.now().toString());
    body.put("status", status.value());
    body.put("error", status.getReasonPhrase());
    body.put("message", message);
    return body;
  }
}