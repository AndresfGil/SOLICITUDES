package co.com.crediya.pragma.solicitudes.r2dbc.helper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public final class ReactiveLogger {

    private ReactiveLogger() {}

    public static <T> Mono<T> logMono(Mono<T> mono, String op, String key, Object value) {
        final long t0 = System.currentTimeMillis();
        log.debug("{} start {}={}", op, key, value);
        return mono
                .doOnSuccess(res -> log.info("{} ok {}={} elapsedMs={} found={}",
                        op, key, value, System.currentTimeMillis() - t0, res != null))
                .doOnError(e -> log.warn("{} fail {}={} elapsedMs={} err={}",
                        op, key, value, System.currentTimeMillis() - t0, e.toString()));
    }

    public static <T> Flux<T> logFlux(Flux<T> flux, String op) {
        final long t0 = System.currentTimeMillis();
        log.debug("{} start", op);
        return flux
                .doOnComplete(() -> log.info("{} complete elapsedMs={}", op, System.currentTimeMillis() - t0))
                .doOnError(e -> log.warn("{} fail elapsedMs={} err={}", op, System.currentTimeMillis() - t0, e.toString()));
    }
}
