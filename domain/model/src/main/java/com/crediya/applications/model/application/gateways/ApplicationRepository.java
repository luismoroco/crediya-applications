package com.crediya.applications.model.application.gateways;

import com.crediya.applications.model.application.Application;
import reactor.core.publisher.Mono;

public interface ApplicationRepository {
  Mono<Application> save(Application application);
}
