package org.example.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(
      HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
    String page = request.getParameter("page");

    if (page != null) {
      log.info(
          "Incoming request: {} {} with page={}",
          request.getMethod(),
          request.getRequestURI(),
          page);
    } else {
      log.info("Incoming request: {} {}", request.getMethod(), request.getRequestURI());
    }

    return true;
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull Object handler,
      Exception ex) {

    Object startTimeAttr = request.getAttribute("startTime");

    Long duration = null;
    if (startTimeAttr instanceof Long startTime) {
      duration = System.currentTimeMillis() - startTime;
    }

    String page = request.getParameter("page");
    int status = response.getStatus();

    String pageInfo = page != null ? " page=" + page : "";
    String durationInfo = duration != null ? ", duration=" + duration + "ms" : "";

    if (status >= 200 && status < 300) {
      log.info(
          "{} {} completed successfully{} (status {}{})",
          request.getMethod(),
          request.getRequestURI(),
          pageInfo,
          status,
          durationInfo);
    } else if (status >= 400 && status < 500) {
      log.warn(
          "{} {} failed due to client error{} (status {}{})",
          request.getMethod(),
          request.getRequestURI(),
          pageInfo,
          status,
          durationInfo);
    } else {
      log.error(
          "{} {} failed due to server error{} (status {}{})",
          request.getMethod(),
          request.getRequestURI(),
          pageInfo,
          status,
          durationInfo);
    }
  }
}
