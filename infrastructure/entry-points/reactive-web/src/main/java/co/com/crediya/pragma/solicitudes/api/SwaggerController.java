package co.com.crediya.pragma.solicitudes.api;

import co.com.crediya.pragma.solicitudes.api.dto.SolicitudDTO;
import co.com.crediya.pragma.solicitudes.api.dto.SolicitudResponseDTO;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudPage;
import co.com.crediya.pragma.solicitudes.model.solicitud.SolicitudConUsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
            summary = "Obtener solicitudes paginadas con información de usuarios",
            description = "Retorna una lista paginada de solicitudes enriquecida con información de usuarios",
            parameters = {
                    @Parameter(name = "page", description = "Número de página (base 0)", example = "0", schema = @Schema(type = "integer", defaultValue = "0")),
                    @Parameter(name = "size", description = "Tamaño de página (máximo 200)", example = "50", schema = @Schema(type = "integer", defaultValue = "50")),
                    @Parameter(name = "sort", description = "Dirección de ordenamiento: ASC o DESC", example = "ASC", schema = @Schema(type = "string", allowableValues = {"ASC", "DESC"}, defaultValue = "ASC")),
                    @Parameter(name = "columnSort", description = "Columna para ordenar", example = "id_solicitud", schema = @Schema(type = "string")),
                    @Parameter(name = "query", description = "Texto para filtrar por tipo de préstamo", example = "consumo", schema = @Schema(type = "string")),
                    @Parameter(name = "status", description = "Estado de la solicitud (puede ser múltiple separado por comas)", example = "APROBADO", schema = @Schema(type = "string"))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de solicitudes con usuarios obtenida exitosamente",
                            content = @Content(schema = @Schema(implementation = SolicitudPage.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Parámetros de consulta inválidos"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor"
                    )
            }
    )
    public SolicitudPage<SolicitudConUsuarioResponse> getSolicitudesWithUsers(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "50") Integer size,
            @RequestParam(value = "sort", defaultValue = "ASC") String sort,
            @RequestParam(value = "columnSort", required = false) String columnSort,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "status", required = false) String status) {
        throw new UnsupportedOperationException("Este endpoint debe ser llamado a través de WebFlux");
    }

}
