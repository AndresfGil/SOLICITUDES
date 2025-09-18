package co.com.crediya.pragma.solicitudes.api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Documentación OpenAPI para el controlador de health check
 */
@Tag(name = "Health Check", description = "Endpoints para verificar el estado del servicio")
public interface HealthControllerDocs {

    @Operation(
        summary = "Health Check",
        description = "Verifica el estado del servicio de solicitudes PowerUp"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servicio funcionando correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    Mono<ResponseEntity<Map<String, Object>>> health();

    @Operation(
        summary = "Ping",
        description = "Endpoint simple para verificar conectividad con el servicio"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servicio respondiendo correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    Mono<ResponseEntity<Map<String, String>>> ping();
}
