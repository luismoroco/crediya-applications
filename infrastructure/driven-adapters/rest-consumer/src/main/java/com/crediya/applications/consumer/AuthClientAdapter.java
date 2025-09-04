package com.crediya.applications.consumer;

import com.crediya.applications.consumer.config.RestConsumerProperties;
import com.crediya.applications.model.application.gateways.AuthClient;
import com.crediya.applications.model.application.gateways.dto.UserDTO;
import com.crediya.common.LogCatalog;
import com.crediya.common.exc.NotFoundException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AuthClientAdapter implements AuthClient {

  private final WebClient webClient;
  private final RestConsumerProperties properties;

  public AuthClientAdapter(@Qualifier("crediya-auth") WebClient webClient,
                           RestConsumerProperties properties) {
    this.webClient = webClient;
    this.properties = properties;
  }

  @Override
  public Mono<UserDTO> getUserByIdentityCardNumber(String identityCardNumber) {
    return this.webClient.get()
      .uri(this.properties.getCrediyaAuth().getPath().getGetUserByIdentityCardNumber(), identityCardNumber)
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
        .path(this.properties.getCrediyaAuth().getPath().getGetUsers())
        .queryParam("identity_card_numbers", identityCardNumbers.toArray())
        .build()
      )
      .accept(MediaType.APPLICATION_JSON)
      .retrieve()
      .bodyToFlux(UserDTO.class);
  }
}
