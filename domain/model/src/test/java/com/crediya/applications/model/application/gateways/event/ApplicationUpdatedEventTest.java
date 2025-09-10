package com.crediya.applications.model.application.gateways.event;

import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.application.ApplicationStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationUpdatedEventTest {

  @Test
  void testFromApplication_createsCorrectEvent() {
    Application application = Application.builder()
      .applicationId(100L)
      .amount(5000L)
      .deadline(12)
      .email("test@example.com")
      .applicationStatusId(ApplicationStatus.PENDING.getCode())
      .loanTypeId(1L)
      .build();

    ApplicationUpdatedEvent event = ApplicationUpdatedEvent.from(application);

    assertThat(event.getApplicationId()).isEqualTo(application.getApplicationId());
    assertThat(event.getAmount()).isEqualTo(application.getAmount());
    assertThat(event.getDeadline()).isEqualTo(application.getDeadline());
    assertThat(event.getEmail()).isEqualTo(application.getEmail());
    assertThat(event.getApplicationStatus()).isEqualTo(ApplicationStatus.PENDING);
  }

  @Test
  void testSettersAndGetters_workCorrectly() {
    ApplicationUpdatedEvent event = new ApplicationUpdatedEvent();

    event.setApplicationId(101L);
    event.setAmount(10000L);
    event.setDeadline(24);
    event.setEmail("user@example.com");
    event.setApplicationStatus(ApplicationStatus.REJECTED);

    assertThat(event.getApplicationId()).isEqualTo(101L);
    assertThat(event.getAmount()).isEqualTo(10000L);
    assertThat(event.getDeadline()).isEqualTo(24);
    assertThat(event.getEmail()).isEqualTo("user@example.com");
    assertThat(event.getApplicationStatus()).isEqualTo(ApplicationStatus.REJECTED);
  }

  @Test
  void testToString_returnsFormattedString() {
    ApplicationUpdatedEvent event = ApplicationUpdatedEvent.builder()
      .applicationId(1L)
      .amount(2000L)
      .deadline(6)
      .applicationStatus(ApplicationStatus.PENDING)
      .email("abc@example.com")
      .build();

    String result = event.toString();

    assertThat(result).isEqualTo(
      "[applicationId=1][amount=2000][deadline=6][applicationStatus=PENDING][email=abc@example.com]"
    );
  }
}
