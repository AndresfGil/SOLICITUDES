package co.com.crediya.pragma.solicitudes.model.estado.gateways;

import co.com.crediya.pragma.solicitudes.model.estado.Estado;
import co.com.crediya.pragma.solicitudes.model.tipoprestamo.TipoPrestamo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EstadoRepository {

    Mono<EstadoRepository> findAllEstado();

    Flux<TipoPrestamo> findAllTipoPrestamo();
}
