package com.crediya.applications.r2dbc;

import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.application.gateways.ApplicationRepository;
import com.crediya.applications.r2dbc.entity.ApplicationEntity;
import com.crediya.applications.r2dbc.helper.ReactiveAdapterOperations;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

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

    @Transactional(readOnly = true)
    @Override
    public Mono<Application> save(Application application) {
      return super.save(application);
    }

}
