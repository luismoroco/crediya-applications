package com.crediya.applications.model.application.gateways.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MinimalLoanDTOTest {

  @Test
  void testNoArgsConstructorAndSetters() {
    MinimalLoanDTO dto = new MinimalLoanDTO();
    dto.setLoanId(1L);
    dto.setAmount(5000L);
    dto.setDeadline(12);
    dto.setInterestRate(BigDecimal.valueOf(5.5));

    assertThat(dto.getLoanId()).isEqualTo(1L);
    assertThat(dto.getAmount()).isEqualTo(5000L);
    assertThat(dto.getDeadline()).isEqualTo(12);
    assertThat(dto.getInterestRate()).isEqualByComparingTo("5.5");
  }

  @Test
  void testAllArgsConstructor() {
    MinimalLoanDTO dto = new MinimalLoanDTO(
      2L,
      10000L,
      24,
      BigDecimal.valueOf(7.25)
    );

    assertThat(dto.getLoanId()).isEqualTo(2L);
    assertThat(dto.getAmount()).isEqualTo(10000L);
    assertThat(dto.getDeadline()).isEqualTo(24);
    assertThat(dto.getInterestRate()).isEqualByComparingTo("7.25");
  }

  @Test
  void testBuilder() {
    MinimalLoanDTO dto = MinimalLoanDTO.builder()
      .loanId(3L)
      .amount(15000L)
      .deadline(36)
      .interestRate(BigDecimal.valueOf(8.75))
      .build();

    assertThat(dto.getLoanId()).isEqualTo(3L);
    assertThat(dto.getAmount()).isEqualTo(15000L);
    assertThat(dto.getDeadline()).isEqualTo(36);
    assertThat(dto.getInterestRate()).isEqualByComparingTo("8.75");
  }

  @Test
  void testToBuilder() {
    MinimalLoanDTO original = MinimalLoanDTO.builder()
      .loanId(4L)
      .amount(20000L)
      .deadline(48)
      .interestRate(BigDecimal.valueOf(10.0))
      .build();

    MinimalLoanDTO modified = original.toBuilder()
      .amount(25000L)
      .build();

    assertThat(modified.getLoanId()).isEqualTo(4L);
    assertThat(modified.getAmount()).isEqualTo(25000L);
    assertThat(modified.getDeadline()).isEqualTo(48);
    assertThat(modified.getInterestRate()).isEqualByComparingTo("10.0");
  }
}
