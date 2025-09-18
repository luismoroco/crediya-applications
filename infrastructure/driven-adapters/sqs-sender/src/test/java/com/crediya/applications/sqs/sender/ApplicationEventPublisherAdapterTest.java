package com.crediya.applications.sqs.sender;

import com.crediya.applications.model.application.gateways.event.ApplicationApprovedEvent;
import com.crediya.applications.model.application.gateways.event.ApplicationUpdatedEvent;
import com.crediya.applications.model.application.gateways.event.AutomaticEvaluationLoanRequestStartedEvent;
import com.crediya.applications.sqs.sender.config.SQSSenderConfig;
import com.crediya.applications.sqs.sender.config.SQSSenderProperties;
import com.crediya.common.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.metrics.MetricPublisher;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ApplicationEventPublisherAdapterTest {

  private SQSSenderProperties properties;
  private SqsAsyncClient client;
  private ObjectMapper mapper;
  private Logger logger;

  private ApplicationEventPublisherAdapter publisher;

  private SQSSenderProperties.SQSConfig notificationsConfig;
  private SQSSenderProperties.SQSConfig riskConfig;
  private SQSSenderProperties.SQSConfig reportingConfig;

  @BeforeEach
  void setUp() {
    properties = mock(SQSSenderProperties.class);
    client = mock(SqsAsyncClient.class);
    mapper = new ObjectMapper();
    logger = mock(Logger.class);

    notificationsConfig = mock(SQSSenderProperties.SQSConfig.class);
    riskConfig = mock(SQSSenderProperties.SQSConfig.class);
    reportingConfig = mock(SQSSenderProperties.SQSConfig.class);

    when(properties.crediyaNotifications()).thenReturn(notificationsConfig);
    when(properties.crediyaRiskAnalysis()).thenReturn(riskConfig);
    when(properties.crediyaReporting()).thenReturn(reportingConfig);

    when(notificationsConfig.queueUrl()).thenReturn("notificationsQueue");
    when(riskConfig.queueUrl()).thenReturn("riskQueue");
    when(reportingConfig.queueUrl()).thenReturn("reportingQueue");

    publisher = new ApplicationEventPublisherAdapter(properties, client, mapper, logger);
  }

  private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
    .withBean(SQSSenderProperties.class, () -> new SQSSenderProperties(
      "us-east-1",
      new SQSSenderProperties.SQSConfig("https://sqs.us-east-1.amazonaws.com/123456789012/crediya-notifications"),
      new SQSSenderProperties.SQSConfig("https://sqs.us-east-1.amazonaws.com/123456789012/crediya-risk-analysis"),
      new SQSSenderProperties.SQSConfig("https://sqs.us-east-1.amazonaws.com/123456789012/crediya-reporting")
    ))
    .withBean(MetricPublisher.class, Mockito::mock)
    .withUserConfiguration(SQSSenderConfig.class);

  @Test
  void testSqsAsyncClientBeanCreated() {
    contextRunner.run(context -> {
      assertThat(context).hasSingleBean(SqsAsyncClient.class);

      SqsAsyncClient client = context.getBean(SqsAsyncClient.class);
      assertThat(client).isNotNull();
    });
  }

  @Test
  void testSqsAsyncClientUsesRegionFromProperties() {
    contextRunner.run(context -> {
      SqsAsyncClient client = context.getBean(SqsAsyncClient.class);
      assertThat(client).isNotNull();
    });
  }

  @Test
  void testMetricPublisherInjected() {
    contextRunner.run(context -> {
      MetricPublisher publisher = context.getBean(MetricPublisher.class);
      assertThat(publisher).isNotNull();

      SqsAsyncClient client = context.getBean(SqsAsyncClient.class);
      assertThat(client).isNotNull();
    });
  }

  @Test
  void testNotifyAutomaticEvaluationLoanRequestStarted_sendsMessage() {
    AutomaticEvaluationLoanRequestStartedEvent event = new AutomaticEvaluationLoanRequestStartedEvent();
    SendMessageResponse response = SendMessageResponse.builder().messageId("456").build();
    when(client.sendMessage(any(SendMessageRequest.class)))
      .thenReturn(CompletableFuture.completedFuture(response));

    StepVerifier.create(publisher.notifyAutomaticEvaluationLoanRequestStarted(event))
      .expectNext("456")
      .verifyComplete();
  }

  @Test
  void testNotifyApplicationUpdated_sendsMessage() {
    ApplicationUpdatedEvent event = new ApplicationUpdatedEvent();
    SendMessageResponse response = SendMessageResponse.builder().messageId("456").build();
    when(client.sendMessage(any(SendMessageRequest.class)))
      .thenReturn(CompletableFuture.completedFuture(response));

    StepVerifier.create(publisher.notifyApplicationUpdated(event))
      .expectNext("456")
      .verifyComplete();
  }

  @Test
  void testNotifyApplicationApproved_sendsMessage() {
    ApplicationApprovedEvent event = new ApplicationApprovedEvent();
    SendMessageResponse response = SendMessageResponse.builder().messageId("789").build();
    when(client.sendMessage(any(SendMessageRequest.class)))
      .thenReturn(CompletableFuture.completedFuture(response));

    StepVerifier.create(publisher.notifyApplicationApproved(event))
      .expectNext("789")
      .verifyComplete();
  }
}
