package com.crediya.applications.api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

public class WebContextFilterTest {

  private WebContextFilter filter;
  private ServerWebExchange exchange;
  private WebFilterChain chain;

  @BeforeEach
  void setUp() {
    filter = new WebContextFilter();
    exchange = mock(ServerWebExchange.class);
    chain = mock(WebFilterChain.class);
  }

  @Test
  void filter_whenIdentityCardNumberExists_shouldContinue() {
    Jwt jwt = mock(Jwt.class);
    when(jwt.getClaimAsString(WebContextFilter.IDENTITY_CARD_NUMBER)).thenReturn("99999999");

    JwtAuthenticationToken token = new JwtAuthenticationToken(jwt);
    when(exchange.getPrincipal()).thenReturn(Mono.just(token));
    when(chain.filter(exchange)).thenReturn(Mono.empty());

    Mono<Void> result = filter.filter(exchange, chain);

    StepVerifier.create(result)
      .verifyComplete();
  }

  @Test
  void filter_whenPrincipalIsEmpty_shouldContinue() {
    when(exchange.getPrincipal()).thenReturn(Mono.empty());
    when(chain.filter(exchange)).thenReturn(Mono.empty());

    Mono<Void> result = filter.filter(exchange, chain);

    StepVerifier.create(result)
      .verifyComplete();

  }
}
