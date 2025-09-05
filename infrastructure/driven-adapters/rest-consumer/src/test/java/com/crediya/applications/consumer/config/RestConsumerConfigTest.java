package com.crediya.applications.consumer.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestConsumerConfigTest {

  @Mock
  private RestConsumerProperties properties;

  @Mock
  private RestConsumerProperties.CrediyaAuthConfig authConfig;

  @Mock
  private WebClient.Builder webClientBuilder;

  private RestConsumerConfig config;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    when(properties.getCrediyaAuth()).thenReturn(authConfig);
    when(authConfig.getUrl()).thenReturn("http://localhost:8080");
    when(authConfig.getTimeout()).thenReturn(5000);

    when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
    when(webClientBuilder.defaultHeader(anyString(), anyString())).thenReturn(webClientBuilder);
    when(webClientBuilder.clientConnector(any(ClientHttpConnector.class))).thenReturn(webClientBuilder);
    when(webClientBuilder.build()).thenReturn(WebClient.builder().build());

    config = new RestConsumerConfig(properties);
  }

  @Test
  void testAuthWebClient() {
    WebClient client = config.authWebClient(webClientBuilder);

    assertNotNull(client);

    verify(webClientBuilder).baseUrl("http://localhost:8080");
    verify(webClientBuilder).defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json");
    verify(webClientBuilder).clientConnector(any(ClientHttpConnector.class));
    verify(webClientBuilder).build();
  }
}
