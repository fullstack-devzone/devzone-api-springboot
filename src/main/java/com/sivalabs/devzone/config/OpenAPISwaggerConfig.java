package com.sivalabs.devzone.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPISwaggerConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder().group("DevZone").pathsToMatch("/api/**").build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("DevZone Java REST API")
                                .description("DevZone Java REST API")
                                .version("v0.0.1")
                                .license(
                                        new License()
                                                .name("Apache 2.0")
                                                .url("https://sivalabs.in")))
                .externalDocs(
                        new ExternalDocumentation()
                                .description("DevZone Java REST API")
                                .url("https://github.com/fullstack-devzone/fullstack-devzone"));
    }
}
