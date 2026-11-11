package com.plataforma.zonas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadatos de la documentación OpenAPI (Swagger UI) de ms-zonas.
 * UI: http://localhost:8085/swagger-ui.html  |  JSON: http://localhost:8085/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("MS Zonas")
                .description("Gestión de zonas geográficas de la ciudad (db_zonas)")
                .version("v1"));
    }
}
