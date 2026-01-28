package org.example.authentication;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.AuthLoginResponse;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenStore {

  private final Map<String, String> accessTokens = new ConcurrentHashMap<>();
  private final Map<String, String> refreshTokens = new ConcurrentHashMap<>();

  public AuthLoginResponse login(String username) {
    String accessToken = UUID.randomUUID().toString();
    String refreshToken = UUID.randomUUID().toString();

    accessTokens.put(accessToken, username);
    refreshTokens.put(refreshToken, username);

    log.info("User {} logged in", username);
    return new AuthLoginResponse(accessToken, refreshToken, username);
  }

  public Optional<String> validateAccessToken(String token) {
    return Optional.ofNullable(accessTokens.get(token));
  }

  public Optional<String> refresh(String refreshToken) {
    String user = refreshTokens.get(refreshToken);
    if (user == null) {
      return Optional.empty();
    }

    String newAccessToken = UUID.randomUUID().toString();
    accessTokens.put(newAccessToken, user);
    return Optional.of(newAccessToken);
  }

  public void logout(String accessToken) {
    String user = accessTokens.get(accessToken);

    if (user == null) {
      return;
    }
    accessTokens.entrySet().removeIf(entry -> entry.getValue().equals(user));
    refreshTokens.entrySet().removeIf(entry -> entry.getValue().equals(user));
  }
}
