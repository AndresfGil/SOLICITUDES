package co.com.crediya.pragma.solicitudes.model.solicitud.gateways;

import co.com.crediya.pragma.solicitudes.model.tipoprestamo.TipoPrestamo;
import reactor.core.publisher.Mono;

public interface TipoPrestamoRepository {

    Mono<TipoPrestamo> findById(Long id);
}
