package com.crediya.applications.model.application.gateways;

import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.application.gateways.dto.AggregatedApplicationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ApplicationRepositoryTest {

  private ApplicationRepository applicationRepository;

  @BeforeEach
  void setUp() {
    applicationRepository = Mockito.mock(ApplicationRepository.class);
  }

  @Test
  void testSaveApplication() {
    Application application = new Application();
    application.setApplicationId(1L);
    application.setAmount(5000L);
    application.setDeadline(12);
    application.setEmail("test@example.com");
    application.setApplicationStatusId(1);
    application.setLoanTypeId(2L);

    when(applicationRepository.save(any(Application.class))).thenReturn(Mono.just(application));

    StepVerifier.create(applicationRepository.save(application))
      .expectNextMatches(app ->
        app.getApplicationId().equals(1L) &&
          app.getAmount().equals(5000L) &&
          app.getDeadline().equals(12) &&
          app.getEmail().equals("test@example.com") &&
          app.getApplicationStatusId().equals(1) &&
          app.getLoanTypeId().equals(2L)
      )
      .verifyComplete();

    verify(applicationRepository, times(1)).save(application);
  }

  @Test
  void testFindAggregatedApplications() {
    List<String> statuses = List.of("PENDING");
    int page = 0;
    int pageSize = 10;

    AggregatedApplicationDTO app = AggregatedApplicationDTO.builder()
      .applicationId(1L)
      .amount(10000L)
      .deadline(24)
      .email("user@example.com")
      .name("Test User")
      .loanTypeId(3)
      .interestRate(BigDecimal.valueOf(5.5))
      .applicationStatusId(1)
      .basicWaging(2000L)
      .totalDebt(BigDecimal.valueOf(5000))
      .build();

    when(applicationRepository.findAggregatedApplications(statuses, page, pageSize, List.of()))
      .thenReturn(Flux.just(app));

    StepVerifier.create(applicationRepository.findAggregatedApplications(statuses, page, pageSize, List.of()))
      .expectNextMatches(a ->
        a.getApplicationId().equals(1L) &&
          a.getAmount().equals(10000L) &&
          a.getDeadline().equals(24) &&
          a.getEmail().equals("user@example.com") &&
          a.getName().equals("Test User") &&
          a.getLoanTypeId().equals(3) &&
          a.getInterestRate().equals(BigDecimal.valueOf(5.5)) &&
          a.getApplicationStatusId().equals(1) &&
          a.getBasicWaging().equals(2000L) &&
          a.getTotalDebt().equals(BigDecimal.valueOf(5000))
      )
      .verifyComplete();

    verify(applicationRepository, times(1))
      .findAggregatedApplications(statuses, page, pageSize, List.of());
  }
}
