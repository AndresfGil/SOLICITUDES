package co.com.crediya.pragma.solicitudes.model.aprobacion.gateway;

import co.com.crediya.pragma.solicitudes.model.aprobacion.SolicitudAprobada;
import reactor.core.publisher.Mono;

public interface SolicitudAprobadaRepository {

    Mono<SolicitudAprobada> saveSolicitudAprobada(SolicitudAprobada solicitudAprobada);
}
