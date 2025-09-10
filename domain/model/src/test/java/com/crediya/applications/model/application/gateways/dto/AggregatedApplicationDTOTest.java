package com.crediya.applications.model.application.gateways.dto;

import com.crediya.applications.model.application.ApplicationStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class AggregatedApplicationDTOTest {

  @Test
  void testBuilderCreatesAggregatedApplication() {
    AggregatedApplicationDTO app = AggregatedApplicationDTO.builder()
      .applicationId(1L)
      .amount(10000L)
      .deadline(12)
      .email("test@example.com")
      .name("Test User")
      .loanTypeId(2)
      .interestRate(BigDecimal.valueOf(5.5))
      .applicationStatusId(1)
      .basicWaging(2000L)
      .totalDebt(BigDecimal.valueOf(5000))
      .build();

    assertThat(app.getApplicationId()).isEqualTo(1L);
    assertThat(app.getAmount()).isEqualTo(10000L);
    assertThat(app.getDeadline()).isEqualTo(12);
    assertThat(app.getEmail()).isEqualTo("test@example.com");
    assertThat(app.getName()).isEqualTo("Test User");
    assertThat(app.getLoanTypeId()).isEqualTo(2);
    assertThat(app.getInterestRate()).isEqualTo(BigDecimal.valueOf(5.5));
    assertThat(app.getApplicationStatusId()).isEqualTo(1);
    assertThat(app.getBasicWaging()).isEqualTo(2000L);
    assertThat(app.getTotalDebt()).isEqualTo(BigDecimal.valueOf(5000));
  }

  @Test
  void testUpdateWithUserDTO() {
    AggregatedApplicationDTO app = AggregatedApplicationDTO.builder()
      .name("Old Name")
      .basicWaging(1000L)
      .build();

    UserDTO user = UserDTO.builder()
      .firstName("Alice")
      .lastName("Smith")
      .basicWaging(3000L)
      .build();

    app.update(user);

    assertThat(app.getName()).isEqualTo("Alice Smith");
    assertThat(app.getBasicWaging()).isEqualTo(3000L);
  }

  @Test
  void testGetApplicationStatus() {
    AggregatedApplicationDTO app = AggregatedApplicationDTO.builder()
      .applicationStatusId(1)
      .build();

    ApplicationStatus status = app.getApplicationStatus();

    assertThat(status).isNotNull();
    assertThat(status.getCode()).isEqualTo(1);
  }
}
