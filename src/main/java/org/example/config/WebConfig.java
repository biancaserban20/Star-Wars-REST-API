package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.authentication.AuthInterceptor;
import org.example.config.interceptor.LoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final LoggingInterceptor loggingInterceptor;
  private final AuthInterceptor authInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(loggingInterceptor).addPathPatterns("/**");
    registry.addInterceptor(authInterceptor).addPathPatterns("/favourites/**");
  }
}
