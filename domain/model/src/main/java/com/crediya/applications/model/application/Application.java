package com.crediya.applications.model.application;

import com.crediya.applications.model.loantype.LoanTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Application {

  private Long applicationId;
  private Long amount;
  private Integer deadline;
  private String email;
  private Integer applicationStatusId;
  private Integer loanTypeId;

  public Application() {
  }

  public Application(Long applicationId, Long amount, Integer deadline, String email, Integer applicationStatusId,
                     Integer loanTypeId) {
    this.applicationId = applicationId;
    this.amount = amount;
    this.deadline = deadline;
    this.email = email;
    this.applicationStatusId = applicationStatusId;
    this.loanTypeId = loanTypeId;
  }

  public void setApplicationStatus(ApplicationStatus applicationStatus) {
    this.applicationStatusId = applicationStatus.getCode();
  }

  public void setLoanType(LoanTypeEnum loanType) {
    this.loanTypeId = loanType.getCode();
  }

}
