package dev.andrifannar.requirements.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class HealthController {

  @GetMapping("/api/health")
  public Map<String, Object> health() {
    return Map.of(
        "Status", "UP",
        "Timestamp", LocalDateTime.now(),
        "Version", "0.0.1",
        "Spring Boot Version", "4.0.0",
        "Java Version", "25");
  }
}
// Hmm