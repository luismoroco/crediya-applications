package com.crediya.applications.consumer;

import com.crediya.applications.consumer.config.RestConsumerProperties;
import com.crediya.applications.model.application.gateways.dto.UserDTO;
import com.crediya.common.exc.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

class AuthClientAdapterTest {

  private WebClient webClient;
  private RestConsumerProperties properties;
  private AuthClientAdapter adapter;

  @BeforeEach
  void setUp() {
    webClient = mock(WebClient.class);

    properties = new RestConsumerProperties();
    var authConfig = new RestConsumerProperties.CrediyaAuthConfig();
    var paths = new RestConsumerProperties.CrediyaAuthConfig.AuthPath();
    paths.setGetUserByIdentityCardNumber("/users/{identityCardNumber}");
    paths.setGetUsers("/users");
    authConfig.setPath(paths);
    properties.setCrediyaAuth(authConfig);

    adapter = new AuthClientAdapter(webClient, properties);
  }

  @Test
  void getUserByIdentityCardNumber_returnsUser() {
    UserDTO user = UserDTO.builder().userId(1L).identityCardNumber("123").build();

    AuthClientAdapter spyAdapter = spy(adapter);
    doReturn(Mono.just(user)).when(spyAdapter).getUserByIdentityCardNumber("123");

    StepVerifier.create(spyAdapter.getUserByIdentityCardNumber("123"))
      .expectNext(user)
      .verifyComplete();
  }

  @Test
  void getUserByIdentityCardNumber_notFound() {
    AuthClientAdapter spyAdapter = spy(adapter);
    doReturn(Mono.error(new NotFoundException("not found")))
      .when(spyAdapter).getUserByIdentityCardNumber("999");

    StepVerifier.create(spyAdapter.getUserByIdentityCardNumber("999"))
      .expectError(NotFoundException.class)
      .verify();
  }

  @Test
  void getUsers_returnsList() {
    UserDTO u1 = UserDTO.builder().userId(1L).identityCardNumber("111").build();
    UserDTO u2 = UserDTO.builder().userId(2L).identityCardNumber("222").build();

    AuthClientAdapter spyAdapter = spy(adapter);
    doReturn(Flux.just(u1, u2)).when(spyAdapter).getUsers(List.of("111", "222"));

    StepVerifier.create(spyAdapter.getUsers(List.of("111", "222")))
      .expectNext(u1, u2)
      .verifyComplete();
  }
}
