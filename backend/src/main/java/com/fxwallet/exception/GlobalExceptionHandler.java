package com.fxwallet.exception;

import java.time.Instant;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ApiException.class)
  ResponseEntity<Map<String, Object>> api(ApiException ex) {
    return ResponseEntity.status(ex.getStatus()).body(Map.of("timestamp", Instant.now(), "message", ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<Map<String, Object>> validation(MethodArgumentNotValidException ex) {
    return ResponseEntity.badRequest().body(Map.of("timestamp", Instant.now(), "message", "Validation failed"));
  }
}
