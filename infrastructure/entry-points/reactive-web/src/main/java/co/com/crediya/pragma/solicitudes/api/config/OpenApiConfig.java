package co.com.crediya.pragma.solicitudes.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PowerUp Solicitudes API")
                        .description("API para gestión de solicitudes de préstamo del sistema PowerUp. " +
                                "Para crear solicitudes, necesitas un token JWT de un usuario con rol CLIENTE. " +
                                "Obtén el token del servicio de autenticación y úsalo en el botón 'Authorize'.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("PowerUp Team")
                                .email("support@powerup.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8081")
                                .description("Servidor de desarrollo"),
                        new Server()
                                .url("https://api.powerup.com")
                                .description("Servidor de producción")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Token JWT obtenido del servicio de autenticación")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
