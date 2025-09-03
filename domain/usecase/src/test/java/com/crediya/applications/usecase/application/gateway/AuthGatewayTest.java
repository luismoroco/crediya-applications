package com.crediya.applications.usecase.application.gateway;

import com.crediya.applications.model.application.gateways.AuthGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class AuthGatewayTest {

  private AuthGateway authGateway;

  @BeforeEach
  void setUp() {
    authGateway = Mockito.mock(AuthGateway.class);
  }

  @Test
  void testGetUserByIdentityCardNumberReturnsTrue() {
    String email = "test@example.com";
    when(authGateway.getUserByIdentityCardNumber(email)).thenReturn(Mono.just(true));

    StepVerifier.create(authGateway.getUserByIdentityCardNumber(email))
      .expectNext(true)
      .verifyComplete();

    verify(authGateway, times(1)).getUserByIdentityCardNumber(email);
  }

  @Test
  void testGetUserByIdentityCardNumberReturnsFalse() {
    String email = "notfound@example.com";
    when(authGateway.getUserByIdentityCardNumber(email)).thenReturn(Mono.just(false));

    StepVerifier.create(authGateway.getUserByIdentityCardNumber(email))
      .expectNext(false)
      .verifyComplete();

    verify(authGateway, times(1)).getUserByIdentityCardNumber(email);
  }
}
