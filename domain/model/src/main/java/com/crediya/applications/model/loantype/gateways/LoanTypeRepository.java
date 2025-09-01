package com.crediya.applications.model.loantype.gateways;

import com.crediya.applications.model.loantype.LoanType;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {
  Mono<LoanType> findById(Long loanTypeId);
}
