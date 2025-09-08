package com.crediya.applications.api.config;

import com.crediya.common.exc.UnauthorizedException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class CustomServerAccessDeniedHandler implements ServerAccessDeniedHandler {

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
    return exchange.getPrincipal()
      .cast(JwtAuthenticationToken.class)
      .flatMap(jwtAuthenticationToken -> Mono.<Void>error(
        new UnauthorizedException(
          "Not allowed to access this resource",
          Map.of("error", jwtAuthenticationToken.getAuthorities().toString()))
      ))
      .switchIfEmpty(Mono.error(new UnauthorizedException("Not allowed to access this resource")));
  }
}
