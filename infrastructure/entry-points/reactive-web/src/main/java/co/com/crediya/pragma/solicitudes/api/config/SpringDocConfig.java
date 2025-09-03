package co.com.crediya.pragma.solicitudes.api.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public GroupedOpenApi solicitudesApi() {
        return GroupedOpenApi.builder()
                .group("solicitudes")
                .pathsToMatch("/api/v1/solicitudes")
                .addOpenApiCustomizer(openApi -> openApi.getInfo()
                        .title("API de Solicitudes")
                        .version("1.0.0")
                        .description("API para gestión de solicitudes de préstamo"))
                .build();
    }
}
