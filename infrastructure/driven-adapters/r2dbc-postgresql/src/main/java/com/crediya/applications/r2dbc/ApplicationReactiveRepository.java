package com.crediya.applications.r2dbc;

import com.crediya.applications.model.application.gateways.dto.AggregatedApplicationDTO;
import com.crediya.applications.model.application.gateways.dto.MinimalLoanDTO;
import com.crediya.applications.r2dbc.entity.ApplicationEntity;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ApplicationReactiveRepository extends ReactiveCrudRepository<ApplicationEntity, Long>,
  ReactiveQueryByExampleExecutor<ApplicationEntity> {

  @Query("""
    SELECT 
        a.application_id        AS application_id,
        a.amount                AS amount,
        a.email                 AS email,
        a.deadline              AS deadline,
        a.application_status_id AS application_status_id,
        a.loan_type_id          AS loan_type_id,
        lt.interest_rate        AS interest_rate
    FROM applications a
    LEFT JOIN loan_type lt 
        ON a.loan_type_id = lt.loan_type_id
    WHERE a.application_status_id IN (:application_status_ids)
      AND (
        'NONE' IN (:emails)
        OR a.email IN (:emails)
      )
    LIMIT :pageSize OFFSET :offset
    """)
  Flux<AggregatedApplicationDTO> findAggregatedApplications(
    @Param("application_status_ids") List<Integer> applicationStatusIds,
    @Param("pageSize") int pageSize,
    @Param("offset") int offset,
    @Param("emails") List<String> emails
  );

  @Query("""
    SELECT 
        a.application_id        AS loan_id,
        a.amount                AS amount,
        a.deadline              AS deadline,
        lt.interest_rate        AS interest_rate
    FROM applications a
    LEFT JOIN loan_type lt 
        ON a.loan_type_id = lt.loan_type_id
    WHERE a.application_status_id IN (:application_status_ids)
      AND a.email IN (:emails)
    """)
  Flux<MinimalLoanDTO> findMinimalLoans(
    @Param("application_status_ids") List<Integer> applicationStatusIds,
    @Param("emails") List<String> emails
  );
}
