package com.crediya.applications.consumer.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class RestConsumerPropertiesTest {

  private RestConsumerProperties properties;

  @BeforeEach
  void setUp() {
    RestConsumerProperties.CrediyaAuthConfig.AuthPath path = new RestConsumerProperties.CrediyaAuthConfig.AuthPath();
    path.setGetUserByIdentityCardNumber("/user/{id}");
    path.setGetUsers("/users");

    RestConsumerProperties.CrediyaAuthConfig auth = new RestConsumerProperties.CrediyaAuthConfig();
    auth.setUrl("http://localhost:8080");
    auth.setTimeout(5000);
    auth.setPath(path);

    properties = new RestConsumerProperties();
    properties.setCrediyaAuth(auth);
  }

  @Test
  void testPropertiesValues() {
    RestConsumerProperties.CrediyaAuthConfig auth = properties.getCrediyaAuth();
    assertEquals("http://localhost:8080", auth.getUrl());
    assertEquals(5000, auth.getTimeout());

    RestConsumerProperties.CrediyaAuthConfig.AuthPath path = auth.getPath();
    assertEquals("/user/{id}", path.getGetUserByIdentityCardNumber());
    assertEquals("/users", path.getGetUsers());
  }
}
