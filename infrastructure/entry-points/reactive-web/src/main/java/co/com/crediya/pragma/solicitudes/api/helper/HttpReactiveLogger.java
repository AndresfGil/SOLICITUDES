package co.com.crediya.pragma.solicitudes.api.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Slf4j
public final class HttpReactiveLogger {

    private HttpReactiveLogger() {}

    public static <T> Mono<T> logMono(ServerRequest req, Mono<T> mono, String action) {
        long t0 = System.currentTimeMillis();
        String method = req.methodName();
        String path = req.path();
        log.info("{} {} - {} - solicitud recibida", method, path, action);
        return mono
                .doOnSuccess(r ->
                        log.info("{} {} - {} - completada en {} ms",
                                method, path, action, System.currentTimeMillis() - t0))
                .doOnError(e ->
                        log.warn("{} {} - {} - fallo en {} ms: {}",
                                method, path, action, System.currentTimeMillis() - t0, e.toString()));
    }
}
