package com.crediya.applications.model.application.gateways;

import reactor.core.publisher.Mono;

public interface Notifications {
  Mono<String> send(String message);
}
