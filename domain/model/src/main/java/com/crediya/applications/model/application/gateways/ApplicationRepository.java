package com.crediya.applications.model.application.gateways;

import com.crediya.applications.model.application.gateways.dto.AggregatedApplication;
import com.crediya.applications.model.application.Application;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ApplicationRepository {
  Mono<Application> save(Application application);

  Flux<AggregatedApplication> findAggregatedApplications(List<String> applicationStatus, Integer page, Integer pageSize);

  Mono<Application> findById(Long applicationId);
}
