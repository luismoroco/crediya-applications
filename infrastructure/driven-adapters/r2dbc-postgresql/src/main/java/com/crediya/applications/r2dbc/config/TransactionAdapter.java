package com.crediya.applications.r2dbc.config;

import com.crediya.common.transaction.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TransactionAdapter implements Transaction {

  private final TransactionalOperator transactionalOperator;

  public TransactionAdapter(ReactiveTransactionManager reactiveTransactionManager) {
    this.transactionalOperator = TransactionalOperator.create(reactiveTransactionManager);
  }

  @Override
  public <T> Mono<T> init(Mono<T> mono) {
    return mono.as(transactionalOperator::transactional);
  }

  @Override
  public <T> Flux<T> init(Flux<T> flux) {
    return flux.as(transactionalOperator::transactional);
  }
}
