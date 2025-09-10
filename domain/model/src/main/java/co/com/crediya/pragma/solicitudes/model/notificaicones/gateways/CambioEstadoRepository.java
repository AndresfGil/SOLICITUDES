package co.com.crediya.pragma.solicitudes.model.notificaicones.gateways;

import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import reactor.core.publisher.Mono;

public interface CambioEstadoRepository {

    Mono<Solicitud> updateEstado(Solicitud solicitud);

}
