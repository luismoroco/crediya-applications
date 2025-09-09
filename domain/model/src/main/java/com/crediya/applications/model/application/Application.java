package com.crediya.applications.model.application;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Application {

  private Long applicationId;
  private Long amount;
  private Integer deadline;
  private String email;
  private Integer applicationStatusId;
  private Long loanTypeId;

  public void setApplicationStatus(ApplicationStatus applicationStatus) {
    this.applicationStatusId = applicationStatus.getCode();
  }

  public ApplicationStatus getApplicationStatus() {
    return ApplicationStatus.fromCode(this.applicationStatusId);
  }

  public enum Field {

    AMOUNT,
    DEADLINE,
    EMAIL,
    APPLICATION_STATUS,
    LOAN_TYPE_ID;

    @Override
    public String toString() {
      return this.name().replaceAll("_", " ");
    }
  }
}
