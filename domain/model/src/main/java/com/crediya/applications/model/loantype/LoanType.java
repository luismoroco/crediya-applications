package com.crediya.applications.model.loantype;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanType {

  private Long loanTypeId;
  private String name;
  private Long maximumAmount;
  private Long minimumAmount;
  private BigDecimal interestRate;
  private Boolean automaticValidation;

  public LoanType() {
  }

  public LoanType(String name, Long loanTypeId, Long maximumAmount, Long minimumAmount, BigDecimal interestRate,
                  Boolean automaticValidation) {
    this.name = name;
    this.loanTypeId = loanTypeId;
    this.maximumAmount = maximumAmount;
    this.minimumAmount = minimumAmount;
    this.interestRate = interestRate;
    this.automaticValidation = automaticValidation;
  }

}
