package com.crediya.applications.sqs.sender;

import com.crediya.applications.model.application.gateways.ApplicationEventPublisher;
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
        return this.send(event);
    }

    @Override
    public Mono<String> notifyAutomaticEvaluationLoanRequestStarted(AutomaticEvaluationLoanRequestStartedEvent event) {
        return Mono.just("SENT");
    }

    private Mono<String> send(Object object) {
        return Mono.fromCallable(() -> {
            this.logger.info("Sending event [args={}]", object);
            return SendMessageRequest.builder()
                .queueUrl(this.properties.queueUrl())
                .messageBody(this.mapper.writeValueAsString(object))
                .build();
           })
          .flatMap(request -> Mono.fromFuture(this.client.sendMessage(request)))
          .doOnNext(response -> this.logger.info("Event sent [messageId={}]", response.messageId()))
          .map(SendMessageResponse::messageId);
    }
}
