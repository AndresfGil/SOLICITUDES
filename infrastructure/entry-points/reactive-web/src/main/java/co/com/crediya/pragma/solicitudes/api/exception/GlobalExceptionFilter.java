package co.com.crediya.pragma.solicitudes.api.exception;


import co.com.crediya.pragma.solicitudes.model.exception.BaseException;
import co.com.crediya.pragma.solicitudes.model.exception.ValidationException;
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

import java.util.stream.Collectors;

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
                            .collect(Collectors.toList());

                    var validationEx = new ValidationException(errors);

                    log.warn("Excepcion de validacion: {}", errors);

                    return ServerResponse.status(validationEx.getStatusCode())
                            .bodyValue(ErrorResponse.builder()
                                    .errorCode(validationEx.getErrorCode())
                                    .tittle(validationEx.getTitle())
                                    .message(validationEx.getMessage())
                                    .errors(validationEx.getErrors())
                                    .status(HttpStatus.valueOf(validationEx.getStatusCode()))
                                    .timestamp(validationEx.getTimestamp())
                                    .build());
                });
    }
}
