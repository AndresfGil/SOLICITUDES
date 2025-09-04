package co.com.crediya.pragma.solicitudes.api;

import co.com.crediya.pragma.solicitudes.api.dto.SolicitudDTO;
import co.com.crediya.pragma.solicitudes.api.helper.DtoValidator;
import co.com.crediya.pragma.solicitudes.api.helper.HttpReactiveLogger;
import co.com.crediya.pragma.solicitudes.api.helper.SolicitudMapper;
import co.com.crediya.pragma.solicitudes.api.helper.SolicitudResponseMapper;
import co.com.crediya.pragma.solicitudes.api.helper.PaginatedResponseMapper;
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
    private final PaginatedResponseMapper paginatedResponseMapper;

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

    public Mono<ServerResponse> listenGetSolicitudes(ServerRequest req) {
        log.info("Iniciando consulta de solicitudes paginadas - Path: {}", req.path());

        int page = Integer.parseInt(req.queryParam("page").orElse("0"));
        int size = Integer.parseInt(req.queryParam("size").orElse("10"));
        String sortBy = req.queryParam("sortBy").orElse("idSolicitud");
        String sortDirection = req.queryParam("sortDirection").orElse("ASC");

        if (page < 0) page = 0;
        if (size < 1 || size > 100) size = 10;
        int finalPage = page;
        int finalSize = size;

        Mono<ServerResponse> flow = extractTokenFromRequest(req)
                .doOnNext(token -> log.info("Token extraído para validación de administrador"))
                .flatMap(token -> authUseCase.validateAdminAdvisor(token)
                        .doOnNext(userInfo -> log.info("Usuario ADMIN/ASESOR validado: {}", userInfo.email()))
                        .flatMap(userInfo -> solicitudUseCase.findAllSolicitudes(finalPage, finalSize, sortBy, sortDirection, token)
                                .flatMap(paginatedResult -> paginatedResponseMapper.toPaginatedResponse(paginatedResult))
                                .flatMap(response -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(response))))
                .doOnError(error -> log.error("Error en consulta de solicitudes paginadas: {}", error.getMessage()));

        return HttpReactiveLogger.logMono(req, flow, "solicitudes paginadas");
    }

    private Mono<String> extractTokenFromRequest(ServerRequest req) {
        log.info("Extrayendo token de headers: {}", req.headers().firstHeader(HttpHeaders.AUTHORIZATION));

        return Mono.justOrEmpty(req.headers().firstHeader(HttpHeaders.AUTHORIZATION))
                .map(token -> token.replace("Bearer ", ""))
                .switchIfEmpty(Mono.error(new RuntimeException("Token no proporcionado")));
    }

}
