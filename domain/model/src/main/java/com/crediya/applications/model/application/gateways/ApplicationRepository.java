package com.crediya.applications.model.application.gateways;

import com.crediya.applications.model.application.AggregatedApplication;
import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.application.ApplicationStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ApplicationRepository {
  Mono<Application> save(Application application);

  Flux<AggregatedApplication> findAggregatedApplications(List<ApplicationStatus> applicationStatus);
}
