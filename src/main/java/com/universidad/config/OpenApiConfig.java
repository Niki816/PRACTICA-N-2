package com.universidad.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // Configuración general de OpenAPI (metadata y seguridad)
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Registro Universitario")
                        .version("1.0")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    // Grupo específico para inscripciones (usando la nueva sintaxis de GroupedOpenApi)
    @Bean
    public GroupedOpenApi inscripcionesGroup() {
        return GroupedOpenApi.builder()
                .group("inscripciones") // Nombre del grupo
                .pathsToMatch("/api/inscripciones/**") // Filtra rutas
                .build();
    }

    // Grupo para otros endpoints (opcional)
    @Bean
    public GroupedOpenApi otrosEndpointsGroup() {
        return GroupedOpenApi.builder()
                .group("otros")
                .pathsToMatch("/api/**")
                .pathsToExclude("/api/inscripciones/**") // Excluye las rutas de inscripciones
                .build();
    }
    @Bean
    public GroupedOpenApi estudiantesApi() {
        return GroupedOpenApi.builder()
                .group("estudiantes")
                .pathsToMatch("/api/estudiantes/**")
                .build();
    }
}