package com.crediya.applications.r2dbc;

import com.crediya.applications.model.application.gateways.dto.AggregatedApplication;
import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.application.ApplicationStatus;
import com.crediya.applications.model.application.gateways.ApplicationRepository;
import com.crediya.applications.r2dbc.entity.ApplicationEntity;
import com.crediya.applications.r2dbc.helper.ReactiveAdapterOperations;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public class ApplicationReactiveRepositoryAdapter extends ReactiveAdapterOperations<
  Application,
  ApplicationEntity,
  Long,
  ApplicationReactiveRepository
> implements ApplicationRepository {

  public ApplicationReactiveRepositoryAdapter(ApplicationReactiveRepository repository, ObjectMapper mapper) {
    super(repository, mapper, d -> mapper.map(d, Application.class));
  }

  @Override
  public Flux<AggregatedApplication> findAggregatedApplications(List<String> applicationStatus, Integer page, Integer pageSize) {
    List<Integer> applicationStatusIds = applicationStatus.stream()
      .map(ApplicationStatus::valueOf)
      .map(ApplicationStatus::getCode)
      .toList();

    return this.repository.findAggregatedApplications(applicationStatusIds, pageSize, (page - 1) * pageSize);
  }
}
