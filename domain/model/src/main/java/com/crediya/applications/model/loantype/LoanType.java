package com.crediya.applications.model.loantype;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanType {

  private Long loanTypeId;
  private String name;
  private Long maximumAmount;
  private Long minimumAmount;
  private BigDecimal interestRate;
  private Boolean automaticValidation;

  public enum Field {

    LOAN_TYPE_ID,
    NAME,
    MAXIMUM_AMOUNT,
    MINIMUM_AMOUNT,
    INTEREST_RATE,
    AUTOMATIC_VALIDATION,;

    @Override
    public String toString() {
      return this.name().replaceAll("_", " ");
    }
  }
}
