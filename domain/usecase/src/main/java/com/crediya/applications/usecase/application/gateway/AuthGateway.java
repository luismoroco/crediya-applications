package com.crediya.applications.usecase.application.gateway;

import com.crediya.applications.usecase.application.gateway.dto.UserDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AuthGateway {
  Mono<UserDTO> getUserByIdentityCardNumber(String identityCardNumber);

  Flux<UserDTO> getUsers(List<String> identityCardNumbers);
}
