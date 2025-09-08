package com.crediya.applications.api.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import reactor.core.publisher.Flux;

import javax.crypto.SecretKey;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private static final String[] PUBLIC_URLS = { "/api/v1/public/**" };

  private final CustomServerAccessDeniedHandler serverAccessDeniedHandler;

  @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
  private String secret;

  @Bean
  public ReactiveJwtDecoder jwtDecoder() {
    byte[] keyBytes = Decoders.BASE64URL.decode(secret);
    SecretKey key = Keys.hmacShaKeyFor(keyBytes);
    return NimbusReactiveJwtDecoder.withSecretKey(key)
      .macAlgorithm(MacAlgorithm.HS256)
      .build();
  }

  @Bean
  public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
    ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
      String role = jwt.getClaims().get("role").toString();
      return Flux.just(new SimpleGrantedAuthority("ROLE_" + role));
    });

    return converter;
  }

  @Bean
  public SecurityWebFilterChain security(ServerHttpSecurity http) {
    return http
      .csrf(ServerHttpSecurity.CsrfSpec::disable)
      .authorizeExchange(exchanges -> exchanges
        .pathMatchers(PUBLIC_URLS).permitAll()
        .pathMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
        .anyExchange().permitAll()
      )
      .exceptionHandling(spec -> spec
        .accessDeniedHandler(serverAccessDeniedHandler))
      .oauth2ResourceServer(oauth2 ->
        oauth2
          .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
          .authenticationEntryPoint(authenticationEntryPoint())
      )
      .build();
  }

  @Bean
  public ServerAuthenticationEntryPoint authenticationEntryPoint() {
    return new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED);
  }
}
