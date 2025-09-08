package com.crediya.applications.api.config;

import com.crediya.common.exc.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomServerAccessDeniedHandlerTest {

  private final CustomServerAccessDeniedHandler handler = new CustomServerAccessDeniedHandler();

  @Test
  void givenJwtPrincipal_whenHandle_thenThrowsUnauthorizedWithAuthorities() {
    JwtAuthenticationToken jwt = mock(JwtAuthenticationToken.class);
    when(jwt.getAuthorities()).thenReturn(List.of(() -> "ROLE_USER"));

    ServerWebExchange exchange = mock(ServerWebExchange.class);
    when(exchange.getPrincipal()).thenReturn(Mono.just(jwt));

    Mono<Void> result = handler.handle(exchange, new AccessDeniedException("denied"));

    StepVerifier.create(result)
      .expectErrorSatisfies(error -> {
        assert error instanceof UnauthorizedException;
      })
      .verify();
  }

  @Test
  void givenNoPrincipal_whenHandle_thenThrowsUnauthorized() {
    ServerWebExchange exchange = mock(ServerWebExchange.class);
    when(exchange.getPrincipal()).thenReturn(Mono.empty());

    Mono<Void> result = handler.handle(exchange, new AccessDeniedException("denied"));

    StepVerifier.create(result)
      .expectErrorSatisfies(error -> {
        assert error instanceof UnauthorizedException;
        assert error.getMessage().contains("Not allowed to access this resource");
      })
      .verify();
  }
}
