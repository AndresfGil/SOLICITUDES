package co.com.crediya.pragma.solicitudes.api;

import co.com.crediya.pragma.solicitudes.api.dto.SolicitudDTO;
import co.com.crediya.pragma.solicitudes.api.helper.DtoValidator;
import co.com.crediya.pragma.solicitudes.api.helper.HttpReactiveLogger;
import co.com.crediya.pragma.solicitudes.api.helper.PageRequestBuilder;
import co.com.crediya.pragma.solicitudes.api.helper.SolicitudMapper;
import co.com.crediya.pragma.solicitudes.api.helper.SolicitudResponseMapper;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudPageRequest;
import co.com.crediya.pragma.solicitudes.usecase.auth.AuthUseCase;
import co.com.crediya.pragma.solicitudes.usecase.solicitud.SolicitudUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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
    private final AuthUseCase authUseCase;
    private final DtoValidator  dtoValidator;
    private final SolicitudMapper solicitudMapper;
    private final SolicitudResponseMapper solicitudResponseMapper;
    private final PageRequestBuilder pageRequestBuilder;

    public Mono<ServerResponse> listenSaveSolicitud(ServerRequest req) {
        log.info("Iniciando creación de solicitud - Path: {}", req.path());

        Mono<ServerResponse> flow = req.bodyToMono(SolicitudDTO.class)
                .doOnNext(dto -> log.info("DTO de solicitud recibido"))
                .flatMap(dto -> extractTokenFromRequest(req)
                        .doOnNext(token -> log.info("Token extraído para validación"))
                        .flatMap(token -> authUseCase.validateClientUserForSelf(token, dto.documentoIdentidad()))
                        .doOnNext(userInfo -> log.info("Usuario CLIENTE validado para sí mismo: {}", userInfo.email()))
                        .thenReturn(dto))
                .flatMap(dto -> dtoValidator.validate(dto)
                        .map(solicitudMapper::toDomain)
                        .flatMap(solicitudUseCase::saveSolicitud)
                        .map(solicitudResponseMapper::toResponse)
                        .flatMap(response -> ServerResponse.ok().bodyValue(response)))
                .doOnError(error -> log.error("Error en creación de solicitud: {}", error.getMessage()));

        return HttpReactiveLogger.logMono(req, flow, "crear solicitud");
    }

    public Mono<ServerResponse> listenGetSolicitudes(ServerRequest serverRequest) {
        log.info("Iniciando consulta de solicitudes con usuarios - Path: {}", serverRequest.path());
        
        // Extraer parámetros de query usando el helper
        SolicitudPageRequest pageRequest = pageRequestBuilder.buildFromQuery(serverRequest);
        log.info("Parámetros de paginación extraídos: page={}, size={}, sort={}, query={}, status={}", 
                pageRequest.getPage(), pageRequest.getSize(), pageRequest.getSort(), 
                pageRequest.getQuery(), pageRequest.getStatus());
        
        return extractTokenFromRequest(serverRequest)
                .flatMap(token -> {
                    log.info("Token extraído para enriquecimiento de usuarios");
                    return solicitudUseCase.findAllSolicitudes(pageRequest, token)
                            .flatMap(sp -> ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(sp));
                })
                .doOnError(ex -> log.error("[GET_SOLICITUDES] Error: {}", ex.toString()));
    }

    private Mono<String> extractTokenFromRequest(ServerRequest req) {
        log.info("Extrayendo token de headers: {}", req.headers().firstHeader(HttpHeaders.AUTHORIZATION));

        return Mono.justOrEmpty(req.headers().firstHeader(HttpHeaders.AUTHORIZATION))
                .map(token -> token.replace("Bearer ", ""))
                .switchIfEmpty(Mono.error(new RuntimeException("Token no proporcionado")));
    }

}
