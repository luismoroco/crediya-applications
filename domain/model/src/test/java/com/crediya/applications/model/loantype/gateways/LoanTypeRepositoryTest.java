package com.crediya.applications.model.loantype.gateways;

import com.crediya.applications.model.loantype.LoanType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

public class LoanTypeRepositoryTest {

  private LoanTypeRepository loanTypeRepository;

  @BeforeEach
  void setUp() {
    loanTypeRepository = Mockito.mock(LoanTypeRepository.class);
  }

  @Test
  void testFindByIdSuccess() {
    LoanType loanType = LoanType.builder()
      .loanTypeId(1L)
      .name("Personal Loan")
      .maximumAmount(10000L)
      .minimumAmount(1000L)
      .interestRate(BigDecimal.valueOf(5.5))
      .automaticValidation(true)
      .build();

    when(loanTypeRepository.findById(1L)).thenReturn(Mono.just(loanType));

    StepVerifier.create(loanTypeRepository.findById(1L))
      .expectNextMatches(l ->
        l.getLoanTypeId().equals(1L) &&
          l.getName().equals("Personal Loan") &&
          l.getMaximumAmount().equals(10000L) &&
          l.getMinimumAmount().equals(1000L) &&
          l.getInterestRate().equals(BigDecimal.valueOf(5.5)) &&
          l.getAutomaticValidation().equals(true)
      )
      .verifyComplete();

    verify(loanTypeRepository, times(1)).findById(1L);
  }
}
