package com.crediya.applications.usecase.application;

import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.application.ApplicationStatus;
import com.crediya.applications.model.application.gateways.ApplicationRepository;
import com.crediya.applications.model.loantype.gateways.LoanTypeRepository;
import com.crediya.applications.usecase.application.dto.StartApplicationDTO;
import com.crediya.applications.usecase.application.gateway.AuthGateway;
import com.crediya.common.exc.NotFoundException;
import com.crediya.common.logging.Logger;
import com.crediya.common.validation.ValidatorUtils;
import static com.crediya.applications.model.application.Application.Field.*;
import static com.crediya.common.LogCatalog.*;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ApplicationUseCase {

  private final ApplicationRepository repository;
  private final LoanTypeRepository loanTypeRepository;
  private final AuthGateway authGateway;
  private final Logger logger;

  public Mono<Application> startApplication(StartApplicationDTO dto) {
    return validateStartApplication(dto)
      .then(this.loanTypeRepository.findById(dto.getLoanTypeId()))
      .switchIfEmpty(Mono.defer(() -> {
        this.logger.warn(ENTITY_NOT_FOUND.of(LOAN_TYPE_ID, dto.getLoanTypeId()));
        return Mono.error(new NotFoundException(ENTITY_NOT_FOUND.of(LOAN_TYPE_ID, dto.getLoanTypeId())));
      }))
      .flatMap(loanType -> ValidatorUtils.numberBetween(
        AMOUNT, dto.getAmount(), loanType.getMinimumAmount(), loanType.getMaximumAmount())
      )
      .then(this.authGateway.getUserByIdentityCardNumber(dto.getIdentityCardNumber()))
      .flatMap(email -> {
        Application application = new Application();
        application.setAmount(dto.getAmount());
        application.setDeadline(dto.getDeadline());
        application.setEmail(email);
        application.setApplicationStatus(ApplicationStatus.PENDING);
        application.setLoanTypeId(dto.getLoanTypeId());

        return repository.save(application)
          .doOnError(error -> this.logger.error(ERROR_PROCESSING.of("startApplication", application.getEmail()), error));
      });
  }

  public static Mono<Void> validateStartApplication(StartApplicationDTO dto) {
    return ValidatorUtils.nonNull(AMOUNT, dto.getAmount())
      .then(ValidatorUtils.nonNull(DEADLINE, dto.getDeadline()))
      .then(ValidatorUtils.string("IDENTITY CARD NUMBER", dto.getIdentityCardNumber()))
      .then(ValidatorUtils.nonNull(LOAN_TYPE_ID, dto.getLoanTypeId()));
  }
}
