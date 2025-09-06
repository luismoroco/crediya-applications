package com.crediya.applications.r2dbc;

import com.crediya.applications.model.application.Application;
import com.crediya.applications.model.application.ApplicationStatus;
import com.crediya.applications.model.application.gateways.dto.AggregatedApplication;
import com.crediya.applications.r2dbc.entity.ApplicationEntity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationReactiveRepositoryAdapterTest {

    @InjectMocks
    ApplicationReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    ApplicationReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    private static ApplicationEntity createApplicationEntity() {
        return ApplicationEntity.builder()
          .applicationId(1L)
          .amount(1000L)
          .deadline(12)
          .email("john@example.com")
          .applicationStatusId(1)
          .loanTypeId(1)
          .build();
    }

    public static Application createApplication() {
        return new Application(1L, 1000L, 12, "john@example.com", 1, 1L);
    }

    @Test
    void mustFindValueById() {
        ApplicationEntity entity = createApplicationEntity();
        Application application = createApplication();

        when(repository.findById(1L)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Application.class)).thenReturn(application);

        Mono<Application> result = repositoryAdapter.findById(1L);

        StepVerifier.create(result)
          .expectNextMatches(u -> u.getEmail().equals("john@example.com"))
          .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        ApplicationEntity entity = createApplicationEntity();
        Application application = createApplication();

        when(repository.findAll()).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Application.class)).thenReturn(application);

        Flux<Application> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
          .expectNextMatches(u -> u.getEmail().equals("john@example.com"))
          .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        ApplicationEntity entity = createApplicationEntity();
        Application application = createApplication();

        when(mapper.map(application, ApplicationEntity.class)).thenReturn(entity);
        when(mapper.map(entity, Application.class)).thenReturn(application);
        when(repository.save(entity)).thenReturn(Mono.just(entity));

        Mono<Application> result = repositoryAdapter.save(application);

        StepVerifier.create(result)
          .expectNextMatches(u -> u.getEmail().equals("john@example.com"))
          .verifyComplete();
    }

    @Test
    void mustFindAggregatedApplications() {
        AggregatedApplication ap = new AggregatedApplication();
        ap.setApplicationId(1L);
        ap.setStatus(ApplicationStatus.PENDING);

        when(repositoryAdapter.findAggregatedApplications(List.of("PENDING"), 1, 3)).thenReturn(Flux.just(ap));

        StepVerifier.create(repositoryAdapter.findAggregatedApplications(List.of("PENDING"), 1, 3))
          .expectNextMatches(u -> u.getApplicationId().equals(ap.getApplicationId()))
          .verifyComplete();
    }
}
