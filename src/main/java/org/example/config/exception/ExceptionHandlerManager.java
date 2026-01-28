package org.example.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerManager {

  @ExceptionHandler(HttpClientErrorException.NotFound.class)
  public ResponseEntity<String> handleNotFound() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("The requested resource does not exist");
  }

  @ExceptionHandler(HttpServerErrorException.class)
  public ResponseEntity<String> handleExternalApiError() {
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
        .body("Star Wars API is currently unavailable. Please try again later.");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleValidation(MethodArgumentNotValidException ex) {
    String message =
        ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .orElse("Invalid request");

    log.warn("Login request validation failed: {}", message);
    return ResponseEntity.badRequest().body(message);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGeneric(Exception ex) {
    log.error("Unexpected error", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("An unexpected error occurred");
  }
}
