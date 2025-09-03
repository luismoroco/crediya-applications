package com.crediya.applications.usecase.application;

import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.application.ApplicationStatus;
import com.crediya.applications.model.application.gateways.dto.AggregatedApplication;
import com.crediya.applications.model.application.gateways.ApplicationRepository;
import com.crediya.applications.model.loantype.gateways.LoanTypeRepository;
import com.crediya.applications.usecase.application.dto.GetApplicationsDTO;
import com.crediya.applications.usecase.application.dto.StartApplicationDTO;
import com.crediya.applications.model.application.gateways.AuthGateway;
import com.crediya.applications.model.application.gateways.dto.UserDTO;
import com.crediya.common.exc.NotFoundException;
import com.crediya.common.logging.Logger;
import com.crediya.common.validation.ValidatorUtils;
import static com.crediya.applications.model.application.Application.Field.*;
import static com.crediya.common.LogCatalog.*;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ApplicationUseCase {

  private static final int MINIMUM_PAGE = 1;
  private static final int MINIMUM_PAGE_SIZE = 3;

  private final ApplicationRepository repository;
  private final LoanTypeRepository loanTypeRepository;
  private final AuthGateway authGateway;
  private final Logger logger;

  public Mono<Application> startApplication(StartApplicationDTO dto) {
    return validateStartApplicationDTOConstraints(dto)
      .then(this.loanTypeRepository.findById(dto.getLoanTypeId()))
      .switchIfEmpty(Mono.defer(() -> {
        this.logger.warn(ENTITY_NOT_FOUND.of(LOAN_TYPE_ID, dto.getLoanTypeId()));
        return Mono.error(new NotFoundException(ENTITY_NOT_FOUND.of(LOAN_TYPE_ID, dto.getLoanTypeId())));
      }))
      .flatMap(loanType -> ValidatorUtils.numberBetween(
        AMOUNT, dto.getAmount(), loanType.getMinimumAmount(), loanType.getMaximumAmount())
      )
      .then(this.authGateway.getUserByIdentityCardNumber(dto.getIdentityCardNumber()))
      .flatMap(userDTO -> {
        Application application = new Application();
        application.setAmount(dto.getAmount());
        application.setDeadline(dto.getDeadline());
        application.setEmail(userDTO.getEmail());
        application.setApplicationStatus(ApplicationStatus.PENDING);
        application.setLoanTypeId(dto.getLoanTypeId());

        return repository.save(application)
          .doOnError(error -> this.logger.error(ERROR_PROCESSING.of("startApplication", application.getEmail()), error));
      });
  }

  public Flux<AggregatedApplication> getAggregatedApplications(GetApplicationsDTO dto) {
    return validateGetApplicationsDTOConstraints(dto)
      .thenMany(this.repository.findAggregatedApplications(dto.getApplicationStatuses(), dto.getPage(), dto.getPageSize()))
      .switchIfEmpty(Flux.defer(() -> {
        this.logger.warn(ENTITY_NOT_FOUND.of("AggregatedApplications", dto));
        return Flux.empty();
      }))
      .collectList()
      .flatMapMany(dtos -> {
        List<String> userEmails = dtos.stream().map(AggregatedApplication::getEmail).toList();

        return this.authGateway.getUsers(userEmails)
          .collectMap(UserDTO::getEmail)
          .flatMapMany(userMap -> Flux.fromIterable(dtos)
            .map(app -> {
              UserDTO user = Optional.ofNullable(userMap.get(app.getEmail()))
                .orElseThrow(() -> new NotFoundException(ENTITY_NOT_FOUND.of(app.getEmail())));

              return app.update(user);
            }));
      })
      .doOnError(error -> this.logger.error(ERROR_PROCESSING.of("getAggregatedApplications", dto), error));
  }

  public static Mono<Void> validateGetApplicationsDTOConstraints(GetApplicationsDTO dto) {
    return ValidatorUtils.enumsValueOf("APPLICATION_STATUSES", dto.getApplicationStatuses(), ApplicationStatus.class) // TODO: this does not work
      .then(ValidatorUtils.greaterOrEqualThan("PAGE", dto.getPage(), MINIMUM_PAGE))
      .then(ValidatorUtils.greaterOrEqualThan("PAGE_SIZE", dto.getPageSize(), MINIMUM_PAGE_SIZE));
  }

  public static Mono<Void> validateStartApplicationDTOConstraints(StartApplicationDTO dto) {
    return ValidatorUtils.nonNull(AMOUNT, dto.getAmount())
      .then(ValidatorUtils.nonNull(DEADLINE, dto.getDeadline()))
      .then(ValidatorUtils.string("IDENTITY CARD NUMBER", dto.getIdentityCardNumber()))
      .then(ValidatorUtils.nonNull(LOAN_TYPE_ID, dto.getLoanTypeId()));
  }
}
