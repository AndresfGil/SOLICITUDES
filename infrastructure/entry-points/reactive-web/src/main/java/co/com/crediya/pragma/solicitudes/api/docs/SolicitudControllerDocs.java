package co.com.crediya.pragma.solicitudes.api.docs;

import co.com.crediya.pragma.solicitudes.api.SolicitudHandler;
import co.com.crediya.pragma.solicitudes.api.dto.SolicitudDTO;
import co.com.crediya.pragma.solicitudes.api.dto.SolicitudResponseDTO;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudPage;
import co.com.crediya.pragma.solicitudes.model.solicitud.SolicitudConUsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

public interface SolicitudControllerDocs {

    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/solicitudes",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = SolicitudHandler.class,
                    beanMethod = "listenSaveSolicitud",
                    operation = @Operation(
                            operationId = "createSolicitud",
                            summary = "Crear solicitud de préstamo",
                            description = "Crea una nueva solicitud de préstamo. Solo usuarios con rol CLIENTE pueden crear solicitudes.",
                            security = @SecurityRequirement(name = "bearerAuth"),
                            requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = SolicitudDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Solicitud creada exitosamente",
                                            content = @Content(schema = @Schema(implementation = SolicitudResponseDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos o tipo de préstamo no encontrado"),
                                    @ApiResponse(responseCode = "401", description = "Token no proporcionado o inválido"),
                                    @ApiResponse(responseCode = "403", description = "Usuario no autorizado - Solo usuarios CLIENTE pueden crear solicitudes"),
                                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/solicitudes",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = SolicitudHandler.class,
                    beanMethod = "listenGetSolicitudes",
                    operation = @Operation(
                            operationId = "getSolicitudesWithUsers",
                            summary = "Obtener solicitudes paginadas con información de usuarios",
                            description = "Retorna una lista paginada de solicitudes enriquecida con información de usuarios. Solo usuarios con rol ADMIN o ASESOR pueden consultar solicitudes.",
                            security = @SecurityRequirement(name = "bearerAuth"),
                            parameters = {
                                    @Parameter(name = "page", description = "Número de página (base 0)", example = "0", schema = @Schema(type = "integer", defaultValue = "0")),
                                    @Parameter(name = "size", description = "Tamaño de página (máximo 200)", example = "50", schema = @Schema(type = "integer", defaultValue = "50")),
                                    @Parameter(name = "sort", description = "Dirección de ordenamiento: ASC o DESC", example = "ASC", schema = @Schema(type = "string", allowableValues = {"ASC", "DESC"}, defaultValue = "ASC")),
                                    @Parameter(name = "columnSort", description = "Columna para ordenar", example = "id_solicitud", schema = @Schema(type = "string")),
                                    @Parameter(name = "query", description = "Texto para filtrar por tipo de préstamo", example = "consumo", schema = @Schema(type = "string")),
                                    @Parameter(name = "status", description = "Estado de la solicitud (puede ser múltiple separado por comas)", example = "APROBADO", schema = @Schema(type = "string"))
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Lista de solicitudes con usuarios obtenida exitosamente",
                                            content = @Content(schema = @Schema(implementation = SolicitudPage.class))),
                                    @ApiResponse(responseCode = "400", description = "Parámetros de consulta inválidos"),
                                    @ApiResponse(responseCode = "401", description = "Token no proporcionado o inválido"),
                                    @ApiResponse(responseCode = "403", description = "Usuario no autorizado - Solo usuarios ADMIN/ASESOR pueden consultar solicitudes"),
                                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(SolicitudHandler handler);

}