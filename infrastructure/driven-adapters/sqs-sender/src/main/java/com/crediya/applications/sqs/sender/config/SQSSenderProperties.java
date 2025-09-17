package com.crediya.applications.sqs.sender.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapter.sqs")
public record SQSSenderProperties(
  String region,
  SQSConfig crediyaNotifications,
  SQSConfig crediyaRiskAnalysis,
  SQSConfig crediyaReporting) {

  public record SQSConfig(
    String queueUrl) {
  }
}
