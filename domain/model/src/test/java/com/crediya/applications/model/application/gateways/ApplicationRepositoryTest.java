package com.crediya.applications.model.application.gateways;

import com.crediya.applications.model.application.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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
    application.setLoanTypeId(2);

    when(applicationRepository.save(any(Application.class))).thenReturn(Mono.just(application));

    StepVerifier.create(applicationRepository.save(application))
      .expectNextMatches(app ->
        app.getApplicationId().equals(1L) &&
          app.getAmount().equals(5000L) &&
          app.getDeadline().equals(12) &&
          app.getEmail().equals("test@example.com") &&
          app.getApplicationStatusId().equals(1) &&
          app.getLoanTypeId().equals(2)
      )
      .verifyComplete();

    verify(applicationRepository, times(1)).save(application);
  }
}
