package com.crediya.applications.model.application.gateways.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDTOTest {

  @Test
  void testBuilderCreatesUserDTO() {
    LocalDate birthDate = LocalDate.of(1990, 1, 1);

    UserDTO user = UserDTO.builder()
      .userId(1L)
      .userRoleId(2L)
      .firstName("Jane")
      .lastName("Doe")
      .email("jane.doe@example.com")
      .identityCardNumber("ABC123")
      .password("secret")
      .phoneNumber("987654321")
      .basicWaging(3000L)
      .birthDate(birthDate)
      .address("123 Main St")
      .userRole("ADMIN")
      .build();

    assertThat(user.getUserId()).isEqualTo(1L);
    assertThat(user.getFirstName()).isEqualTo("Jane");
    assertThat(user.getLastName()).isEqualTo("Doe");
    assertThat(user.getEmail()).isEqualTo("jane.doe@example.com");
    assertThat(user.getIdentityCardNumber()).isEqualTo("ABC123");
    assertThat(user.getUserRole()).isEqualTo("ADMIN");
    assertThat(user.getBirthDate()).isEqualTo(birthDate);
  }

  @Test
  void testSettersAndGetters() {
    UserDTO user = new UserDTO();
    user.setUserId(5L);
    user.setFirstName("John");
    user.setLastName("Smith");

    assertThat(user.getUserId()).isEqualTo(5L);
    assertThat(user.getFirstName()).isEqualTo("John");
    assertThat(user.getLastName()).isEqualTo("Smith");
  }

  @Test
  void testEqualsAndHashCode() {
    UserDTO user1 = UserDTO.builder()
      .userId(1L)
      .firstName("Alice")
      .build();

    UserDTO user2 = UserDTO.builder()
      .userId(1L)
      .firstName("Alice")
      .build();

    assertThat(user1).isEqualTo(user2);
    assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
  }

  @Test
  void testToString() {
    UserDTO user = UserDTO.builder()
      .userId(10L)
      .firstName("Bob")
      .build();

    String str = user.toString();

    assertThat(str).isNotNull();
    assertThat(str).contains("Bob");
    assertThat(str).contains("10");
  }
}
