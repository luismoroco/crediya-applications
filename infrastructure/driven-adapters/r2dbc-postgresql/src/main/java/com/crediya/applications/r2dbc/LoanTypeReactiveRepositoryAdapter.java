package com.crediya.applications.r2dbc;

import com.crediya.applications.model.loantype.LoanType;
import com.crediya.applications.model.loantype.gateways.LoanTypeRepository;
import com.crediya.applications.r2dbc.entity.LoanTypeEntity;
import com.crediya.applications.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LoanTypeReactiveRepositoryAdapter extends ReactiveAdapterOperations<
  LoanType,
  LoanTypeEntity,
  Long,
  LoanTypeReactiveRepository
  > implements LoanTypeRepository {

  public LoanTypeReactiveRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper) {
    super(repository, mapper, d -> mapper.map(d, LoanType.class));
  }
}
