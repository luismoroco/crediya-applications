package com.crediya.applications.consumer;

import com.crediya.applications.usecase.application.gateway.AuthGateway;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestAuthGateway implements AuthGateway {

  private final WebClient webClient;

  @Override
  public Mono<Boolean> userExistsByEmail(String email) {
    return this.webClient.get()
      .uri("/api/v1/users/{email}", email)
      .accept(MediaType.APPLICATION_JSON)
      .exchangeToMono(response -> response.statusCode()
        .is2xxSuccessful() ? Mono.just(true) : Mono.just(false))
      .onErrorReturn(false);
  }
}
