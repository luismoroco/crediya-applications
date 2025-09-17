package com.crediya.applications.model.application.gateways;

import com.crediya.applications.model.application.gateways.event.ApplicationApprovedEvent;
import com.crediya.applications.model.application.gateways.event.ApplicationUpdatedEvent;
import com.crediya.applications.model.application.gateways.event.AutomaticEvaluationLoanRequestStartedEvent;
import reactor.core.publisher.Mono;

public interface ApplicationEventPublisher {

  Mono<String> notifyApplicationUpdated(ApplicationUpdatedEvent event);

  Mono<String> notifyAutomaticEvaluationLoanRequestStarted(AutomaticEvaluationLoanRequestStartedEvent event);

  Mono<String> notifyApplicationApproved(ApplicationApprovedEvent event);
}
