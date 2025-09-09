package com.crediya.applications.api;

import com.crediya.applications.model.application.ApplicationStatus;
import com.crediya.applications.model.application.gateways.dto.AggregatedApplication;
import com.crediya.applications.usecase.application.ApplicationUseCase;
import com.crediya.applications.usecase.application.dto.GetApplicationsDTO;
import com.crediya.applications.usecase.application.dto.StartApplicationDTO;
import com.crediya.applications.usecase.application.dto.UpdateApplicationDTO;
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

import static com.crediya.applications.usecase.application.ApplicationUseCase.PAGE;
import static com.crediya.applications.usecase.application.ApplicationUseCase.PAGE_SIZE;
import static com.crediya.applications.usecase.application.ApplicationUseCase.MINIMUM_PAGE;
import static com.crediya.applications.usecase.application.ApplicationUseCase.MINIMUM_PAGE_SIZE;
import static com.crediya.applications.usecase.application.ApplicationUseCase.APPLICATION_STATUSES;
import static com.crediya.applications.usecase.application.ApplicationUseCase.APPLICATION_ID;
import static com.crediya.applications.api.config.WebContextFilter.IDENTITY_CARD_NUMBER;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ApplicationUseCase useCase;

    @AutomaticLogging
    @PreAuthorize("hasRole('CUSTOMER')")
    public Mono<ServerResponse> startApplication(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(StartApplicationDTO.class)
          .flatMap(startApplicationDto -> Mono.deferContextual(context -> {
              startApplicationDto.setIdentityCardNumber(context.get(IDENTITY_CARD_NUMBER));
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
        int page = Integer.parseInt(serverRequest.queryParam(PAGE).orElse(String.valueOf(MINIMUM_PAGE)));
        int pageSize = Integer.parseInt(serverRequest.queryParam(PAGE_SIZE).orElse(String.valueOf(MINIMUM_PAGE_SIZE)));
        List<String> applicationStatuses = serverRequest.queryParams()
          .getOrDefault(APPLICATION_STATUSES, List.of(ApplicationStatus.PENDING.name()));

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

    @AutomaticLogging
    @PreAuthorize("hasRole('ADVISOR')")
    public Mono<ServerResponse> updateApplication(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UpdateApplicationDTO.class)
          .flatMap(request -> {
            request.setApplicationId(Long.valueOf(serverRequest.pathVariable(APPLICATION_ID)));
            return this.useCase.updateApplication(request);
          })
          .flatMap(dto -> ServerResponse
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
          );
    }
}
