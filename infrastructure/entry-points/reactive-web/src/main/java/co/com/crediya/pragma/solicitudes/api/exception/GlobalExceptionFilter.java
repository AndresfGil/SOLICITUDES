package co.com.crediya.pragma.solicitudes.api.exception;


import co.com.crediya.pragma.solicitudes.model.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalExceptionFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @NonNull
    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        return next.handle(request)
                .onErrorResume(BaseException.class, ex -> {
                    log.warn("Excepcion controlada code={} status={} path={} msg={}",
                            ex.getErrorCode(), ex.getStatusCode(), request.path(), ex.getMessage());
                    return ServerResponse
                            .status(ex.getStatusCode())
                            .bodyValue(ErrorResponse.builder()
                                    .errorCode(ex.getErrorCode())
                                    .tittle(ex.getTitle())
                                    .message(ex.getMessage())
                                    .errors(ex.getErrors())
                                    .status(HttpStatus.valueOf(ex.getStatusCode()))
                                    .timestamp(ex.getTimestamp())
                                    .build());
                })

                .onErrorResume(WebExchangeBindException.class, ex -> {
                    var errors = ex.getFieldErrors().stream()
                            .map(err -> err.getField() + ": " + err.getDefaultMessage())
                            .toList();

                    log.warn("Excepcion de validacion en path={}: {}", request.path(), errors);

                    return ServerResponse.status(HttpStatus.BAD_REQUEST)
                            .bodyValue(ErrorResponse.builder()
                                    .errorCode("VALIDATION_ERROR")
                                    .tittle("Error de validación")
                                    .message("Los datos enviados no son válidos")
                                    .errors(errors)
                                    .status(HttpStatus.BAD_REQUEST)
                                    .timestamp(java.time.LocalDateTime.now())
                                    .build());
                })
                .onErrorResume(RuntimeException.class, ex -> {
                    log.error("Error inesperado en path={} msg={}", request.path(), ex.getMessage(), ex);
                    
                    // Errores específicos de autenticación
                    if (ex.getMessage() != null && ex.getMessage().contains("Error al validar token")) {
                        return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                                .bodyValue(ErrorResponse.builder()
                                        .errorCode("INVALID_TOKEN")
                                        .tittle("Token inválido")
                                        .message("Token de autenticación inválido o expirado")
                                        .errors(java.util.List.of(ex.getMessage()))
                                        .status(HttpStatus.UNAUTHORIZED)
                                        .timestamp(java.time.LocalDateTime.now())
                                        .build());
                    }
                    
                    // Errores de comunicación con servicios externos
                    if (ex.getMessage() != null && ex.getMessage().contains("Connection refused")) {
                        return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .bodyValue(ErrorResponse.builder()
                                        .errorCode("SERVICE_UNAVAILABLE")
                                        .tittle("Servicio no disponible")
                                        .message("El servicio de autenticación no está disponible")
                                        .errors(java.util.List.of("Error de conexión con servicio externo"))
                                        .status(HttpStatus.SERVICE_UNAVAILABLE)
                                        .timestamp(java.time.LocalDateTime.now())
                                        .build());
                    }
                    
                    // Error genérico
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue(ErrorResponse.builder()
                                    .errorCode("INTERNAL_ERROR")
                                    .tittle("Error interno")
                                    .message("Error interno del servidor")
                                    .errors(java.util.List.of("Error inesperado del sistema"))
                                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .timestamp(java.time.LocalDateTime.now())
                                    .build());
                });
    }
}
