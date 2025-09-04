package com.crediya.applications.consumer;

import com.crediya.applications.model.application.gateways.AuthClient;
import com.crediya.applications.model.application.gateways.dto.UserDTO;
import com.crediya.common.LogCatalog;
import com.crediya.common.exc.NotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthClientAdapter implements AuthClient {

  private final WebClient webClient;

  @Override
  public Mono<UserDTO> getUserByIdentityCardNumber(String identityCardNumber) {
    return this.webClient.get()
      .uri("/api/v1/users/{identity_card_number}", identityCardNumber) // TODO: use env instead
      .accept(MediaType.APPLICATION_JSON)
      .exchangeToMono(response ->
        response.statusCode().is2xxSuccessful()
          ? response.bodyToMono(UserDTO.class)
          : Mono.error(new NotFoundException(LogCatalog.ENTITY_NOT_FOUND.of(
          "identity_card_number",
          identityCardNumber
        )))
      );
  }

  @Override
  public Flux<UserDTO> getUsers(List<String> identityCardNumbers) {
    return this.webClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/api/v1/users")
        .queryParam("identity_card_numbers", identityCardNumbers.toArray())
        .build()
      )
      .accept(MediaType.APPLICATION_JSON)
      .retrieve()
      .bodyToFlux(UserDTO.class);
  }
}
