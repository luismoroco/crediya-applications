package com.crediya.applications.usecase.application;

import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.application.ApplicationStatus;
import com.crediya.applications.model.application.gateways.ApplicationEventPublisher;
import com.crediya.applications.model.application.gateways.ApplicationRepository;
import com.crediya.applications.model.application.gateways.dto.AggregatedApplicationDTO;
import com.crediya.applications.model.application.gateways.dto.UserDTO;
import com.crediya.applications.model.loantype.LoanType;
import com.crediya.applications.model.loantype.gateways.LoanTypeRepository;
import com.crediya.applications.usecase.application.dto.GetApplicationsDTO;
import com.crediya.applications.usecase.application.dto.StartApplicationDTO;
import com.crediya.applications.model.application.gateways.AuthClient;
import com.crediya.applications.usecase.application.dto.UpdateApplicationDTO;
import com.crediya.common.exc.NotFoundException;
import com.crediya.common.transaction.Transaction;
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
  private ApplicationEventPublisher publisher;

  @Mock
  private Transaction transaction;

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
      .automaticValidation(false)
      .build();

    UserDTO user = new UserDTO();
    user.setIdentityCardNumber("11223344");
    user.setEmail("root@gmail.com");

    when(transaction.init(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
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

    when(transaction.init(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
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

    when(transaction.init(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
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

//  @Test
//  void testGetAggregatedApplicationsSuccess() {
//    ApplicationStatus desiredStatus = ApplicationStatus.PENDING;
//
//    GetApplicationsDTO dto = new GetApplicationsDTO();
//    dto.setPage(1);
//    dto.setPageSize(10);
//    dto.setApplicationStatuses(List.of(desiredStatus.name()));
//
//    AggregatedApplicationDTO ap = new AggregatedApplicationDTO();
//    ap.setApplicationStatusId(ApplicationStatus.PENDING.getCode());
//    ap.setEmail("root@gmail.com");
//
//    UserDTO user = new UserDTO();
//    user.setEmail("root@gmail.com");
//
//    when(repository.findAggregatedApplications(dto.getApplicationStatuses(), dto.getPage(), dto.getPageSize(), List.of())).thenReturn(Flux.just(ap));
//    when(authClient.getUsers(List.of(ap.getEmail()))).thenReturn(Flux.just(user));
//
//    StepVerifier.create(useCase.getAggregatedApplications(dto))
//      .expectNextMatches(result -> result.getApplicationStatus().equals(desiredStatus))
//      .verifyComplete();
//  }

  @Test
  void testGetAggregatedApplicationsSuccessWithoutResults() {
    ApplicationStatus desiredStatus = ApplicationStatus.PENDING;

    GetApplicationsDTO dto = new GetApplicationsDTO();
    dto.setPage(1);
    dto.setPageSize(10);
    dto.setApplicationStatuses(List.of(desiredStatus.name()));
    dto.setEmails(List.of());

    AggregatedApplicationDTO ap = new AggregatedApplicationDTO();
    ap.setApplicationStatusId(ApplicationStatus.PENDING.getCode());
    ap.setEmail("root@gmail.com");

    UserDTO user = new UserDTO();
    user.setEmail("root@gmail.com");

    when(repository.findAggregatedApplications(dto.getApplicationStatuses(), dto.getPage(), dto.getPageSize(), List.of())).thenReturn(Flux.empty());
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
    dto.setEmails(List.of());

    AggregatedApplicationDTO ap = new AggregatedApplicationDTO();
    ap.setApplicationStatusId(ApplicationStatus.PENDING.getCode());
    ap.setEmail("root@gmail.com");

    UserDTO user = new UserDTO();
    user.setEmail("root-difer@gmail.com");

    when(repository.findAggregatedApplications(dto.getApplicationStatuses(), dto.getPage(), dto.getPageSize(), List.of())).thenReturn(Flux.just(ap));
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

  @Test
  void testValidateUpdateApplicationDTOConstraintsSuccess() {
    UpdateApplicationDTO dto = new UpdateApplicationDTO();
    dto.setApplicationId(1L);
    dto.setApplicationStatus(ApplicationStatus.PENDING);

    StepVerifier.create(ApplicationUseCase.validateUpdateApplicationDTOConstraints(dto))
      .verifyComplete();
  }

  @Test
  void testUpdateApplicationSuccess() {
    UpdateApplicationDTO dto = UpdateApplicationDTO.builder()
      .applicationId(1L)
      .applicationStatus(ApplicationStatus.PENDING)
      .build();

    Application application = Application.builder()
      .applicationId(1L)
      .applicationStatusId(ApplicationStatus.PENDING.getCode())
      .build();

    when(transaction.init(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(repository.findById(dto.getApplicationId())).thenReturn(Mono.just(application));
    when(repository.save(any(Application.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
    when(publisher.notifyApplicationUpdated(any())).thenReturn(Mono.just("event-sent"));

    StepVerifier.create(useCase.updateApplication(dto))
      .expectNextMatches(app -> app.getApplicationId().equals(1L) &&
        app.getApplicationStatus() == ApplicationStatus.PENDING)
      .verifyComplete();

    verify(repository, times(1)).findById(dto.getApplicationId());
    verify(repository, times(1)).save(any(Application.class));
  }

  @Test
  void testUpdateApplicationNotFound() {
    UpdateApplicationDTO dto = UpdateApplicationDTO.builder()
      .applicationId(99L)
      .applicationStatus(ApplicationStatus.PENDING)
      .build();

    when(transaction.init(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(repository.findById(dto.getApplicationId())).thenReturn(Mono.empty());

    StepVerifier.create(useCase.updateApplication(dto))
      .expectError(NotFoundException.class)
      .verify();

    verify(repository, times(1)).findById(dto.getApplicationId());
    verify(repository, never()).save(any());
    verify(publisher, never()).notifyApplicationUpdated(any());
  }

//  @Test
//  void testUpdateApplicationEventPublisherFails() {
//    UpdateApplicationDTO dto = UpdateApplicationDTO.builder()
//      .applicationId(1L)
//      .applicationStatus(ApplicationStatus.PENDING)
//      .build();
//
//    Application application = Application.builder()
//      .applicationId(1L)
//      .applicationStatusId(ApplicationStatus.PENDING.getCode())
//      .build();
//
//    when(transaction.init(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
//    when(repository.findById(dto.getApplicationId())).thenReturn(Mono.just(application));
//    when(repository.save(any(Application.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
//    when(publisher.notifyApplicationUpdated(any())).thenReturn(Mono.error(new RuntimeException("event failed")));
//
//    StepVerifier.create(useCase.updateApplication(dto))
//      .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
//        throwable.getMessage().equals("event failed"))
//      .verify();
//
//    verify(repository, times(1)).findById(dto.getApplicationId());
//    verify(repository, times(1)).save(any());
//    verify(publisher, times(1)).notifyApplicationUpdated(any());
//  }
}
