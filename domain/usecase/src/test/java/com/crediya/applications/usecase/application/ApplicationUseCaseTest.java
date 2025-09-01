package com.crediya.applications.usecase.application;

import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.application.gateways.ApplicationRepository;
import com.crediya.applications.model.loantype.LoanTypeEnum;
import com.crediya.applications.usecase.application.dto.StartApplicationDTO;
import com.crediya.applications.usecase.application.gateway.AuthGateway;
import com.crediya.common.exc.NotFoundException;
import com.crediya.common.exc.ValidationException;
import com.crediya.common.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ApplicationUseCaseTest {

  @Mock
  private ApplicationRepository repository;

  @Mock
  private AuthGateway authGateway;

  @Mock
  private Logger logger;

  @InjectMocks
  private ApplicationUseCase useCase;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private StartApplicationDTO createDTO() {
    StartApplicationDTO dto = new StartApplicationDTO();
    dto.setAmount(1000L);
    dto.setDeadline(12);
    dto.setEmail("test@example.com");
    dto.setLoanType(LoanTypeEnum.PERSONAL_LOAN);
    return dto;
  }

  @Test
  void testStartApplication() {
    StartApplicationDTO dto = createDTO();
    Application app = new Application();
    app.setEmail(dto.getEmail());

    when(authGateway.getUserByIdentityCardNumber(dto.getEmail())).thenReturn(Mono.just(true));
    when(repository.save(any(Application.class))).thenReturn(Mono.just(app));

    StepVerifier.create(useCase.startApplication(dto))
      .expectNextMatches(result -> result.getEmail().equals(dto.getEmail()))
      .verifyComplete();

    verify(authGateway).getUserByIdentityCardNumber(dto.getEmail());
    verify(repository).save(any(Application.class));
  }

  @Test
  void testStartApplicationUserDoesNotExist() {
    StartApplicationDTO dto = createDTO();

    when(authGateway.getUserByIdentityCardNumber(dto.getEmail())).thenReturn(Mono.just(false));

    StepVerifier.create(useCase.startApplication(dto))
      .expectError(NotFoundException.class)
      .verify();

    verify(authGateway).getUserByIdentityCardNumber(dto.getEmail());
    verify(repository, never()).save(any(Application.class));
  }

  @Test
  void testStartApplicationInvalidDTO() {
    StartApplicationDTO dto = new StartApplicationDTO();
    dto.setAmount(1000L);

    StepVerifier.create(ApplicationUseCase.validateStartApplication(dto))
      .expectErrorMatches(throwable -> throwable instanceof ValidationException &&
        throwable.getMessage().contains("invalid"))
      .verify();
  }
}
