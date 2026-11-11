package com.plataforma.asignaciones.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadatos de la documentación OpenAPI (Swagger UI) de ms-asignaciones.
 * UI: http://localhost:8083/swagger-ui.html  |  JSON: http://localhost:8083/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("MS Asignaciones")
                .description("Gestión de cuadrillas operativas y asignación por zona y especialidad (db_asignaciones)")
                .version("v1"));
    }
}
