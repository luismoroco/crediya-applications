package com.crediya.applications.r2dbc;

import com.crediya.applications.model.application.AggregatedApplication;
import com.crediya.applications.r2dbc.entity.ApplicationEntity;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ApplicationReactiveRepository extends ReactiveCrudRepository<ApplicationEntity, Long>, ReactiveQueryByExampleExecutor<ApplicationEntity> {

  @Query("" +
    "SELECT application_id, amount " +
    "FROM applications " +
    "WHERE " +
      "application_status_id IN (:application_status_ids) ")
  Flux<AggregatedApplication> findAggregatedApplications(@Param("application_status_ids") List<Integer> applicationStatusIds);
}
