package com.crediya.applications.api;

import com.crediya.applications.usecase.application.ApplicationUseCase;
import com.crediya.applications.usecase.application.dto.StartApplicationDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ApplicationUseCase useCase;

    public Mono<ServerResponse> listenPOSTStartApplication(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(StartApplicationDTO.class)
          .flatMap(this.useCase::startApplication)
          .flatMap(dto -> ServerResponse
            .status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
          );
    }
}
