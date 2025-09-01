package com.crediya.applications.usecase.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class StartApplicationDTO {

  private Long amount;
  private Integer deadline;
  private Long loanTypeId;
  private String identityCardNumber;

  @Override
  public String toString() {
    return String.format("[amount=%s][deadline=%s][loanTypeId=%s][identityCardNumber=%s]", amount, deadline, loanTypeId,
      identityCardNumber);
  }
}
