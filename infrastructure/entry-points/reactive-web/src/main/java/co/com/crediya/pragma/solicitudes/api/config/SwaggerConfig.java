package co.com.crediya.pragma.solicitudes.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("API de Solicitudes de Préstamo")
                        .version("1.0.0")
                        .description("API para la gestión de solicitudes de préstamo con validaciones de tipo de préstamo")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Andres Gil")
                                .email("andresgilmovil@gmail.com")));
    }
}