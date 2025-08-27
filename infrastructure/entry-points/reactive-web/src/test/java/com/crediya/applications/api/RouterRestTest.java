package com.crediya.applications.api;

import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.loantype.LoanTypeEnum;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RouterRestTest {

    private ApplicationUseCase useCase;
    private WebTestClient webTestClient;

    private static Application createApplication() {
      return new Application(1L, 10000L, 1000, "john.doe@gmail.com", 1, 1);
    }

    private static StartApplicationDTO createStartApplicationDTO() {
      return StartApplicationDTO.builder()
        .amount(10000L)
        .deadline(1000)
        .email("john.doe@gmail.com")
        .loanType(LoanTypeEnum.PERSONAL_LOAN)
        .identityCardNumber("123123123")
        .build();
    }

    private static StartApplicationDTO createInvalidStartApplicationDTO() {
      return StartApplicationDTO.builder()
        .amount(10000L)
        .deadline(1000)
        .email("john.doeerteggmail.com")
        .loanType(LoanTypeEnum.PERSONAL_LOAN)
        .identityCardNumber("123123123")
        .build();

    }

    @BeforeEach
    void setUp() {
      useCase = mock(ApplicationUseCase.class);
      Handler handler = new Handler(useCase);
      RouterFunction<?> routes = new RouterRest(handler, new GlobalExceptionFilter())
        .routerFunction();
      webTestClient = WebTestClient.bindToRouterFunction(routes)
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
        .bodyValue(createStartApplicationDTO())
        .exchange()
        .expectStatus().is4xxClientError();
    }
}
