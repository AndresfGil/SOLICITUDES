package co.com.crediya.pragma.solicitudes.api.docs;

import co.com.crediya.pragma.solicitudes.api.SolicitudHandler;
import co.com.crediya.pragma.solicitudes.api.dto.SolicitudDTO;
import co.com.crediya.pragma.solicitudes.api.dto.SolicitudResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;

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
                            description = "Crea una nueva solicitud de préstamo con validación de tipo de préstamo",
                            requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = SolicitudDTO.class))),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Solicitud creada exitosamente",
                                            content = @Content(schema = @Schema(implementation = SolicitudResponseDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos o tipo de préstamo no encontrado"),
                                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/solicitudes",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = SolicitudHandler.class,
                    beanMethod = "listenGetAllSolicitudes",
                    operation = @Operation(
                            operationId = "getAllSolicitudes",
                            summary = "Obtener todas las solicitudes",
                            description = "Retorna una lista de todas las solicitudes de préstamo",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida exitosamente",
                                            content = @Content(schema = @Schema(implementation = Solicitud.class))),
                                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/solicitudes/{id}",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = SolicitudHandler.class,
                    beanMethod = "listenGetSolicitudById",
                    operation = @Operation(
                            operationId = "getSolicitudById",
                            summary = "Obtener solicitud por ID",
                            description = "Retorna una solicitud específica por su ID",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Solicitud encontrada exitosamente",
                                            content = @Content(schema = @Schema(implementation = Solicitud.class))),
                                    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
                                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(SolicitudHandler handler);

}