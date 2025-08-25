package com.crediya.applications.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI apiInfo() {
    return new OpenAPI()
      .info(new Info()
        .title("com.crediya.applications.api")
        .description("crediya-applications")
        .version("1.0.0")
        .contact(new Contact()
          .name("Luis Moroco")
          .email("lmoroco@crediya.com")
        )
      );
  }
}
