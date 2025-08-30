package com.crediya.applications.model.application;

import com.crediya.applications.model.loantype.LoanTypeEnum;
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
  private Integer loanTypeId;

  public void setApplicationStatus(ApplicationStatus applicationStatus) {
    this.applicationStatusId = applicationStatus.getCode();
  }

  public void setLoanType(LoanTypeEnum loanType) {
    this.loanTypeId = loanType.getCode();
  }

  public enum Field {

    AMOUNT,
    DEADLINE,
    EMAIL,
    APPLICATION_STATUS,
    LOAN_TYPE;

    @Override
    public String toString() {
      return this.name().replaceAll("_", " ");
    }
  }
}
