package com.crediya.applications.model.application;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AggregatedApplication {

  private Long applicationId;
  private Long amount;
  private Integer deadline;
  private String email;
  private String name;
  private Integer loanTypeId;
  private BigDecimal interestRate;
  private Integer applicationStatusId;
  private Long basicWaging;
  private BigDecimal totalDebt;
}
