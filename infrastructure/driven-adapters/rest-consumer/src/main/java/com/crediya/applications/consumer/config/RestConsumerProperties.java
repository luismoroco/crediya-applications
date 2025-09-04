package com.crediya.applications.consumer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "adapter.restconsumer")
public class RestConsumerProperties {

  private ServiceConfig auth;

  @Data
  public static class ServiceConfig {
    private String url;
    private int timeout;
  }
}
