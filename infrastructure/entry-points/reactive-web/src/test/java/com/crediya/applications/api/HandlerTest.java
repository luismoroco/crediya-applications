package com.crediya.applications.api;

import com.crediya.applications.model.application.Application;
import com.crediya.applications.usecase.application.ApplicationUseCase;
import com.crediya.applications.usecase.application.dto.StartApplicationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class HandlerTest {

  @InjectMocks
  private Handler handler;

  @Mock
  private ApplicationUseCase useCase;

  @Mock
  private ServerRequest serverRequest;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testStartApplication() {
    StartApplicationDTO dto = StartApplicationDTO.builder()
      .email("john@example.com")
      .build();

    Application application = new Application();
    application.setEmail("john@example.com");

    when(serverRequest.bodyToMono(StartApplicationDTO.class)).thenReturn(Mono.just(dto));
    when(useCase.startApplication(any(StartApplicationDTO.class))).thenReturn(Mono.just(application));

    Mono<ServerResponse> responseMono = handler.startApplication(serverRequest);

    StepVerifier.create(responseMono)
      .assertNext(response -> {
        assert response.statusCode().equals(HttpStatus.CREATED);
        assert response.headers().getContentType().equals(MediaType.APPLICATION_JSON);
      })
      .verifyComplete();
  }
}
