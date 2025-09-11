package com.crediya.applications.model.application.gateways.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MinimalLoanDTO {

  private Long loanId;
  private Long amount;
  private Integer deadline;
  private BigDecimal interestRate;
}
