package com.crediya.applications.sqs.sender.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SQSSenderPropertiesTest {

  @Test
  void testRecordConstruction() {
    SQSSenderProperties properties = new SQSSenderProperties(
      "us-east-1",
      "https://sqs.us-east-1.amazonaws.com/123456789012/my-queue",
      "http://localhost:4566"
    );

    assertThat(properties.region()).isEqualTo("us-east-1");
    assertThat(properties.queueUrl()).isEqualTo("https://sqs.us-east-1.amazonaws.com/123456789012/my-queue");
    assertThat(properties.endpoint()).isEqualTo("http://localhost:4566");
  }

  @Test
  void testEqualsAndHashCode() {
    SQSSenderProperties prop1 = new SQSSenderProperties("us-east-1", "url1", "endpoint1");
    SQSSenderProperties prop2 = new SQSSenderProperties("us-east-1", "url1", "endpoint1");
    SQSSenderProperties prop3 = new SQSSenderProperties("us-east-1", "url2", "endpoint1");

    assertThat(prop1).isEqualTo(prop2);
    assertThat(prop1).isNotEqualTo(prop3);
    assertThat(prop1.hashCode()).isEqualTo(prop2.hashCode());
    assertThat(prop1.hashCode()).isNotEqualTo(prop3.hashCode());
  }

  @Test
  void testToStringContainsValues() {
    SQSSenderProperties properties = new SQSSenderProperties("regionX", "queueUrlX", "endpointX");

    String str = properties.toString();

    assertThat(str).contains("regionX", "queueUrlX", "endpointX");
  }
}
