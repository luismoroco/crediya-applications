package com.crediya.applications.r2dbc.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class TransactionAdapterTest {

  @Mock
  private ReactiveTransactionManager reactiveTransactionManager;

  @Mock
  private TransactionalOperator transactionalOperator;

  @Test
  void shouldApplyTransactionToMono() {
    TransactionAdapter transaction = new TransactionAdapter(reactiveTransactionManager);
    Mono<String> inputMono = Mono.just("test");

    transaction.init(inputMono);
  }

  @Test
  void shouldApplyTransactionToFlux() {
    TransactionAdapter transaction = new TransactionAdapter(reactiveTransactionManager);
    Flux<String> inputFlux = Flux.just("test");

    transaction.init(inputFlux);
  }
}

