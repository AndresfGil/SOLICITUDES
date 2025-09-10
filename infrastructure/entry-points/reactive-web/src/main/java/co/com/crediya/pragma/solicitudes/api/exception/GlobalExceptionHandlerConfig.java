package co.com.crediya.pragma.solicitudes.api.exception;


import co.com.crediya.pragma.solicitudes.model.auth.exception.*;
import co.com.crediya.pragma.solicitudes.model.exception.InvalidCredentialsException;
import co.com.crediya.pragma.solicitudes.model.exception.BaseException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@lombok.extern.slf4j.Slf4j
public class GlobalExceptionHandlerConfig {

    @Bean
    public DefaultErrorAttributes defaultErrorAttributes() {
        return new DefaultErrorAttributes();
    }

    @Bean
    @Order(-2)
    public ErrorWebExceptionHandler globalExceptionHandler(DefaultErrorAttributes errorAttributes,
                                                           ApplicationContext applicationContext) {
        WebProperties.Resources resources = new WebProperties.Resources();
        return new GlobalErrorWebExceptionHandler(errorAttributes, resources, applicationContext);
    }

    static class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

        GlobalErrorWebExceptionHandler(DefaultErrorAttributes g, WebProperties.Resources r, ApplicationContext c) {
            super(g, r, c);
            super.setMessageReaders(ServerCodecConfigurer.create().getReaders());
            super.setMessageWriters(ServerCodecConfigurer.create().getWriters());
        }

        @Override
        protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
            return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
        }

        private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
            Throwable ex = getError(request);

            String correlationId = request.headers().firstHeader("X-Correlation-Id");

            HttpStatus status;
            ErrorResponse payload;

            log.error("[ERROR] {} on path={} correlationId={}", ex.getClass().getSimpleName(), request.path(), correlationId, ex);
            log.error("Exception details: {}", ex.getMessage());

             if(ex instanceof InvalidCredentialsException ic) {
                status = HttpStatus.UNAUTHORIZED;
                payload = new ErrorResponse(
                        ic.getErrorCode(),
                        ic.getMessage(),
                        status.value(),
                        request.path(),
                        java.time.Instant.now(),
                        correlationId,
                        null
                );

            } else if (ex instanceof ConstraintViolationException cve) {
                status = HttpStatus.BAD_REQUEST;
                List<ErrorDetail> details = cve.getConstraintViolations().stream()
                        .map(v -> new ErrorDetail(v.getPropertyPath().toString(), v.getMessage()))
                        .toList();
                payload = ErrorResponse.of(
                        "VALIDATION_ERROR",
                        "Datos de entrada inválidos",
                        status.value(),
                        request.path(),
                        details,
                        correlationId
                );
            } else if (ex instanceof BaseException be) {
                status = HttpStatus.valueOf(be.getStatusCode());
                List<ErrorDetail> details = be.getErrors() == null ? null : be.getErrors().stream()
                        .map(msg -> new ErrorDetail("", msg))
                        .toList();
                payload = ErrorResponse.of(
                        be.getErrorCode(),
                        be.getMessage(),
                        status.value(),
                        request.path(),
                        details,
                        correlationId
                );
            } else if (ex instanceof UnauthorizedException ue) {
                status = HttpStatus.FORBIDDEN;
                payload = ErrorResponse.of(
                        ue.getErrorCode(),
                        ue.getMessage(),
                        status.value(),
                        request.path(),
                        null,
                        correlationId
                );
            } else if (ex instanceof InvalidTokenException ite) {
                status = HttpStatus.UNAUTHORIZED;
                payload = ErrorResponse.of(
                        ite.getErrorCode(),
                        ite.getMessage(),
                        status.value(),
                        request.path(),
                        null,
                        correlationId
                );
            } else if (ex instanceof AccessDeniedException) {
                status = HttpStatus.FORBIDDEN;
                payload = ErrorResponse.of(
                        "ACCESS_DENIED",
                        "No tienes permisos para acceder a este recurso",
                        status.value(),
                        request.path(),
                        null,
                        correlationId
                );
            } else if (ex instanceof IllegalArgumentException iae) {
                status = HttpStatus.BAD_REQUEST;
                payload = ErrorResponse.of(
                        "INVALID_ARGUMENT",
                        iae.getMessage(),
                        status.value(),
                        request.path(),
                        null,
                        correlationId
                );
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                payload = ErrorResponse.of(
                        "INTERNAL_ERROR",
                        "Ocurrió un error inesperado",
                        status.value(),
                        request.path(),
                        null,
                        correlationId
                );
            }

            return ServerResponse.status(status)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(payload));
        }
    }
}
