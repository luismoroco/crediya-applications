package com.crediya.applications.model.application.gateways;

import com.crediya.applications.model.application.gateways.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

public class AuthClientTest {

  private AuthClient authClient;

  @BeforeEach
  void setUp() {
    authClient = Mockito.mock(AuthClient.class);
  }

  @Test
  void testGetUserByIdentityCardNumber() {
    String identityCard = "12345678";

    UserDTO user = UserDTO.builder()
      .userId(1L)
      .userRoleId(2L)
      .firstName("John")
      .lastName("Doe")
      .email("john.doe@example.com")
      .identityCardNumber(identityCard)
      .password("encoded-pass")
      .phoneNumber("987654321")
      .basicWaging(2500L)
      .birthDate(LocalDate.of(1995, 5, 20))
      .address("123 Main St")
      .userRole("CUSTOMER")
      .build();

    when(authClient.getUserByIdentityCardNumber(identityCard)).thenReturn(Mono.just(user));

    StepVerifier.create(authClient.getUserByIdentityCardNumber(identityCard))
      .expectNextMatches(u ->
        u.getUserId().equals(1L) &&
          u.getFirstName().equals("John") &&
          u.getLastName().equals("Doe") &&
          u.getEmail().equals("john.doe@example.com") &&
          u.getIdentityCardNumber().equals(identityCard) &&
          u.getUserRole().equals("CUSTOMER")
      )
      .verifyComplete();

    verify(authClient, times(1)).getUserByIdentityCardNumber(identityCard);
  }

  @Test
  void testGetUsers() {
    List<String> identityCards = List.of("12345678", "87654321");

    UserDTO user1 = UserDTO.builder()
      .userId(1L)
      .firstName("Alice")
      .lastName("Smith")
      .identityCardNumber("12345678")
      .userRole("CUSTOMER")
      .build();

    UserDTO user2 = UserDTO.builder()
      .userId(2L)
      .firstName("Bob")
      .lastName("Johnson")
      .identityCardNumber("87654321")
      .userRole("CUSTOMER")
      .build();

    when(authClient.getUsers(identityCards)).thenReturn(Flux.just(user1, user2));

    StepVerifier.create(authClient.getUsers(identityCards))
      .expectNextMatches(u -> u.getUserId().equals(1L) &&
        u.getIdentityCardNumber().equals("12345678") &&
        u.getUserRole().equals("CUSTOMER"))
      .expectNextMatches(u -> u.getUserId().equals(2L) &&
        u.getIdentityCardNumber().equals("87654321") &&
        u.getUserRole().equals("CUSTOMER"))
      .verifyComplete();

    verify(authClient, times(1)).getUsers(identityCards);
  }
}
