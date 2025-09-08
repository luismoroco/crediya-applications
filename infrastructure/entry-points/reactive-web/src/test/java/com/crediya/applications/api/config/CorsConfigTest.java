package com.crediya.applications.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.cors.reactive.CorsWebFilter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CorsConfigTest {

  @Test
  void shouldCreateCorsWebFilterWithAllowedOrigins() {
    String allowedOrigins = "http://localhost:3000,http://example.com";
    CorsConfig config = new CorsConfig();

    CorsWebFilter filter = config.corsWebFilter(allowedOrigins);

    assertNotNull(filter);
  }
}
