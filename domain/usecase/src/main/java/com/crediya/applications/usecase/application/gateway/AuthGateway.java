package com.crediya.applications.usecase.application.gateway;

import com.crediya.applications.usecase.application.gateway.dto.UserDTO;
import reactor.core.publisher.Mono;

public interface AuthGateway {
  Mono<UserDTO> getUserByIdentityCardNumber(String identityCardNumber);
}
