package com.crediya.applications.model.loantype;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LoanTypeTest {

  @Test
  void testNoArgsConstructorAndSetters() {
    LoanType loanType = new LoanType();

    loanType.setLoanTypeId(1L);
    loanType.setName("Personal Loan");
    loanType.setMaximumAmount(10000L);
    loanType.setMinimumAmount(1000L);
    loanType.setInterestRate(BigDecimal.valueOf(0.12));
    loanType.setAutomaticValidation(true);

    assertThat(loanType.getLoanTypeId()).isEqualTo(1L);
    assertThat(loanType.getName()).isEqualTo("Personal Loan");
    assertThat(loanType.getMaximumAmount()).isEqualTo(10000L);
    assertThat(loanType.getMinimumAmount()).isEqualTo(1000L);
    assertThat(loanType.getInterestRate()).isEqualTo(BigDecimal.valueOf(0.12));
    assertThat(loanType.getAutomaticValidation()).isTrue();
  }

  @Test
  void testAllArgsConstructor() {
    LoanType loanType = new LoanType(
      2L,
      "Mortgage",
      500000L,
      50000L,
      BigDecimal.valueOf(0.08),
      false
    );

    assertThat(loanType.getLoanTypeId()).isEqualTo(2L);
    assertThat(loanType.getName()).isEqualTo("Mortgage");
    assertThat(loanType.getMaximumAmount()).isEqualTo(500000L);
    assertThat(loanType.getMinimumAmount()).isEqualTo(50000L);
    assertThat(loanType.getInterestRate()).isEqualTo(BigDecimal.valueOf(0.08));
    assertThat(loanType.getAutomaticValidation()).isFalse();
  }

  @Test
  void testFieldToString() {
    assertEquals("LOAN TYPE ID", LoanType.Field.LOAN_TYPE_ID.toString());
    assertEquals("NAME", LoanType.Field.NAME.toString());
    assertEquals("MAXIMUM AMOUNT", LoanType.Field.MAXIMUM_AMOUNT.toString());
    assertEquals("MINIMUM AMOUNT", LoanType.Field.MINIMUM_AMOUNT.toString());
    assertEquals("INTEREST RATE", LoanType.Field.INTEREST_RATE.toString());
    assertEquals("AUTOMATIC VALIDATION", LoanType.Field.AUTOMATIC_VALIDATION.toString());
  }
}
