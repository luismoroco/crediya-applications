package com.crediya.applications.model.application;

import com.crediya.applications.model.loantype.LoanTypeEnum;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest {

  @Test
  void testNoArgsConstructorAndSetters() {
    Application app = new Application();
    app.setApplicationId(1L);
    app.setAmount(5000L);
    app.setDeadline(12);
    app.setEmail("test@example.com");
    app.setApplicationStatusId(2);
    app.setLoanTypeId(3);

    assertThat(app.getApplicationId()).isEqualTo(1L);
    assertThat(app.getAmount()).isEqualTo(5000L);
    assertThat(app.getDeadline()).isEqualTo(12);
    assertThat(app.getEmail()).isEqualTo("test@example.com");
    assertThat(app.getApplicationStatusId()).isEqualTo(2);
    assertThat(app.getLoanTypeId()).isEqualTo(3);
  }

  @Test
  void testAllArgsConstructor() {
    Application app = new Application(10L, 10000L, 24, "user@mail.com", 1, 2);

    assertThat(app.getApplicationId()).isEqualTo(10L);
    assertThat(app.getAmount()).isEqualTo(10000L);
    assertThat(app.getDeadline()).isEqualTo(24);
    assertThat(app.getEmail()).isEqualTo("user@mail.com");
    assertThat(app.getApplicationStatusId()).isEqualTo(1);
    assertThat(app.getLoanTypeId()).isEqualTo(2);
  }

  @Test
  void testSetApplicationStatus() {
    Application app = new Application();
    app.setApplicationStatus(ApplicationStatus.ACTIVE);

    assertThat(app.getApplicationStatusId()).isEqualTo(ApplicationStatus.ACTIVE.getCode());
  }

  @Test
  void testSetLoanType() {
    Application app = new Application();
    app.setLoanType(LoanTypeEnum.PERSONAL_LOAN);

    assertThat(app.getLoanTypeId()).isEqualTo(LoanTypeEnum.PERSONAL_LOAN.getCode());
  }

  @Test
  void testFieldEnumLabels() {
    assertThat(Application.Field.AMOUNT.toString()).isEqualTo("AMOUNT");
    assertThat(Application.Field.DEADLINE.toString()).isEqualTo("DEADLINE");
    assertThat(Application.Field.EMAIL.toString()).isEqualTo("EMAIL");
    assertThat(Application.Field.APPLICATION_STATUS.toString()).isEqualTo("APPLICATION STATUS");
    assertThat(Application.Field.LOAN_TYPE.toString()).isEqualTo("LOAN TYPE");
  }
}
