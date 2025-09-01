package co.com.crediya.pragma.solicitudes.api;

import co.com.crediya.pragma.solicitudes.api.dto.SolicitudDTO;
import co.com.crediya.pragma.solicitudes.api.helper.DtoValidator;
import co.com.crediya.pragma.solicitudes.api.helper.HttpReactiveLogger;
import co.com.crediya.pragma.solicitudes.api.helper.SolicitudMapper;
import co.com.crediya.pragma.solicitudes.api.helper.SolicitudResponseMapper;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
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


    public Mono<ServerResponse> listenGetAllSolicitudes(ServerRequest req) {
        log.info("Iniciando consulta de solicitudes - Path: {}", req.path());

        Mono<ServerResponse> flow = extractTokenFromRequest(req)
                .doOnNext(token -> log.info("Token extraído para validación de administrador"))
                .flatMap(authUseCase::validateAdminAdvisor)
                .doOnNext(userInfo -> log.info("Usuario ADMIN/ASESOR validado: {}", userInfo.email()))
                .flatMap(userInfo -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(solicitudUseCase.findAllSolicitudes(), Solicitud.class))
                .doOnError(error -> log.error("Error en consulta de solicitudes: {}", error.getMessage()));

        return HttpReactiveLogger.logMono(req, flow, "lista de solicitudes");
    }


    private Mono<String> extractTokenFromRequest(ServerRequest req) {
        log.info("Extrayendo token de headers: {}", req.headers().firstHeader(HttpHeaders.AUTHORIZATION));

        return Mono.justOrEmpty(req.headers().firstHeader(HttpHeaders.AUTHORIZATION))
                .map(token -> token.replace("Bearer ", ""))
                .switchIfEmpty(Mono.error(new RuntimeException("Token no proporcionado")));
    }


    public Mono<ServerResponse> listenGetSolicitudById(ServerRequest req) {
        log.info("Iniciando consulta de solicitud por ID - Path: {}", req.path());
        Long id = Long.valueOf(req.pathVariable("id"));

        Mono<ServerResponse> flow = extractTokenFromRequest(req)
                .doOnNext(token -> log.info("Token extraído para validación de administrador"))
                .flatMap(authUseCase::validateAdminAdvisor)
                .doOnNext(userInfo -> log.info("Usuario ADMIN/ASESOR validado: {}", userInfo.email()))
                .flatMap(userInfo -> solicitudUseCase.findSolicitudById(id)
                        .flatMap(solicitud -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(solicitud))
                        .switchIfEmpty(ServerResponse.notFound().build()))
                .doOnError(error -> log.error("Error en consulta de solicitud por ID: {}", error.getMessage()));

        return HttpReactiveLogger.logMono(req, flow, "traer solicitud por id");
    }
}
