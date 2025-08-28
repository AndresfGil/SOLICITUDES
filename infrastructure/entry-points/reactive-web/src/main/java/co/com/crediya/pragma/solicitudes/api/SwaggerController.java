package co.com.crediya.pragma.solicitudes.api;

import co.com.crediya.pragma.solicitudes.api.dto.SolicitudDTO;
import co.com.crediya.pragma.solicitudes.api.dto.SolicitudResponseDTO;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/solicitudes")
@Tag(name = "Solicitudes", description = "API para gestión de solicitudes de préstamo")
@ConditionalOnProperty(name = "springdoc.swagger-ui.enabled", havingValue = "true")
public class SwaggerController {

    @PostMapping
    @Operation(
            summary = "Crear solicitud de préstamo",
            description = "Crea una nueva solicitud de préstamo con validación de tipo de préstamo",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Solicitud creada exitosamente",
                            content = @Content(schema = @Schema(implementation = SolicitudResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos de solicitud inválidos o tipo de préstamo no encontrado"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor"
                    )
            }
    )
    public SolicitudResponseDTO createSolicitud(@RequestBody SolicitudDTO solicitudDTO) {
        throw new UnsupportedOperationException("Este endpoint debe ser llamado a través de WebFlux");
    }

    @GetMapping
    @Operation(
            summary = "Obtener todas las solicitudes",
            description = "Retorna una lista de todas las solicitudes de préstamo",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de solicitudes obtenida exitosamente",
                            content = @Content(schema = @Schema(implementation = Solicitud.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor"
                    )
            }
    )
    public List<Solicitud> getAllSolicitudes() {
        throw new UnsupportedOperationException("Este endpoint debe ser llamado a través de WebFlux");
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener solicitud por ID",
            description = "Retorna una solicitud específica por su ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Solicitud encontrada exitosamente",
                            content = @Content(schema = @Schema(implementation = Solicitud.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Solicitud no encontrada"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor"
                    )
            }
    )
    public Solicitud getSolicitudById(@PathVariable Long id) {
        throw new UnsupportedOperationException("Este endpoint debe ser llamado a través de WebFlux");
    }
}
