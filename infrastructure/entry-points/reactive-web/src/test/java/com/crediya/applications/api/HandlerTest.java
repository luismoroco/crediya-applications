package com.crediya.applications.api;

import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.application.ApplicationStatus;
import com.crediya.applications.model.application.gateways.dto.AggregatedApplicationDTO;
import com.crediya.applications.usecase.application.ApplicationUseCase;
import com.crediya.applications.usecase.application.dto.GetApplicationsDTO;
import com.crediya.applications.usecase.application.dto.StartApplicationDTO;
import com.crediya.applications.usecase.application.dto.UpdateApplicationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

import java.util.List;

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
      .identityCardNumber("12345678")
      .build();

    Application application = new Application();
    application.setEmail("john@example.com");

    when(serverRequest.bodyToMono(StartApplicationDTO.class)).thenReturn(Mono.just(dto));
    when(useCase.startApplication(any(StartApplicationDTO.class))).thenReturn(Mono.just(application));

    Mono<ServerResponse> responseMono = handler.startApplication(serverRequest)
      .contextWrite(Context.of("identityCardNumber", "12345678"));

    StepVerifier.create(responseMono)
      .assertNext(response -> {
        assert response.statusCode().equals(HttpStatus.CREATED);
        assert response.headers().getContentType().equals(MediaType.APPLICATION_JSON);
      })
      .verifyComplete();
  }

  @Test
  void testGetApplications() {
    GetApplicationsDTO dto = GetApplicationsDTO.builder()
      .page(1)
      .pageSize(10)
      .applicationStatuses(List.of(ApplicationStatus.PENDING.name()))
      .build();

    AggregatedApplicationDTO application = new AggregatedApplicationDTO();
    application.setStatus(ApplicationStatus.PENDING);

    when(useCase.getAggregatedApplications(any(GetApplicationsDTO.class))).thenReturn(Flux.just(application));

    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    queryParams.add("page", "1");
    queryParams.add("page_Size", "10");
    queryParams.add("application_statuses", ApplicationStatus.PENDING.name());

    when(serverRequest.queryParams()).thenReturn(queryParams);

    Mono<ServerResponse> responseMono = handler.getApplications(serverRequest);

    StepVerifier.create(responseMono)
      .assertNext(response -> {
        assert response.statusCode().equals(HttpStatus.OK);
        assert response.headers().getContentType().equals(MediaType.APPLICATION_JSON);
      })
      .verifyComplete();
  }

  @Test
  void testUpdateApplication() {
    UpdateApplicationDTO dto = new UpdateApplicationDTO();
    dto.setApplicationId(1L);
    dto.setApplicationStatus(ApplicationStatus.PENDING);

    Application application = new Application();
    application.setApplicationId(1L);

    when(serverRequest.bodyToMono(UpdateApplicationDTO.class)).thenReturn(Mono.just(dto));
    when(serverRequest.pathVariable("application_id")).thenReturn("1");
    when(useCase.updateApplication(any(UpdateApplicationDTO.class))).thenReturn(Mono.just(application));

    Mono<ServerResponse> responseMono = handler.updateApplication(serverRequest);

    StepVerifier.create(responseMono)
      .assertNext(response -> {
        assert response.statusCode().equals(HttpStatus.OK);
        assert response.headers().getContentType().equals(MediaType.APPLICATION_JSON);
      })
      .verifyComplete();
  }
}
