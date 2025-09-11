package com.crediya.applications.usecase.application;

import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.application.ApplicationStatus;
import com.crediya.applications.model.application.gateways.ApplicationEventPublisher;
import com.crediya.applications.model.application.gateways.dto.AggregatedApplicationDTO;
import com.crediya.applications.model.application.gateways.ApplicationRepository;
import com.crediya.applications.model.application.gateways.event.ApplicationUpdatedEvent;
import com.crediya.applications.model.application.gateways.event.AutomaticEvaluationLoanRequestStartedEvent;
import com.crediya.applications.model.loantype.LoanType;
import com.crediya.applications.model.loantype.gateways.LoanTypeRepository;
import com.crediya.applications.usecase.application.dto.GetApplicationsDTO;
import com.crediya.applications.usecase.application.dto.StartApplicationDTO;
import com.crediya.applications.model.application.gateways.AuthClient;
import com.crediya.applications.model.application.gateways.dto.UserDTO;
import com.crediya.applications.usecase.application.dto.UpdateApplicationDTO;
import com.crediya.common.exc.NotFoundException;
import com.crediya.common.logging.Logger;
import com.crediya.common.transaction.Transaction;
import com.crediya.common.validation.ValidatorUtils;
import static com.crediya.applications.model.application.Application.Field.*;
import static com.crediya.common.LogCatalog.*;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ApplicationUseCase {

  public static final int MINIMUM_PAGE = 1;
  public static final int MINIMUM_PAGE_SIZE = 3;
  public static final String PAGE = "page";
  public static final String PAGE_SIZE = "page_size";
  private static final String IDENTITY_CARD_NUMBER = "identityCardNumber";
  public static final String APPLICATION_STATUSES = "application_statuses";
  public static final String APPLICATION_ID = "application_id";
  public static final String EMAILS = "emails";

  private final ApplicationRepository repository;
  private final LoanTypeRepository loanTypeRepository;
  private final ApplicationEventPublisher eventPublisher;
  private final Transaction transaction;
  private final AuthClient authClient;
  private final Logger logger;

  public Mono<Application> startApplication(StartApplicationDTO dto) {
    return validateStartApplicationDTOConstraints(dto)
      .then(this.loanTypeRepository.findById(dto.getLoanTypeId()))
      .switchIfEmpty(Mono.defer(() -> {
        this.logger.error("Loan type not found [loanTypeId={}]", dto.getLoanTypeId());
        return Mono.error(new NotFoundException(ENTITY_NOT_FOUND.of(LOAN_TYPE_ID, dto.getLoanTypeId())));
      }))
      .flatMap(loanType -> ValidatorUtils.numberBetween(AMOUNT, dto.getAmount(), loanType.getMinimumAmount(), loanType.getMaximumAmount())
        .then(this.authClient.getUserByIdentityCardNumber(dto.getIdentityCardNumber()))
        .map(userDTO -> Tuples.of(userDTO, loanType))
      )
      .flatMap(tuple -> {
        UserDTO userDTO = tuple.getT1();
        LoanType loanType = tuple.getT2();

        Application application = new Application();
        application.setAmount(dto.getAmount());
        application.setDeadline(dto.getDeadline());
        application.setEmail(userDTO.getEmail());
        application.setApplicationStatus(ApplicationStatus.PENDING);
        application.setLoanTypeId(loanType.getLoanTypeId());

        return repository.save(application)
          .map(ap -> Tuples.of(ap, userDTO,  loanType));
      })
      .flatMap(tuple -> {
        Application application = tuple.getT1();
        UserDTO userDTO = tuple.getT2();
        LoanType loanType = tuple.getT3();

        if (Boolean.FALSE.equals(loanType.getAutomaticValidation())) {
          return Mono.just(application);
        }

        return this.repository.findMinimalLoans(
            List.of(ApplicationStatus.APPROVED),
            List.of(userDTO.getEmail())
          )
          .collectList()
          .flatMap(minimalLoanDTOS ->
            this.eventPublisher.notifyAutomaticEvaluationLoanRequestStarted(
              AutomaticEvaluationLoanRequestStartedEvent.builder()
                .application(application)
                .loanType(loanType)
                .minimalLoanDTOS(minimalLoanDTOS)
                .build()
            )
            .thenReturn(application)
          );
      })
      .doOnError(error -> this.logger.error("Error starting application [args={}][error={}]", dto, error.getMessage()));
  }

  public Flux<AggregatedApplicationDTO> getAggregatedApplications(GetApplicationsDTO dto) {
    return validateGetApplicationsDTOConstraints(dto)
      .thenMany(this.repository.findAggregatedApplications(dto.getApplicationStatuses(), dto.getPage(), dto.getPageSize(), dto.getEmails()))
      .collectList()
      .flatMapMany(dtos -> {
        if (dtos.isEmpty()) {
          this.logger.warn("Entities not found [args={}]", dto);
          return Flux.empty();
        }

        List<String> userEmails = dtos.stream().map(AggregatedApplicationDTO::getEmail).toList();

        return this.authClient.getUsers(userEmails)
          .collectMap(UserDTO::getEmail)
          .flatMapMany(userMap -> Flux.fromIterable(dtos)
            .map(app -> {
              UserDTO user = Optional.ofNullable(userMap.get(app.getEmail()))
                .orElseThrow(() -> {
                  this.logger.error("User not found [email={}]", app.getEmail());
                  return new NotFoundException(ENTITY_NOT_FOUND.of(EMAIL, app.getEmail()));
                });

              return app.update(user);
            }));
      })
      .doOnError(error -> this.logger.error("Error getting aggregated applications [args={}][error={}]", dto, error.getMessage()));
  }

  public Mono<Application> updateApplication(UpdateApplicationDTO dto) {
    return this.transaction.init(
      validateUpdateApplicationDTOConstraints(dto)
        .then(this.repository.findById(dto.getApplicationId()))
        .switchIfEmpty(Mono.defer(() -> {
          this.logger.error("Application not found [applicationId={}]", dto.getApplicationId());
          return Mono.error(new NotFoundException(ENTITY_NOT_FOUND.of(APPLICATION_ID, dto.getApplicationId())));
        }))
        .flatMap(application -> {
          this.logger.info("Updating application [applicationId={}][targetApplicationStatus={}]", dto.getApplicationId(), dto.getApplicationStatus());
          application.setApplicationStatus(dto.getApplicationStatus());

          return repository.save(application);
        })
        .flatMap(application -> {
          if (!List.of(ApplicationStatus.APPROVED, ApplicationStatus.REJECTED).contains(application.getApplicationStatus())) {
            this.logger.info("Skipping status changing notification [applicationId={}][targetApplicationStatus={}]", application.getApplicationId(), dto.getApplicationStatus());
            return Mono.just(application);
          }

          return this.eventPublisher.notifyApplicationUpdated(ApplicationUpdatedEvent.from(application))
            .thenReturn(application);
        })
    ).doOnError(error -> this.logger.error("Error updating application [args={}][error={}]", dto, error.getMessage()));
  }

  public static Mono<Void> validateUpdateApplicationDTOConstraints(UpdateApplicationDTO dto) {
    return ValidatorUtils.nonNull(APPLICATION_ID, dto.getApplicationId())
      .then(ValidatorUtils.nonNull(APPLICATION_STATUS, dto.getApplicationStatus()));
  }

  public static Mono<Void> validateGetApplicationsDTOConstraints(GetApplicationsDTO dto) {
    return ValidatorUtils.enumsValueOf(APPLICATION_STATUSES, dto.getApplicationStatuses(), ApplicationStatus.class)
      .then(ValidatorUtils.greaterOrEqualThan(PAGE, dto.getPage(), MINIMUM_PAGE))
      .then(ValidatorUtils.greaterOrEqualThan(PAGE_SIZE, dto.getPageSize(), MINIMUM_PAGE_SIZE));
  }

  public static Mono<Void> validateStartApplicationDTOConstraints(StartApplicationDTO dto) {
    return ValidatorUtils.nonNull(AMOUNT, dto.getAmount())
      .then(ValidatorUtils.nonNull(DEADLINE, dto.getDeadline()))
      .then(ValidatorUtils.string(IDENTITY_CARD_NUMBER, dto.getIdentityCardNumber()))
      .then(ValidatorUtils.nonNull(LOAN_TYPE_ID, dto.getLoanTypeId()));
  }
}
