package com.crediya.applications.usecase.application.dto;

import com.crediya.applications.model.loantype.LoanTypeEnum;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StartApplicationDTOTest {

  @Test
  void testBuilder() {
    StartApplicationDTO dto = StartApplicationDTO.builder()
      .amount(100000L)
      .deadline(12)
      .email("john.doe@example.com")
      .loanType(LoanTypeEnum.PERSONAL_LOAN)
      .identityCardNumber("12345678")
      .build();

    assertThat(dto.getAmount()).isEqualTo(100000L);
    assertThat(dto.getDeadline()).isEqualTo(12);
    assertThat(dto.getEmail()).isEqualTo("john.doe@example.com");
    assertThat(dto.getLoanType()).isEqualTo(LoanTypeEnum.PERSONAL_LOAN);
    assertThat(dto.getIdentityCardNumber()).isEqualTo("12345678");
  }

  @Test
  void testAllArgsConstructor() {
    StartApplicationDTO dto = new StartApplicationDTO(
      200000L,
      24,
      "jane.doe@example.com",
      LoanTypeEnum.PERSONAL_LOAN,
      "87654321"
    );

    assertThat(dto.getAmount()).isEqualTo(200000L);
    assertThat(dto.getDeadline()).isEqualTo(24);
    assertThat(dto.getEmail()).isEqualTo("jane.doe@example.com");
    assertThat(dto.getLoanType()).isEqualTo(LoanTypeEnum.PERSONAL_LOAN);
    assertThat(dto.getIdentityCardNumber()).isEqualTo("87654321");
  }

  @Test
  void testNoArgsConstructorAndSetters() {
    StartApplicationDTO dto = new StartApplicationDTO();
    dto.setAmount(300000L);
    dto.setDeadline(36);
    dto.setEmail("jack.doe@example.com");
    dto.setLoanType(LoanTypeEnum.PERSONAL_LOAN);
    dto.setIdentityCardNumber("11223344");

    assertThat(dto.getAmount()).isEqualTo(300000L);
    assertThat(dto.getDeadline()).isEqualTo(36);
    assertThat(dto.getEmail()).isEqualTo("jack.doe@example.com");
    assertThat(dto.getLoanType()).isEqualTo(LoanTypeEnum.PERSONAL_LOAN);
    assertThat(dto.getIdentityCardNumber()).isEqualTo("11223344");
  }
}
