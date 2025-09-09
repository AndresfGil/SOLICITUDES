package co.com.crediya.pragma.solicitudes.api.docs;

import co.com.crediya.pragma.solicitudes.api.SolicitudHandler;
import co.com.crediya.pragma.solicitudes.api.dto.CambioEstadoDTO;
import co.com.crediya.pragma.solicitudes.api.dto.SolicitudDTO;
import co.com.crediya.pragma.solicitudes.api.dto.SolicitudResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
                            security = @SecurityRequirement(name = "Bearer Authentication"),
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
                    path = "/api/v1/solicitud",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.PUT,
                    beanClass = SolicitudHandler.class,
                    beanMethod = "listenUpdateSolicitud",
                    operation = @Operation(
                            operationId = "updateEstadoSolicitud",
                            summary = "Actualizar estado de una solicitud",
                            description = "Actualiza una soplicitud. Solo usuarios con rol ASESOR pueden crear solicitudes.",
                            security = @SecurityRequirement(name = "Bearer Authentication"),
                            requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = CambioEstadoDTO.class))),
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
                            operationId = "getSolicitudes",
                            summary = "Obtener solicitudes de préstamo",
                            description = "Obtiene una lista paginada de solicitudes de préstamo con filtros y ordenamiento. Solo usuarios con rol ASESOR pueden consultar solicitudes.",
                            security = @SecurityRequirement(name = "Bearer Authentication"),
                            parameters = {
                                    @Parameter(
                                            name = "page",
                                            description = "Número de página (comienza en 0)",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "integer", defaultValue = "0", example = "0")
                                    ),
                                    @Parameter(
                                            name = "size",
                                            description = "Tamaño de la página (número de elementos por página)",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "integer", defaultValue = "50", example = "10")
                                    ),
                                    @Parameter(
                                            name = "sort",
                                            description = "Dirección del ordenamiento",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "string", allowableValues = {"ASC", "DESC"}, defaultValue = "ASC", example = "ASC")
                                    ),
                                    @Parameter(
                                            name = "columnSort",
                                            description = "Columna por la cual ordenar",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "string", example = "monto")
                                    ),
                                    @Parameter(
                                            name = "query",
                                            description = "Filtro de búsqueda (busca en tipo de préstamo)",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "string", defaultValue = "%", example = "personal")
                                    ),
                                    @Parameter(
                                            name = "status",
                                            description = "Filtro por estado de solicitud (separado por comas para múltiples estados)",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "string", example = "1,2,3")
                                    )
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida exitosamente",
                                            content = @Content(schema = @Schema(implementation = SolicitudResponseDTO.class))),
                                    @ApiResponse(responseCode = "401", description = "Token no proporcionado o inválido"),
                                    @ApiResponse(responseCode = "403", description = "Usuario no autorizado - Solo usuarios ASESOR pueden consultar solicitudes"),
                                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(SolicitudHandler handler);

}