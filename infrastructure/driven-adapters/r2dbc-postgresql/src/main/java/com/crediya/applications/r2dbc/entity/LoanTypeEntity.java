package com.crediya.applications.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("loan_type")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoanTypeEntity {

  @Id private Long loanTypeId;
  private String name;
  private Long maximumAmount;
  private Long minimumAmount;
  private Integer interestRate;
  private Boolean automaticValidation;
}
