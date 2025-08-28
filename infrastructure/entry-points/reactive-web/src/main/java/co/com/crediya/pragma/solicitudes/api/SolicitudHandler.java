package co.com.crediya.pragma.solicitudes.api;

import co.com.crediya.pragma.solicitudes.api.dto.SolicitudDTO;
import co.com.crediya.pragma.solicitudes.api.helper.DtoValidator;
import co.com.crediya.pragma.solicitudes.api.helper.HttpReactiveLogger;
import co.com.crediya.pragma.solicitudes.api.helper.SolicitudMapper;
import co.com.crediya.pragma.solicitudes.api.helper.SolicitudResponseMapper;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.usecase.solicitud.SolicitudUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SolicitudHandler {

    private final SolicitudUseCase solicitudUseCase;
    private final DtoValidator  dtoValidator;
    private final SolicitudMapper solicitudMapper;
    private final SolicitudResponseMapper solicitudResponseMapper;

    public Mono<ServerResponse> listenSaveSolicitud(ServerRequest req) {
        Mono<ServerResponse> flow = req.bodyToMono(SolicitudDTO.class)
                .flatMap(dtoValidator::validate)
                .map(solicitudMapper::toDomain)
                .flatMap(solicitudUseCase::saveSolicitud)
                .map(solicitudResponseMapper::toResponse)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto));

        return HttpReactiveLogger.logMono(req, flow, "crear solicitud");
    }

    public Mono<ServerResponse> listenGetAllSolicitudes(ServerRequest req) {
        Mono<ServerResponse> flow = ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(solicitudUseCase.findAllSolicitudes(), Solicitud.class);
        return HttpReactiveLogger.logMono(req, flow, "lista de solicitudes");
    }

    public Mono<ServerResponse> listenGetSolicitudById(ServerRequest req) {
        Long id = Long.valueOf(req.pathVariable("id"));
        Mono<ServerResponse> flow = solicitudUseCase.findSolicitudById(id)
                .flatMap(solicitud -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(solicitud))
                        .switchIfEmpty(ServerResponse.notFound().build());
        return HttpReactiveLogger.logMono(req, flow, "traer soplicitud por id");
    }
}
