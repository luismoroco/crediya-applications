package com.crediya.applications.r2dbc.config;

import com.crediya.common.transaction.Transaction;

import org.springframework.stereotype.Component;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ReactiveTransaction implements Transaction {

  private final TransactionalOperator transactionalOperator;

  public ReactiveTransaction(ReactiveTransactionManager reactiveTransactionManager) {
    this.transactionalOperator = TransactionalOperator.create(reactiveTransactionManager);
  }

  @Override
  public <TObject> Mono<TObject> init(Mono<TObject> mono) {
    return mono.as(transactionalOperator::transactional);
  }

  @Override
  public <TObject> Flux<TObject> init(Flux<TObject> flux) {
    return flux.as(transactionalOperator::transactional);
  }
}
