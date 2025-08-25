package com.crediya.applications.model.application;

import com.crediya.applications.model.loantype.LoanType;

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
  private LoanType loanType;

  public Application() {
  }

  public Application(Long applicationId, Long amount, Integer deadline, String email, Integer applicationStatusId,
                     LoanType loanType) {
    this.applicationId = applicationId;
    this.amount = amount;
    this.deadline = deadline;
    this.email = email;
    this.applicationStatusId = applicationStatusId;
    this.loanType = loanType;
  }

}
