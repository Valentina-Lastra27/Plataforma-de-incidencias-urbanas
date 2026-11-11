package com.plataforma.ciudadanos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadatos de la documentación OpenAPI (Swagger UI) de ms-ciudadanos.
 * UI: http://localhost:8081/swagger-ui.html  |  JSON: http://localhost:8081/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("MS Ciudadanos")
                .description("Gestión de ciudadanos que reportan incidencias (db_usuarios)")
                .version("v1"));
    }
}
