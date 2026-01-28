package org.example.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

  private final TokenStore tokenStore;

  @Override
  public boolean preHandle(
      HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {

    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      log.warn(
          "Unauthorized request to {} {} – missing or invalid Authorization header",
          request.getMethod(),
          request.getRequestURI());
      writeUnauthorizedResponse(response, "Access token is missing or invalid");
      return false;
    }

    String token = authHeader.substring(7);

    if (tokenStore.validateAccessToken(token).isEmpty()) {
      log.warn(
          "Unauthorized request to {} {} – invalid or expired access token",
          request.getMethod(),
          request.getRequestURI());
      writeUnauthorizedResponse(response, "Access token is invalid or expired");
      return false;
    }

    log.debug("Authorized request to {} {}", request.getMethod(), request.getRequestURI());

    return true;
  }

  private void writeUnauthorizedResponse(HttpServletResponse response, String message) {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("text/plain;charset=UTF-8");
    try {
      response.getWriter().write(message);
    } catch (IOException e) {
      log.error("Failed to write unauthorized response", e);
    }
  }
}
