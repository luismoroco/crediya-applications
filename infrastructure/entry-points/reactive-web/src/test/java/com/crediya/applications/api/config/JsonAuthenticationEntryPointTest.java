package com.crediya.applications.api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.core.AuthenticationException;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class JsonAuthenticationEntryPointTest {

  private JsonAuthenticationEntryPoint entryPoint;

  @BeforeEach
  void setUp() {
    entryPoint = new JsonAuthenticationEntryPoint();
  }

  @Test
  void commence_setsUnauthorizedAndJsonBody() {
    MockServerWebExchange exchange = MockServerWebExchange.from(
      org.springframework.mock.http.server.reactive.MockServerHttpRequest.get("/test")
    );

    AuthenticationException authEx = Mockito.mock(AuthenticationException.class);

    StepVerifier.create(entryPoint.commence(exchange, authEx))
      .verifyComplete();

    MockServerHttpResponse response = exchange.getResponse();

    // Verifica status
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    // Verifica headers
    assertThat(response.getHeaders().getFirst("Content-Type")).isEqualTo("application/json");
  }
}
