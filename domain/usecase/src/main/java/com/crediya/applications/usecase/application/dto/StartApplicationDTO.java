package com.crediya.applications.usecase.application.dto;

import com.crediya.applications.model.loantype.LoanTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartApplicationDTO {

  private Long amount;
  private Integer deadline;
  private String email;
  private LoanTypeEnum loanType;
  private String identityCardNumber;

  public StartApplicationDTO() {
  }

  public StartApplicationDTO(Long amount, Integer deadline, String email, LoanTypeEnum loanType,
                             String identityCardNumber) {
    this.amount = amount;
    this.deadline = deadline;
    this.email = email;
    this.loanType = loanType;
    this.identityCardNumber = identityCardNumber;
  }
}
