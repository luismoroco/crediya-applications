package com.crediya.applications.r2dbc;

import com.crediya.applications.model.application.gateways.dto.AggregatedApplicationDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationReactiveRepositoryTest {

  @Mock
  private ApplicationReactiveRepository repository;

  @Test
  void testRepositoryIsCalled() {
    AggregatedApplicationDTO app = AggregatedApplicationDTO.builder()
      .applicationId(1L)
      .amount(5000L)
      .email("unit@test.com")
      .interestRate(new BigDecimal("0.10"))
      .build();

    when(repository.findAggregatedApplications(List.of(1), 10, 0, List.of()))
      .thenReturn(Flux.just(app));

    StepVerifier.create(repository.findAggregatedApplications(List.of(1), 10, 0, List.of()))
      .expectNextMatches(a -> a.getApplicationId() == 1L && a.getEmail().equals("unit@test.com"))
      .verifyComplete();

    verify(repository, times(1)).findAggregatedApplications(List.of(1), 10, 0, List.of());
  }
}
