package com.crediya.applications.usecase.application.dto;

import com.crediya.applications.model.loantype.LoanTypeEnum;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StartApplicationDTO {

  private Long amount;
  private Integer deadline;
  private LoanTypeEnum loanType;
  private String identityCardNumber;

  public StartApplicationDTO() {
  }

  public StartApplicationDTO(Long amount, Integer deadline, LoanTypeEnum loanType,
                             String identityCardNumber) {
    this.amount = amount;
    this.deadline = deadline;
    this.loanType = loanType;
    this.identityCardNumber = identityCardNumber;
  }

  @Override
  public String toString() {
    return String.format("[amount=%s][deadline=%s][loanType=%s][identityCardNumber=%s]", amount, deadline, loanType,
      identityCardNumber);
  }
}
