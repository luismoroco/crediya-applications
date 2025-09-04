package com.crediya.applications.model.application.gateways;

import com.crediya.applications.model.application.gateways.dto.UserDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AuthClient {
  Mono<UserDTO> getUserByIdentityCardNumber(String identityCardNumber);

  Flux<UserDTO> getUsers(List<String> identityCardNumbers);
}
