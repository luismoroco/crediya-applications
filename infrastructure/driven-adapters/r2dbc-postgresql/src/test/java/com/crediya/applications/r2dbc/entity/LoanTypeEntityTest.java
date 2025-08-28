package com.crediya.applications.r2dbc.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class LoanTypeEntityTest {

  @Test
  void testBuilderAndGetters() {
    LoanTypeEntity entity = LoanTypeEntity.builder()
      .loanTypeId(1L)
      .name("Personal Loan")
      .maximumAmount(10000L)
      .minimumAmount(1000L)
      .interestRate(new BigDecimal("12.5"))
      .automaticValidation(true)
      .build();

    assertThat(entity.getLoanTypeId()).isEqualTo(1L);
    assertThat(entity.getName()).isEqualTo("Personal Loan");
    assertThat(entity.getMaximumAmount()).isEqualTo(10000L);
    assertThat(entity.getMinimumAmount()).isEqualTo(1000L);
    assertThat(entity.getInterestRate()).isEqualByComparingTo("12.5");
    assertThat(entity.getAutomaticValidation()).isTrue();
  }

  @Test
  void testSetters() {
    LoanTypeEntity entity = new LoanTypeEntity();
    entity.setLoanTypeId(2L);
    entity.setName("Mortgage");
    entity.setMaximumAmount(500000L);
    entity.setMinimumAmount(50000L);
    entity.setInterestRate(new BigDecimal("7.2"));
    entity.setAutomaticValidation(false);

    assertThat(entity.getLoanTypeId()).isEqualTo(2L);
    assertThat(entity.getName()).isEqualTo("Mortgage");
    assertThat(entity.getMaximumAmount()).isEqualTo(500000L);
    assertThat(entity.getMinimumAmount()).isEqualTo(50000L);
    assertThat(entity.getInterestRate()).isEqualByComparingTo("7.2");
    assertThat(entity.getAutomaticValidation()).isFalse();
  }
}
