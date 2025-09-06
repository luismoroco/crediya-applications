package com.crediya.applications.r2dbc;

import com.crediya.applications.model.loantype.LoanType;
import com.crediya.applications.r2dbc.entity.LoanTypeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanTypeReactiveRepositoryAdapterTest {

  @InjectMocks
  LoanTypeReactiveRepositoryAdapter repositoryAdapter;

  @Mock
  LoanTypeReactiveRepository repository;

  @Mock
  ObjectMapper mapper;

  private static LoanTypeEntity createLoanTypeEntity() {
    return LoanTypeEntity.builder()
      .loanTypeId(1L)
      .name("Personal Loan")
      .maximumAmount(10000L)
      .minimumAmount(1000L)
      .interestRate(BigDecimal.valueOf(1.0))
      .automaticValidation(true)
      .build();
  }

  private static LoanType createLoanType() {
    return LoanType.builder()
      .loanTypeId(1L)
      .name("Personal Loan")
      .maximumAmount(10000L)
      .minimumAmount(1000L)
      .interestRate(BigDecimal.valueOf(1.0))
      .automaticValidation(true)
      .build();
  }

  @Test
  void mustFindValueById() {
    LoanTypeEntity entity = createLoanTypeEntity();
    LoanType loanType = createLoanType();

    when(repository.findById(1L)).thenReturn(Mono.just(entity));
    when(mapper.map(entity, LoanType.class)).thenReturn(loanType);

    Mono<LoanType> result = repositoryAdapter.findById(1L);

    StepVerifier.create(result)
      .expectNextMatches(l -> l.getName().equals("Personal Loan"))
      .verifyComplete();
  }

  @Test
  void mustFindAllValues() {
    LoanTypeEntity entity = createLoanTypeEntity();
    LoanType loanType = createLoanType();

    when(repository.findAll()).thenReturn(Flux.just(entity));
    when(mapper.map(entity, LoanType.class)).thenReturn(loanType);

    Flux<LoanType> result = repositoryAdapter.findAll();

    StepVerifier.create(result)
      .expectNextMatches(l -> l.getMaximumAmount().equals(10000L))
      .verifyComplete();
  }

  @Test
  void mustSaveValue() {
    LoanTypeEntity entity = createLoanTypeEntity();
    LoanType loanType = createLoanType();

    when(mapper.map(loanType, LoanTypeEntity.class)).thenReturn(entity);
    when(mapper.map(entity, LoanType.class)).thenReturn(loanType);
    when(repository.save(entity)).thenReturn(Mono.just(entity));

    Mono<LoanType> result = repositoryAdapter.save(loanType);

    StepVerifier.create(result)
      .expectNextMatches(LoanType::getAutomaticValidation)
      .verifyComplete();
  }
}
