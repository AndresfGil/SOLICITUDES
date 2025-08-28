package co.com.crediya.pragma.solicitudes.model.solicitud.gateways;

import reactor.core.publisher.Mono;

public interface TipoPrestamoRepository {

    Mono<Boolean> existsById(Long id);
}
