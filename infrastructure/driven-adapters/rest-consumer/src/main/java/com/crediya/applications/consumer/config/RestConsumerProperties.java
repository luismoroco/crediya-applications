package com.crediya.applications.consumer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "adapter.restconsumer")
public class RestConsumerProperties {

  private CrediyaAuthConfig crediyaAuth;

  @Data
  public static class CrediyaAuthConfig {
    private String url;
    private int timeout;
    private AuthPath path;

    @Data
    public static class AuthPath {
      private String getUserByIdentityCardNumber;
      private String getUsers;
    }
  }
}
