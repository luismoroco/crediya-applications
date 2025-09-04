package com.crediya.applications.usecase.application;

import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.application.gateways.ApplicationRepository;
import com.crediya.applications.model.application.gateways.dto.UserDTO;
import com.crediya.applications.model.loantype.LoanType;
import com.crediya.applications.model.loantype.gateways.LoanTypeRepository;
import com.crediya.applications.usecase.application.dto.StartApplicationDTO;
import com.crediya.applications.model.application.gateways.AuthClient;
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
  private AuthClient authClient;

  @Mock
  private Logger logger;

  @Mock
  private LoanTypeRepository loanTypeRepository;

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
    dto.setIdentityCardNumber("11223344");
    return dto;
  }

//  @Test
//  void testStartApplication() {
//    StartApplicationDTO dto = createDTO();
//
//    Application app = new Application();
//
//    UserDTO user = new UserDTO();
//    user.setIdentityCardNumber("11223344");
//
//    when(authClient.getUserByIdentityCardNumber(dto.getIdentityCardNumber())).thenReturn(Mono.just(user));
//    when(repository.save(any(Application.class))).thenReturn(Mono.just(app));
//
//    StepVerifier.create(useCase.startApplication(dto))
//      .expectNextMatches(result -> result.getEmail().equals(user.getEmail()))
//      .verifyComplete();
//  }

//  @Test
//  void testStartApplicationUserDoesNotExist() {
//    StartApplicationDTO dto = createDTO();
//    dto.setLoanTypeId(1L);
//
//    Application app = new Application();
//    LoanType loanType = new LoanType();
//
//    UserDTO user = new UserDTO();
//    user.setIdentityCardNumber("11223344");
//
//    when(loanTypeRepository.findById(dto.getLoanTypeId())).thenReturn(Mono.just(loanType));
//    when(authClient.getUserByIdentityCardNumber(dto.getIdentityCardNumber())).thenReturn(Mono.error(new NotFoundException("")));
//
//    StepVerifier.create(useCase.startApplication(dto))
//      .expectError(NotFoundException.class)
//      .verify();
//  }

  @Test
  void testStartApplicationInvalidDTO() {
    StartApplicationDTO dto = new StartApplicationDTO();
    dto.setAmount(1000L);

    StepVerifier.create(ApplicationUseCase.validateStartApplicationDTOConstraints(dto))
      .expectErrorMatches(throwable -> throwable instanceof ValidationException &&
        throwable.getMessage().contains("invalid"))
      .verify();
  }
}
