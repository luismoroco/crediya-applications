package com.crediya.applications.model.application.gateways;

import com.crediya.applications.model.application.gateways.event.ApplicationUpdatedEvent;
import reactor.core.publisher.Mono;

public interface ApplicationEventPublisher {

  Mono<String > notifyApplicationUpdated(ApplicationUpdatedEvent event);
}
