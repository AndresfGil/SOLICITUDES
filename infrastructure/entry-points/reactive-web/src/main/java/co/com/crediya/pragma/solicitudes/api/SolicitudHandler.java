package co.com.crediya.pragma.solicitudes.api;

import co.com.crediya.pragma.solicitudes.api.dto.SolicitudDTO;
import co.com.crediya.pragma.solicitudes.api.helper.DtoValidator;
import co.com.crediya.pragma.solicitudes.api.helper.HttpReactiveLogger;
import co.com.crediya.pragma.solicitudes.api.helper.PageRequestBuilder;
import co.com.crediya.pragma.solicitudes.api.helper.SolicitudMapper;
import co.com.crediya.pragma.solicitudes.api.helper.SolicitudResponseMapper;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudPageRequest;
import co.com.crediya.pragma.solicitudes.usecase.solicitud.SolicitudUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class SolicitudHandler {

    private final SolicitudUseCase solicitudUseCase;
    private final DtoValidator  dtoValidator;
    private final SolicitudMapper solicitudMapper;
    private final SolicitudResponseMapper solicitudResponseMapper;
    private final PageRequestBuilder pageRequestBuilder;

    public Mono<ServerResponse> listenSaveSolicitud(ServerRequest req) {
        log.info("Iniciando creación de solicitud - Path: {}", req.path());

        Mono<ServerResponse> flow = req.bodyToMono(SolicitudDTO.class)
                .doOnNext(dto -> log.info("DTO de solicitud recibido"))
                .flatMap(dto -> dtoValidator.validate(dto)
                        .map(solicitudMapper::toDomain)
                        .flatMap(solicitudUseCase::saveSolicitud)
                        .map(solicitudResponseMapper::toResponse)
                        .flatMap(response -> ServerResponse.ok().bodyValue(response)))
                .doOnError(error -> log.error("Error en creación de solicitud: {}", error.getMessage()));

        return HttpReactiveLogger.logMono(req, flow, "crear solicitud");
    }


    public Mono<ServerResponse> listenGetSolicitudes(ServerRequest serverRequest) {
        log.info("Procesando solicitud GET con query params: {}", serverRequest.queryParams());
        
        SolicitudPageRequest pageRequest = pageRequestBuilder.buildFromQuery(serverRequest);
        log.info("PageRequest construido: page={}, size={}, sort={}, query={}", 
                pageRequest.getPage(), pageRequest.getSize(), pageRequest.getSort(), pageRequest.getQuery());
        
        return solicitudUseCase.findAllSolicitudes(pageRequest)
                .flatMap(sp -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(sp))
                .doOnError(ex -> log.error("[GET_SOLICITUDES] Error: {}", ex.toString()));
    }


}
