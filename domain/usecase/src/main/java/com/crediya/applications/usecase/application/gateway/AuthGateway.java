package com.crediya.applications.usecase.application.gateway;

import reactor.core.publisher.Mono;

public interface AuthGateway {
  Mono<String> getUserByIdentityCardNumber(String identityCardNumber);
}
