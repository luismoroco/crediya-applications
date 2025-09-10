package com.crediya.applications.usecase.application;

import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.application.ApplicationStatus;
import com.crediya.applications.model.application.gateways.ApplicationRepository;
import com.crediya.applications.model.application.gateways.dto.AggregatedApplicationDTO;
import com.crediya.applications.model.application.gateways.dto.UserDTO;
import com.crediya.applications.model.loantype.LoanType;
import com.crediya.applications.model.loantype.gateways.LoanTypeRepository;
import com.crediya.applications.usecase.application.dto.GetApplicationsDTO;
import com.crediya.applications.usecase.application.dto.StartApplicationDTO;
import com.crediya.applications.model.application.gateways.AuthClient;
import com.crediya.common.exc.NotFoundException;
import com.crediya.common.validation.exc.ValidationException;
import com.crediya.common.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

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

  @Test
  void testStartApplicationSuccess() {
    StartApplicationDTO dto = createDTO();
    dto.setLoanTypeId(1000L);

    Application app = new Application();
    app.setEmail("root@gmail.com");

    LoanType loanType = LoanType.builder()
      .loanTypeId(1000L)
      .name("Personal Loan")
      .maximumAmount(50000L)
      .minimumAmount(1000L)
      .interestRate(BigDecimal.valueOf(5.5))
      .automaticValidation(true)
      .build();

    UserDTO user = new UserDTO();
    user.setIdentityCardNumber("11223344");
    user.setEmail("root@gmail.com");

    when(loanTypeRepository.findById(dto.getLoanTypeId())).thenReturn(Mono.just(loanType));
    when(authClient.getUserByIdentityCardNumber(dto.getIdentityCardNumber())).thenReturn(Mono.just(user));
    when(repository.save(any(Application.class))).thenReturn(Mono.just(app));

    StepVerifier.create(useCase.startApplication(dto))
      .expectNextMatches(result -> result.getEmail().equals(user.getEmail()))
      .verifyComplete();
  }

  @Test
  void testStartApplicationUserDoesNotExist() {
    StartApplicationDTO dto = createDTO();
    dto.setLoanTypeId(1L);

    Application app = new Application();
    LoanType loanType = LoanType.builder()
      .loanTypeId(1000L)
      .name("Personal Loan")
      .maximumAmount(50000L)
      .minimumAmount(1000L)
      .interestRate(BigDecimal.valueOf(5.5))
      .automaticValidation(true)
      .build();

    UserDTO user = new UserDTO();
    user.setIdentityCardNumber("11223344");

    when(loanTypeRepository.findById(dto.getLoanTypeId())).thenReturn(Mono.just(loanType));
    when(authClient.getUserByIdentityCardNumber(dto.getIdentityCardNumber())).thenReturn(Mono.error(new NotFoundException("")));
    when(repository.save(any(Application.class))).thenReturn(Mono.just(app));

    StepVerifier.create(useCase.startApplication(dto))
      .expectError(NotFoundException.class)
      .verify();
  }

  @Test
  void testStartApplicationLoanTypeDoesNotExist() {
    StartApplicationDTO dto = createDTO();
    dto.setLoanTypeId(1L);

    Application app = new Application();
    LoanType loanType = LoanType.builder()
      .loanTypeId(1000L)
      .name("Personal Loan")
      .maximumAmount(50000L)
      .minimumAmount(1000L)
      .interestRate(BigDecimal.valueOf(5.5))
      .automaticValidation(true)
      .build();

    UserDTO user = new UserDTO();
    user.setIdentityCardNumber("11223344");

    when(loanTypeRepository.findById(dto.getLoanTypeId())).thenReturn(Mono.empty());
    when(authClient.getUserByIdentityCardNumber(dto.getIdentityCardNumber())).thenReturn(Mono.error(new NotFoundException("")));
    when(repository.save(any(Application.class))).thenReturn(Mono.just(app));

    StepVerifier.create(useCase.startApplication(dto))
      .expectError(NotFoundException.class)
      .verify();
  }

  @Test
  void testStartApplicationInvalidDTO() {
    StartApplicationDTO dto = new StartApplicationDTO();
    dto.setAmount(1000L);

    StepVerifier.create(ApplicationUseCase.validateStartApplicationDTOConstraints(dto))
      .expectErrorMatches(throwable -> throwable instanceof ValidationException &&
        throwable.getMessage().contains("invalid"))
      .verify();
  }

  @Test
  void testGetAggregatedApplicationsSuccess() {
    ApplicationStatus desiredStatus = ApplicationStatus.PENDING;

    GetApplicationsDTO dto = new GetApplicationsDTO();
    dto.setPage(1);
    dto.setPageSize(10);
    dto.setApplicationStatuses(List.of(desiredStatus.name()));

    AggregatedApplicationDTO ap = new AggregatedApplicationDTO();
    ap.setApplicationStatusId(ApplicationStatus.PENDING.getCode());
    ap.setEmail("root@gmail.com");

    UserDTO user = new UserDTO();
    user.setEmail("root@gmail.com");

    when(repository.findAggregatedApplications(dto.getApplicationStatuses(), dto.getPage(), dto.getPageSize())).thenReturn(Flux.just(ap));
    when(authClient.getUsers(List.of(ap.getEmail()))).thenReturn(Flux.just(user));

    StepVerifier.create(useCase.getAggregatedApplications(dto))
      .expectNextMatches(result -> result.getApplicationStatus().equals(desiredStatus))
      .verifyComplete();
  }

  @Test
  void testGetAggregatedApplicationsSuccessWithoutResults() {
    ApplicationStatus desiredStatus = ApplicationStatus.PENDING;

    GetApplicationsDTO dto = new GetApplicationsDTO();
    dto.setPage(1);
    dto.setPageSize(10);
    dto.setApplicationStatuses(List.of(desiredStatus.name()));

    AggregatedApplicationDTO ap = new AggregatedApplicationDTO();
    ap.setApplicationStatusId(ApplicationStatus.PENDING.getCode());
    ap.setEmail("root@gmail.com");

    UserDTO user = new UserDTO();
    user.setEmail("root@gmail.com");

    when(repository.findAggregatedApplications(dto.getApplicationStatuses(), dto.getPage(), dto.getPageSize())).thenReturn(Flux.empty());
    when(authClient.getUsers(List.of(ap.getEmail()))).thenReturn(Flux.just(user));

    StepVerifier.create(useCase.getAggregatedApplications(dto))
      .expectNextCount(0)
      .verifyComplete();
  }

  @Test
  void testGetAggregatedApplicationsRequiredEmailNotFound() {
    ApplicationStatus desiredStatus = ApplicationStatus.PENDING;

    GetApplicationsDTO dto = new GetApplicationsDTO();
    dto.setPage(1);
    dto.setPageSize(10);
    dto.setApplicationStatuses(List.of(desiredStatus.name()));

    AggregatedApplicationDTO ap = new AggregatedApplicationDTO();
    ap.setApplicationStatusId(ApplicationStatus.PENDING.getCode());
    ap.setEmail("root@gmail.com");

    UserDTO user = new UserDTO();
    user.setEmail("root-difer@gmail.com");

    when(repository.findAggregatedApplications(dto.getApplicationStatuses(), dto.getPage(), dto.getPageSize())).thenReturn(Flux.just(ap));
    when(authClient.getUsers(List.of(ap.getEmail()))).thenReturn(Flux.just(user));

    StepVerifier.create(useCase.getAggregatedApplications(dto))
      .expectError(NotFoundException.class)
      .verify();
  }

  @Test
  void testValidateGetApplicationsDTOConstraintsSuccess() {
    GetApplicationsDTO dto = new GetApplicationsDTO();
    dto.setPage(-1);
    dto.setPageSize(10);
    dto.setApplicationStatuses(List.of(ApplicationStatus.PENDING.name()));

    StepVerifier.create(ApplicationUseCase.validateGetApplicationsDTOConstraints(dto))
      .expectErrorMatches(throwable -> throwable instanceof ValidationException &&
        throwable.getMessage().contains("invalid"))
      .verify();
  }
}
