package com.crediya.applications.model.loantype;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoanTypeEnumTest {

  @Test
  void testPersonalLoanEnumValues() {
    LoanTypeEnum loanType = LoanTypeEnum.PERSONAL_LOAN;

    assertThat(loanType.getCode()).isEqualTo(1);
    assertThat(loanType.getName()).isEqualTo("PERSONAL_LOAN");
    assertThat(loanType.getMinimumAmount()).isEqualTo(50000L);
    assertThat(loanType.getMaximumAmount()).isEqualTo(5000000L);
    assertThat(loanType.getInterestRate()).isEqualTo(12.5);
    assertThat(loanType.getAutomaticValidation()).isTrue();
  }

  @Test
  void testEnumValuesCount() {
    LoanTypeEnum[] values = LoanTypeEnum.values();
    assertThat(values).hasSize(1); // Solo hay un tipo de préstamo definido
    assertThat(values[0]).isEqualTo(LoanTypeEnum.PERSONAL_LOAN);
  }

  @Test
  void testValueOf() {
    LoanTypeEnum loanType = LoanTypeEnum.valueOf("PERSONAL_LOAN");
    assertThat(loanType).isEqualTo(LoanTypeEnum.PERSONAL_LOAN);
  }
}
