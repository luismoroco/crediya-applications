package com.crediya.applications.usecase.application.gateway;

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
  void testUserExistsByEmailReturnsTrue() {
    String email = "test@example.com";
    when(authGateway.userExistsByEmail(email)).thenReturn(Mono.just(true));

    StepVerifier.create(authGateway.userExistsByEmail(email))
      .expectNext(true)
      .verifyComplete();

    verify(authGateway, times(1)).userExistsByEmail(email);
  }

  @Test
  void testUserExistsByEmailReturnsFalse() {
    String email = "notfound@example.com";
    when(authGateway.userExistsByEmail(email)).thenReturn(Mono.just(false));

    StepVerifier.create(authGateway.userExistsByEmail(email))
      .expectNext(false)
      .verifyComplete();

    verify(authGateway, times(1)).userExistsByEmail(email);
  }
}
