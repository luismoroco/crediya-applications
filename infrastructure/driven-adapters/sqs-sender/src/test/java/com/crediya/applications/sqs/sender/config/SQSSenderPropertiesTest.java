package com.crediya.applications.sqs.sender.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SQSSenderPropertiesTest {

  @Test
  void testRecordConstruction() {
    SQSSenderProperties.SQSConfig notifications = new SQSSenderProperties.SQSConfig("https://sqs.us-east-1.amazonaws.com/123456789012/notifications");
    SQSSenderProperties.SQSConfig riskAnalysis = new SQSSenderProperties.SQSConfig("https://sqs.us-east-1.amazonaws.com/123456789012/risk-analysis");
    SQSSenderProperties.SQSConfig reporting = new SQSSenderProperties.SQSConfig("https://sqs.us-east-1.amazonaws.com/123456789012/reporting");

    SQSSenderProperties properties = new SQSSenderProperties(
      "us-east-1",
      notifications,
      riskAnalysis,
      reporting
    );

    assertThat(properties.region()).isEqualTo("us-east-1");
    assertThat(properties.crediyaNotifications().queueUrl()).isEqualTo("https://sqs.us-east-1.amazonaws.com/123456789012/notifications");
    assertThat(properties.crediyaRiskAnalysis().queueUrl()).isEqualTo("https://sqs.us-east-1.amazonaws.com/123456789012/risk-analysis");
    assertThat(properties.crediyaReporting().queueUrl()).isEqualTo("https://sqs.us-east-1.amazonaws.com/123456789012/reporting");
  }

  @Test
  void testEqualsAndHashCode() {
    SQSSenderProperties.SQSConfig config1 = new SQSSenderProperties.SQSConfig("url1");
    SQSSenderProperties.SQSConfig config2 = new SQSSenderProperties.SQSConfig("url1");
    SQSSenderProperties.SQSConfig config3 = new SQSSenderProperties.SQSConfig("url2");

    SQSSenderProperties prop1 = new SQSSenderProperties("us-east-1", config1, config2, config3);
    SQSSenderProperties prop2 = new SQSSenderProperties("us-east-1", config1, config2, config3);
    SQSSenderProperties prop3 = new SQSSenderProperties("us-east-1", config1, config2, new SQSSenderProperties.SQSConfig("urlX"));

    assertThat(prop1).isEqualTo(prop2);
    assertThat(prop1).isNotEqualTo(prop3);
    assertThat(prop1.hashCode()).isEqualTo(prop2.hashCode());
    assertThat(prop1.hashCode()).isNotEqualTo(prop3.hashCode());
  }

  @Test
  void testToStringContainsValues() {
    SQSSenderProperties properties = new SQSSenderProperties(
      "regionX",
      new SQSSenderProperties.SQSConfig("queueUrlNotifications"),
      new SQSSenderProperties.SQSConfig("queueUrlRisk"),
      new SQSSenderProperties.SQSConfig("queueUrlReporting")
    );

    String str = properties.toString();

    assertThat(str).contains("regionX", "queueUrlNotifications", "queueUrlRisk", "queueUrlReporting");
  }
}
