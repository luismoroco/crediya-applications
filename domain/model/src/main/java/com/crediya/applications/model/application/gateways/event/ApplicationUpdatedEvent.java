package com.crediya.applications.model.application.gateways.event;

import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.application.ApplicationStatus;
import com.crediya.applications.model.loantype.LoanType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ApplicationUpdatedEvent {

  private Long applicationId;
  private Long amount;
  private Integer deadline;
  private ApplicationStatus applicationStatus;
  private String email;
  private BigDecimal interestRate;

  public static ApplicationUpdatedEvent from(Application application, LoanType loanType) {
    return ApplicationUpdatedEvent.builder()
      .applicationId(application.getApplicationId())
      .amount(application.getAmount())
      .deadline(application.getDeadline())
      .applicationStatus(application.getApplicationStatus())
      .email(application.getEmail())
      .interestRate(loanType.getInterestRate())
      .build();
  }

  @Override
  public String toString() {
    return String.format("[applicationId=%s][amount=%s][deadline=%s][applicationStatus=%s][email=%s]",
      applicationId, amount, deadline, applicationStatus, email);
  }
}
