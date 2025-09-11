package com.crediya.applications.model.application.gateways.event;

import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.application.gateways.dto.MinimalLoanDTO;
import com.crediya.applications.model.loantype.LoanType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class AutomaticEvaluationLoanRequestStartedEvent {

  private Long basicWaging;
  private Application application;
  private LoanType loanType;
  private List<MinimalLoanDTO> minimalLoanDTOS;
}
