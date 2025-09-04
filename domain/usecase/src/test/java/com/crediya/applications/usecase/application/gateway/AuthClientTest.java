package com.crediya.applications.usecase.application.gateway;

import com.crediya.applications.model.application.gateways.AuthClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class AuthClientTest {

  private AuthClient authClient;

  @BeforeEach
  void setUp() {
    authClient = Mockito.mock(AuthClient.class);
  }

  @Test
  void testGetUserByIdentityCardNumberReturnsTrue() {
    String email = "test@example.com";
    when(authClient.getUserByIdentityCardNumber(email)).thenReturn(Mono.just(true));

    StepVerifier.create(authClient.getUserByIdentityCardNumber(email))
      .expectNext(true)
      .verifyComplete();

    verify(authClient, times(1)).getUserByIdentityCardNumber(email);
  }

  @Test
  void testGetUserByIdentityCardNumberReturnsFalse() {
    String email = "notfound@example.com";
    when(authClient.getUserByIdentityCardNumber(email)).thenReturn(Mono.just(false));

    StepVerifier.create(authClient.getUserByIdentityCardNumber(email))
      .expectNext(false)
      .verifyComplete();

    verify(authClient, times(1)).getUserByIdentityCardNumber(email);
  }
}
