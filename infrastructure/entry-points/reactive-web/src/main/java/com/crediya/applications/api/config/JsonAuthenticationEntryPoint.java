package com.crediya.applications.api.config;

import com.crediya.common.api.ApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class JsonAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public Mono<Void> commence(ServerWebExchange exchange, org.springframework.security.core.AuthenticationException ex) {
    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
    exchange.getResponse().getHeaders().add("Content-Type", "application/json");

    byte[] bytes = null;
    try {
      bytes = mapper.writeValueAsBytes(ApiException.of(ex));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return exchange.getResponse()
      .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
  }
}
