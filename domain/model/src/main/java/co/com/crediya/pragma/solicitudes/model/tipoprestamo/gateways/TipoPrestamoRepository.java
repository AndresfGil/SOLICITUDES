package co.com.crediya.pragma.solicitudes.model.tipoprestamo.gateways;

import co.com.crediya.pragma.solicitudes.model.tipoprestamo.TipoPrestamo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TipoPrestamoRepository {

    Mono<TipoPrestamo> findByIdTipoPrestamo(Integer id);

    Flux<TipoPrestamo> findAllTipoPrestamo();


}
