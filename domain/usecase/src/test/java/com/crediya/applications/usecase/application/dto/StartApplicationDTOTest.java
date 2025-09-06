package com.crediya.applications.usecase.application.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StartApplicationDTOTest {

  @Test
  void testBuilder() {
    StartApplicationDTO dto = StartApplicationDTO.builder()
      .amount(100000L)
      .deadline(12)
      .identityCardNumber("12345678")
      .build();

    assertThat(dto.getAmount()).isEqualTo(100000L);
    assertThat(dto.getDeadline()).isEqualTo(12);
    assertThat(dto.getIdentityCardNumber()).isEqualTo("12345678");
  }

  @Test
  void testAllArgsConstructor() {
    StartApplicationDTO dto = new StartApplicationDTO(200000L, 24, 1L, "87654321");

    assertThat(dto.getAmount()).isEqualTo(200000L);
    assertThat(dto.getDeadline()).isEqualTo(24);
    assertThat(dto.getIdentityCardNumber()).isEqualTo("87654321");
  }

  @Test
  void testNoArgsConstructorAndSetters() {
    StartApplicationDTO dto = new StartApplicationDTO();
    dto.setAmount(300000L);
    dto.setDeadline(36);
    dto.setIdentityCardNumber("11223344");

    assertThat(dto.getAmount()).isEqualTo(300000L);
    assertThat(dto.getDeadline()).isEqualTo(36);
    assertThat(dto.getIdentityCardNumber()).isEqualTo("11223344");
  }

  @Test
  void testToString() {
    StartApplicationDTO dto = StartApplicationDTO.builder()
      .amount(1000L)
      .deadline(12)
      .loanTypeId(5L)
      .identityCardNumber("ABC123")
      .build();

    String expected = "[amount=1000][deadline=12][loanTypeId=5][identityCardNumber=ABC123]";

    assertEquals(expected, dto.toString());
  }
}
