package com.crediya.applications.api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class SecurityHeadersConfigTest {

  private SecurityHeadersConfig filter;
  private ServerWebExchange exchange;
  private WebFilterChain chain;
  private ServerHttpResponse response;

  @BeforeEach
  void setUp() {
    filter = new SecurityHeadersConfig();
    exchange = mock(ServerWebExchange.class);
    response = mock(ServerHttpResponse.class);
    chain = mock(WebFilterChain.class);

    HttpHeaders headers = new HttpHeaders();
    when(exchange.getResponse()).thenReturn(response);
    when(response.getHeaders()).thenReturn(headers);
    when(chain.filter(exchange)).thenReturn(Mono.empty());
  }

  @Test
  void shouldAddSecurityHeaders() {
    filter.filter(exchange, chain).block();

    HttpHeaders headers = response.getHeaders();

    assertEquals("default-src 'self'; frame-ancestors 'self'; form-action 'self'",
      headers.getFirst("Content-Security-Policy"));
    assertEquals("max-age=31536000;", headers.getFirst("Strict-Transport-Security"));
    assertEquals("nosniff", headers.getFirst("X-Content-Type-Options"));
    assertEquals("", headers.getFirst("Server"));
    assertEquals("no-store", headers.getFirst("Cache-Control"));
    assertEquals("no-cache", headers.getFirst("Pragma"));
    assertEquals("strict-origin-when-cross-origin", headers.getFirst("Referrer-Policy"));

    verify(chain, times(1)).filter(exchange);
  }
}
