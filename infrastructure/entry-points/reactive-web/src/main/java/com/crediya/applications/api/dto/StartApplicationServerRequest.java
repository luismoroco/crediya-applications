package com.crediya.applications.api.dto;

import com.crediya.applications.model.loantype.LoanTypeEnum;
import com.crediya.applications.usecase.application.dto.StartApplicationDTO;
import com.crediya.common.mapping.Mappable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartApplicationServerRequest implements Mappable<StartApplicationDTO> {

  @NotNull @Min(0) private Long amount;
  @NotNull @Min(0) private Integer deadline;
  @NotBlank @Email private String email;
  @NotNull private LoanTypeEnum loanType;
  @NotNull private String identityCardNumber;

  @Override
  public Class<StartApplicationDTO> getTargetClass() {
    return StartApplicationDTO.class;
  }
}
