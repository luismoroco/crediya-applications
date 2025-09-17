package com.crediya.applications.sqs.sender;

import com.crediya.applications.model.application.gateways.ApplicationEventPublisher;
import com.crediya.applications.model.application.gateways.event.ApplicationApprovedEvent;
import com.crediya.applications.model.application.gateways.event.ApplicationUpdatedEvent;
import com.crediya.applications.model.application.gateways.event.AutomaticEvaluationLoanRequestStartedEvent;
import com.crediya.applications.sqs.sender.config.SQSSenderProperties;
import com.crediya.common.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
@RequiredArgsConstructor
public class ApplicationEventPublisherAdapter implements ApplicationEventPublisher {

    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private final ObjectMapper mapper;
    private final Logger logger;

    @Override
    public Mono<String> notifyApplicationUpdated(ApplicationUpdatedEvent event) {
        return this.send(event, this.properties.crediyaNotifications());
    }

    @Override
    public Mono<String> notifyAutomaticEvaluationLoanRequestStarted(AutomaticEvaluationLoanRequestStartedEvent event) {
        return this.send(event, this.properties.crediyaRiskAnalysis());
    }

    @Override
    public Mono<String> notifyApplicationApproved(ApplicationApprovedEvent event) {
        return this.send(event, this.properties.crediyaReporting());
    }

    private Mono<String> send(Object object, SQSSenderProperties.SQSConfig config) {
        return Mono.fromCallable(() -> {
            this.logger.info("Sending event [args={}]", object);
            return SendMessageRequest.builder()
                .queueUrl(config.queueUrl())
                .messageBody(this.mapper.writeValueAsString(object))
                .build();
           })
          .flatMap(request -> Mono.fromFuture(this.client.sendMessage(request)))
          .doOnNext(response -> this.logger.info("Event sent [messageId={}][queueUrl={}]", response.messageId(), config.queueUrl()))
          .map(SendMessageResponse::messageId);
    }
}
