package com.crediya.applications.api;

import com.crediya.applications.model.application.ApplicationStatus;
import com.crediya.applications.usecase.application.ApplicationUseCase;
import com.crediya.applications.usecase.application.dto.GetApplicationsDTO;
import com.crediya.applications.usecase.application.dto.StartApplicationDTO;
import com.crediya.common.logging.aspect.AutomaticLogging;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ApplicationUseCase useCase;

    @AutomaticLogging
    public Mono<ServerResponse> startApplication(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(StartApplicationDTO.class)
          .flatMap(this.useCase::startApplication)
          .flatMap(dto -> ServerResponse
            .status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
          );
    }

    @AutomaticLogging
    public Mono<ServerResponse> getApplications(ServerRequest serverRequest) {
        int page = Integer.parseInt(serverRequest.queryParam("page").orElse("0"));
        int size = Integer.parseInt(serverRequest.queryParam("size").orElse("10"));

        GetApplicationsDTO request = GetApplicationsDTO.builder()
          .page(page)
          .pageSize(size)
          .applicationStatuses(List.of(ApplicationStatus.PENDING))
          .build();

        return this.useCase.getAggregatedApplications(request)
          .collectList()
          .flatMap(dto -> ServerResponse
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
          );
    }
}
