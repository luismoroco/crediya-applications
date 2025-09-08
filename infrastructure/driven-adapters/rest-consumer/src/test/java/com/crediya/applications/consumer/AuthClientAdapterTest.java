package com.crediya.applications.consumer;

import com.crediya.applications.consumer.config.RestConsumerProperties;
import com.crediya.common.exc.NotFoundException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

class AuthClientAdapterTest {

  private static MockWebServer mockBackEnd;
  private static AuthClientAdapter authClientAdapter;

  @BeforeAll
  static void setUp() throws IOException {
    mockBackEnd = new MockWebServer();
    mockBackEnd.start();

    RestConsumerProperties properties = new RestConsumerProperties();
    RestConsumerProperties.CrediyaAuthConfig authConfig = new RestConsumerProperties.CrediyaAuthConfig();
    RestConsumerProperties.CrediyaAuthConfig.AuthPath path = new RestConsumerProperties.CrediyaAuthConfig.AuthPath();

    path.setGetUserByIdentityCardNumber("/users/{identityCardNumber}");
    path.setGetUsers("/users");
    authConfig.setPath(path);
    properties.setCrediyaAuth(authConfig);

    WebClient webClient = WebClient.builder()
      .baseUrl(mockBackEnd.url("/").toString())
      .build();

    authClientAdapter = new AuthClientAdapter(webClient, properties);
  }

  @AfterAll
  static void tearDown() throws IOException {
    mockBackEnd.shutdown();
  }

  @Test
  void getUserByIdentityCardNumber_shouldReturnUser() {
    String identityCard = "12345678";
    String jsonBody = "{ \"userId\": 1, \"firstName\": \"Luis\", \"lastName\": \"Perez\", \"identityCardNumber\": \"" + identityCard + "\" }";

    mockBackEnd.enqueue(new MockResponse()
      .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .setResponseCode(HttpStatus.OK.value())
      .setBody(jsonBody));

    StepVerifier.create(authClientAdapter.getUserByIdentityCardNumber(identityCard))
      .expectNextMatches(user ->
        user.getUserId() == 1L &&
          user.getFirstName().equals("Luis") &&
          user.getIdentityCardNumber().equals(identityCard))
      .verifyComplete();
  }

  @Test
  void getUserByIdentityCardNumber_shouldThrowNotFoundException() {
    String identityCard = "99999999";

    mockBackEnd.enqueue(new MockResponse()
      .setResponseCode(HttpStatus.NOT_FOUND.value()));

    StepVerifier.create(authClientAdapter.getUserByIdentityCardNumber(identityCard))
      .expectError(NotFoundException.class)
      .verify();
  }

  @Test
  @DisplayName("getUsers debe devolver lista de UserDTO cuando el backend responde 200")
  void getUsers_shouldReturnUsers() {
    String jsonBody = "[" +
      "{ \"userId\": 1, \"firstName\": \"Ana\", \"lastName\": \"Lopez\", \"identityCardNumber\": \"11111111\" }," +
      "{ \"userId\": 2, \"firstName\": \"Juan\", \"lastName\": \"Torres\", \"identityCardNumber\": \"22222222\" }" +
      "]";

    mockBackEnd.enqueue(new MockResponse()
      .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .setResponseCode(HttpStatus.OK.value())
      .setBody(jsonBody));

    StepVerifier.create(authClientAdapter.getUsers(List.of("11111111", "22222222")))
      .expectNextMatches(user -> user.getUserId() == 1L && user.getFirstName().equals("Ana"))
      .expectNextMatches(user -> user.getUserId() == 2L && user.getFirstName().equals("Juan"))
      .verifyComplete();
  }
}
