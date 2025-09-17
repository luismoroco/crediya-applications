package com.crediya.applications.api;

import com.crediya.applications.api.config.ApplicationPath;
import com.crediya.applications.usecase.application.dto.StartApplicationDTO;
import com.crediya.applications.usecase.application.dto.UpdateApplicationDTO;
import com.crediya.common.api.handling.GlobalExceptionFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

  private final Handler handler;
  private final GlobalExceptionFilter filter;
  private final ApplicationPath path;

    @RouterOperations({
      @RouterOperation(
        path = "/api/v1/applications",
        produces = { "application/json" },
        beanClass = Handler.class,
        method = RequestMethod.POST,
        beanMethod = "startApplication",
        operation = @Operation(
          operationId = "startApplication",
          summary = "Start a Loan request",
          security = @SecurityRequirement(name = "bearerAuth"),
          requestBody = @RequestBody(
            required = true,
            content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = StartApplicationDTO.class),
              examples = {
                @ExampleObject(
                  name = "Example",
                  value = """
                                  {
                                    "amount": 120000,
                                    "deadline": 500,
                                    "loanTypeId": 1
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
      ),
      @RouterOperation(
        path = "/api/v1/applications",
        produces = { "application/json" },
        beanClass = Handler.class,
        method = RequestMethod.GET,
        beanMethod = "getApplications",
        operation = @Operation(
          operationId = "getApplications",
          summary = "Get applications",
          security = @SecurityRequirement(name = "bearerAuth"),
          parameters = {
            @Parameter(
              name = "page",
              in = ParameterIn.QUERY,
              required = true,
              description = "page",
              schema = @Schema(type = "integer", example = "1")
            ),
            @Parameter(
              name = "page_size",
              in = ParameterIn.QUERY,
              required = true,
              description = "page size",
              schema = @Schema(type = "integer", example = "3")
            ),
            @Parameter(
              name = "application_statuses",
              in = ParameterIn.QUERY,
              required = true,
              description = "Applications' statuses",
              array = @ArraySchema(schema = @Schema(type = "string")),
              example = "PENDING"
            )
          },
          responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
          }
        )
      ),
      @RouterOperation(
        path = "/api/v1/applications/{application_id}",
        produces = { "application/json" },
        beanClass = Handler.class,
        method = RequestMethod.PUT,
        beanMethod = "updateApplication",
        operation = @Operation(
          operationId = "updateApplication",
          summary = "Update applications",
          security = @SecurityRequirement(name = "bearerAuth"),
          parameters = {
            @Parameter(
              name = "application_id",
              in = ParameterIn.PATH,
              required = true,
              description = "Applications's id",
              schema = @Schema(type = "integer", example = "1")
            ),
          },
          requestBody = @RequestBody(
            required = true,
            content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = UpdateApplicationDTO.class),
              examples = {
                @ExampleObject(
                  name = "Example",
                  value = """
                                  {
                                    "applicationStatus": "APPROVED"
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
    public RouterFunction<ServerResponse> routerFunction() {
        return route(POST(this.path.startApplication()), this.handler::startApplication)
          .andRoute(GET(this.path.getApplications()), this.handler::getApplications)
          .andRoute(PUT(this.path.updateApplication()), this.handler::updateApplication)
          .filter(filter);
    }
}
