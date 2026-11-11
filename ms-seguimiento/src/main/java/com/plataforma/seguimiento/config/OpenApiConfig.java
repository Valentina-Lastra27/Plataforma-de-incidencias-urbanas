package com.plataforma.seguimiento.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadatos de la documentación OpenAPI (Swagger UI) de ms-seguimiento.
 * UI: http://localhost:8084/swagger-ui.html  |  JSON: http://localhost:8084/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("MS Seguimiento")
                .description("Historial de cambios de estado de los reportes (db_seguimiento)")
                .version("v1"));
    }
}
