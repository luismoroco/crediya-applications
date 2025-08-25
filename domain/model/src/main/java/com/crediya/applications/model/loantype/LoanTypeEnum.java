package com.crediya.applications.model.loantype;

import lombok.Getter;

@Getter
public enum LoanTypeEnum {
  PERSONAL_LOAN(1, "PERSONAL_LOAN", 50000L, 5000000L, 12.5, true);

  private final Integer code;
  private final String name;
  private final Long minimumAmount;
  private final Long maximumAmount;
  private final Double interestRate;
  private final Boolean automaticValidation;


  LoanTypeEnum(Integer code, String name, Long minimumAmount, Long maximumAmount, Double interestRate, Boolean automaticValidation) {
    this.code = code;
    this.name = name;
    this.minimumAmount = minimumAmount;
    this.maximumAmount = maximumAmount;
    this.interestRate = interestRate;
    this.automaticValidation = automaticValidation;
  }
}
