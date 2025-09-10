package co.com.crediya.pragma.solicitudes.model.estado.gateways;

import co.com.crediya.pragma.solicitudes.model.estado.Estado;
import reactor.core.publisher.Mono;

public interface EstadoRepository {

    Mono<Estado> findById(Long id);

}
