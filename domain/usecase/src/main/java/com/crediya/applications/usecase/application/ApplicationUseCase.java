package com.crediya.applications.usecase.application;

import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.application.ApplicationStatus;
import com.crediya.applications.model.application.gateways.ApplicationRepository;
import com.crediya.applications.usecase.application.dto.StartApplicationDTO;
import com.crediya.applications.usecase.application.gateway.AuthGateway;
import com.crediya.common.ErrorCode;
import com.crediya.common.exc.NotFoundException;
import com.crediya.common.logging.Logger;
import com.crediya.common.validation.ValidatorUtils;
import static com.crediya.applications.model.application.Application.Field.*;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ApplicationUseCase {

  private final ApplicationRepository repository;
  private final AuthGateway authGateway;
  private final Logger logger;

  public Mono<Application> startApplication(StartApplicationDTO dto) {
    logger.info("Starting application with {}", dto);

    return validateStartApplication(dto)
      .then(this.authGateway.userExistsByEmail(dto.getEmail()))
      .flatMap(exists -> {
        if (Boolean.FALSE.equals(exists)) {
          logger.warn(String.format("User not found [email=%s]", dto.getEmail()));
          return Mono.error(
            new NotFoundException(ErrorCode.ENTITY_NOT_FOUND.get(EMAIL.getLabel(), dto.getEmail()))
          );
        }

        Application application = new Application();
        application.setAmount(dto.getAmount());
        application.setDeadline(dto.getDeadline());
        application.setEmail(dto.getEmail());
        application.setApplicationStatus(ApplicationStatus.PENDING);
        application.setLoanType(dto.getLoanType());

        return repository.save(application)
          .doOnSuccess(saved ->
            logger.info(String.format("Application saved successfully [id=%s][email=%s]", saved.getApplicationId(),
              saved.getEmail()))
          )
          .doOnError(error ->
            logger.error(String.format("Error while saving application [email=%s] [error=%s]", dto.getEmail(),
              error.getMessage()), error)
          );
      });
  }

  public static Mono<Void> validateStartApplication(StartApplicationDTO dto) {
    return ValidatorUtils.nonNull(AMOUNT.getLabel(), dto.getAmount())
      .then(ValidatorUtils.nonNull(DEADLINE.getLabel(), dto.getDeadline()))
      .then(ValidatorUtils.email(EMAIL.getLabel(), dto.getEmail()))
      .then(ValidatorUtils.nonNull(LOAN_TYPE.getLabel(), dto.getLoanType()));
  }
}
