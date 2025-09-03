package com.crediya.applications.model.application.gateways.dto;

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

  public AggregatedApplication update(UserDTO userDTO) {
    this.setName(String.format("%s %s", userDTO.getFirstName(), userDTO.getLastName()));
    this.setBasicWaging(userDTO.getBasicWaging());

    return this;
  }
}
