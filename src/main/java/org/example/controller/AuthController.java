package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authentication.TokenStore;
import org.example.dto.AuthLoginResponse;
import org.example.dto.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final TokenStore tokenStore;

  @PostMapping("/login")
  public ResponseEntity<AuthLoginResponse> login(@Valid @RequestBody LoginRequest request) {
    log.info("Login attempt for user={}", request.user());
    AuthLoginResponse response = tokenStore.login(request.user());
    return ResponseEntity.ok(response);
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refresh(@RequestHeader("X-Refresh-Token") String refreshToken) {
    if (refreshToken == null || refreshToken.isBlank()) {
      log.warn("Refresh token missing from header");
      return ResponseEntity.badRequest().body("Refresh token must be provided");
    }

    return tokenStore
        .refresh(refreshToken)
        .map(ResponseEntity::ok)
        .orElseGet(
            () -> {
              log.warn("Invalid refresh token");
              return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                  .body("Refresh token is invalid or expired");
            });
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(
      @RequestHeader(value = "Authorization", required = false) String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      log.warn("Logout attempted without access token");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("Access token is missing or invalid");
    }

    String accessToken = authHeader.substring(7);
    tokenStore.logout(accessToken);

    log.info("User logged out successfully");
    return ResponseEntity.noContent().build();
  }
}
