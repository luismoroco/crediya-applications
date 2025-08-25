package com.crediya.applications.api;

import com.crediya.applications.api.dto.StartApplicationServerRequest;
import com.crediya.common.api.handling.GlobalExceptionFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @RouterOperations({
      @RouterOperation(
        path = "/api/v1/applications",
        produces = { "application/json" },
        beanClass = Handler.class,
        method = RequestMethod.POST,
        beanMethod = "listenPOSTStartApplication",
        operation = @Operation(
          operationId = "startApplication",
          summary = "Init application",
          requestBody = @RequestBody(
            required = true,
            content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = StartApplicationServerRequest.class),
              examples = {
                @ExampleObject(
                  name = "Example",
                  value = """
                                  {
                                    "amount": 5000,
                                    "deadline": 12,
                                    "email": "usuario@correo.com",
                                    "loanType": "PERSONAL_LOAN",
                                    "identityCardNumber": "12345678"
                                  }
                                  """
                )
              }
            )
          ),
          responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
          }
        )
      )
    })
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler, GlobalExceptionFilter filter) {
        return route(POST("/api/v1/applications"), handler::listenPOSTStartApplication)
          .filter(filter);
    }
}
