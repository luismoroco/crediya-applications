package com.crediya.applications.consumer;

import com.crediya.applications.usecase.application.gateway.AuthGateway;
import com.crediya.applications.usecase.application.gateway.dto.UserDTO;
import com.crediya.common.LogCatalog;
import com.crediya.common.exc.NotFoundException;

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
  public Mono<UserDTO> getUserByIdentityCardNumber(String identityCardNumber) {
    return this.webClient.get()
      .uri("/api/v1/users/{identity_card_number}", identityCardNumber)
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
}
