package com.crediya.applications.r2dbc.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppicationEntityTest {

  @Test
  void testBuilderAndGetters() {
    ApplicationEntity entity = ApplicationEntity.builder()
      .applicationId(1L)
      .amount(5000L)
      .deadline(12)
      .email("john@example.com")
      .applicationStatusId(2)
      .loanTypeId(3)
      .build();

    assertThat(entity.getApplicationId()).isEqualTo(1L);
    assertThat(entity.getAmount()).isEqualTo(5000L);
    assertThat(entity.getDeadline()).isEqualTo(12);
    assertThat(entity.getEmail()).isEqualTo("john@example.com");
    assertThat(entity.getApplicationStatusId()).isEqualTo(2);
    assertThat(entity.getLoanTypeId()).isEqualTo(3);
  }

  @Test
  void testSetters() {
    ApplicationEntity entity = new ApplicationEntity();
    entity.setApplicationId(10L);
    entity.setAmount(2000L);
    entity.setDeadline(24);
    entity.setEmail("jane@example.com");
    entity.setApplicationStatusId(1);
    entity.setLoanTypeId(5);

    assertThat(entity.getApplicationId()).isEqualTo(10L);
    assertThat(entity.getAmount()).isEqualTo(2000L);
    assertThat(entity.getDeadline()).isEqualTo(24);
    assertThat(entity.getEmail()).isEqualTo("jane@example.com");
    assertThat(entity.getApplicationStatusId()).isEqualTo(1);
    assertThat(entity.getLoanTypeId()).isEqualTo(5);
  }
}
