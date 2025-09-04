package com.crediya.applications.api;

import com.crediya.applications.model.application.gateways.dto.AggregatedApplication;
import com.crediya.applications.usecase.application.ApplicationUseCase;
import com.crediya.applications.usecase.application.dto.GetApplicationsDTO;
import com.crediya.applications.usecase.application.dto.StartApplicationDTO;
import com.crediya.common.logging.aspect.AutomaticLogging;

import com.crediya.common.pagination.Paginator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('CUSTOMER')")
    public Mono<ServerResponse> startApplication(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(StartApplicationDTO.class)
          .flatMap(startApplicationDto -> Mono.deferContextual(context -> {
              startApplicationDto.setIdentityCardNumber(context.get("identityCardNumber"));
              return Mono.just(startApplicationDto);
          }))
          .flatMap(this.useCase::startApplication)
          .flatMap(dto -> ServerResponse
            .status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
          );
    }

    @AutomaticLogging
    @PreAuthorize("hasRole('ADVISOR')")
    public Mono<ServerResponse> getApplications(ServerRequest serverRequest) {
        int page = Integer.parseInt(serverRequest.queryParam("page").orElse("1"));
        int pageSize = Integer.parseInt(serverRequest.queryParam("page_size").orElse("3"));
        List<String> applicationStatuses = serverRequest.queryParams()
          .getOrDefault("application_statuses", List.of("PENDING"));

        GetApplicationsDTO request = GetApplicationsDTO.builder()
          .page(page)
          .pageSize(pageSize)
          .applicationStatuses(applicationStatuses)
          .build();

        return this.useCase.getAggregatedApplications(request)
          .collectList()
          .flatMap(dto -> ServerResponse
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
              Paginator.<AggregatedApplication>builder()
                .page(page)
                .size(pageSize)
                .content(dto)
                .build()
            )
          );
    }
}
