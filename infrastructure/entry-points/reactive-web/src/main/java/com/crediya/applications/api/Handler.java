package com.crediya.applications.api;

import com.crediya.applications.api.dto.StartApplicationServerRequest;
import com.crediya.applications.usecase.application.ApplicationUseCase;
import com.crediya.common.mapping.Mappable;
import com.crediya.common.validation.ObjectValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ApplicationUseCase useCase;

    public Mono<ServerResponse> listenPOSTStartApplication(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(StartApplicationServerRequest.class)
          .flatMap(ObjectValidator.get()::validate)
          .map(Mappable::map)
          .flatMap(this.useCase::startApplication)
          .flatMap(dto -> ServerResponse.status(HttpStatus.CREATED).bodyValue(dto));
    }
}
