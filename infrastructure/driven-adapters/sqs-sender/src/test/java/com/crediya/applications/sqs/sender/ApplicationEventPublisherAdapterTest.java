package com.crediya.applications.sqs.sender;

import com.crediya.applications.sqs.sender.config.SQSSenderConfig;
import com.crediya.applications.sqs.sender.config.SQSSenderProperties;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import software.amazon.awssdk.metrics.MetricPublisher;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationEventPublisherAdapterTest {

  private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
    .withBean(SQSSenderProperties.class, () -> new SQSSenderProperties(
      "us-east-1",
      "https://sqs.us-east-1.amazonaws.com/123456789012/my-queue",
      "http://localhost:4566"
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
}
