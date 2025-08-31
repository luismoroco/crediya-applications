package com.crediya.applications.usecase.application;

import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.application.ApplicationStatus;
import com.crediya.applications.model.application.gateways.ApplicationRepository;
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
  private final AuthGateway authGateway;
  private final Logger logger;

  public Mono<Application> startApplication(StartApplicationDTO dto) {
    return validateStartApplication(dto)
      .then(this.authGateway.userExistsByEmail(dto.getEmail()))
      .flatMap(exists -> {
        if (Boolean.FALSE.equals(exists)) {
          this.logger.error(ENTITY_NOT_FOUND.of(EMAIL, dto.getEmail()));
          return Mono.error(
            new NotFoundException(ENTITY_NOT_FOUND.of(EMAIL, dto.getEmail()))
          );
        }

        Application application = new Application();
        application.setAmount(dto.getAmount());
        application.setDeadline(dto.getDeadline());
        application.setEmail(dto.getEmail());
        application.setApplicationStatus(ApplicationStatus.PENDING);
        application.setLoanType(dto.getLoanType());

        return repository.save(application)
          .doOnError(error -> this.logger.error(ERROR_PROCESSING.of("startApplication", application.getEmail()), error));
      });
  }

  public static Mono<Void> validateStartApplication(StartApplicationDTO dto) {
    return ValidatorUtils.nonNull(AMOUNT, dto.getAmount())
      .then(ValidatorUtils.nonNull(DEADLINE, dto.getDeadline()))
      .then(ValidatorUtils.email(EMAIL, dto.getEmail()))
      .then(ValidatorUtils.nonNull(LOAN_TYPE, dto.getLoanType()));
  }
}
