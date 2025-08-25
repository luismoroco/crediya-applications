package com.crediya.applications.usecase.application;

import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.application.ApplicationStatus;
import com.crediya.applications.model.application.gateways.ApplicationRepository;
import com.crediya.applications.usecase.application.dto.StartApplicationDTO;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ApplicationUseCase {

  private final ApplicationRepository repository;

  public Mono<Application> startApplication(StartApplicationDTO dto) {
    Application application = new Application();
    application.setAmount(dto.getAmount());
    application.setDeadline(dto.getDeadline());
    application.setEmail(dto.getEmail());
    application.setApplicationStatus(ApplicationStatus.PENDING);
    application.setLoanType(dto.getLoanType());

    return this.repository.save(application);
  }
}
