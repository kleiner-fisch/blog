package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
@SecurityScheme(
  type = SecuritySchemeType.HTTP,
  name = "BasicAuth",
  scheme = "basic",
  in = SecuritySchemeIn.HEADER)
public class OpenAPIConfig {

    @Bean
	public OpenAPI springShopOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("SpringBlog API")
				.description("Small Blog developed with Spring Boot")
				.version("v0.0.1")
				.license(new License().name("Apache 2.0").url("http://springdoc.org")))
				.externalDocs(new ExternalDocumentation()
				.url("https://github.com/kleiner-fisch/blog"));
	}
}


