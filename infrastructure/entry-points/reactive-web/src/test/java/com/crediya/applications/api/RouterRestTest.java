package com.crediya.applications.api;

import com.crediya.applications.api.config.ApplicationPath;
import com.crediya.applications.model.application.Application;
import com.crediya.applications.usecase.application.ApplicationUseCase;
import com.crediya.applications.usecase.application.dto.StartApplicationDTO;
import com.crediya.common.api.handling.GlobalExceptionFilter;
import com.crediya.common.exc.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RouterRestTest {

    private ApplicationUseCase useCase;
    private WebTestClient webTestClient;
    private ApplicationPath applicationPath;

    @BeforeEach
    void setUp() {
      useCase = mock(ApplicationUseCase.class);
      Handler handler = new Handler(useCase);
      applicationPath = new ApplicationPath("/api/v1/applications", "/api/v1/applications");
      RouterFunction<?> routes = new RouterRest(handler, new GlobalExceptionFilter(), applicationPath)
        .routerFunction();
      webTestClient = WebTestClient.bindToRouterFunction(routes)
        .build();

      webTestClient = WebTestClient.bindToRouterFunction(routes)
        .webFilter((exchange, chain) -> chain.filter(exchange).contextWrite(
          Context.of("identityCardNumber", "123123123")
        ))
        .build();
    }

    private static Application createApplication() {
      return new Application(1L, 10000L, 1000, "john.doe@gmail.com", 1, 1L);
    }

    private static StartApplicationDTO createStartApplicationDTO() {
      return StartApplicationDTO.builder()
        .amount(10000L)
        .deadline(1000)
        .loanTypeId(1L)
        .identityCardNumber("123123123")
        .build();
    }

    private static StartApplicationDTO createInvalidStartApplicationDTO() {
      return StartApplicationDTO.builder()
        .amount(10000L)
        .deadline(1000)
        .loanTypeId(1L)
        .identityCardNumber("")
        .build();
    }

    @Test
    void mustStartApplication() {
      Application application = createApplication();

      when(useCase.startApplication(org.mockito.ArgumentMatchers.any(StartApplicationDTO.class)))
        .thenReturn(Mono.just(application));

      webTestClient.post()
        .uri("/api/v1/applications")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(createStartApplicationDTO())
        .exchange()
        .expectStatus().isCreated();
    }

    @Test
    void mustNotStartApplication() {
      when(useCase.startApplication(org.mockito.ArgumentMatchers.any(StartApplicationDTO.class)))
        .thenReturn(Mono.error(new NotFoundException("Invalid Body")));

      webTestClient.post()
        .uri("/api/v1/applications")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(createInvalidStartApplicationDTO())
        .exchange()
        .expectStatus().is4xxClientError();
    }
}
