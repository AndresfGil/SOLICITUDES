package co.com.crediya.pragma.solicitudes.model.solicitud.gateways;

import co.com.crediya.pragma.solicitudes.model.page.SolicitudFieldsPage;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudPage;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudPageRequest;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import reactor.core.publisher.Mono;

public interface SolicitudRepository  {

    Mono<Solicitud> saveSolicitud(Solicitud solicitud);

    Mono<Solicitud> findSolicitudById(Long idSolicitud);

    Mono<SolicitudPage<SolicitudFieldsPage>> page(SolicitudPageRequest pageRequest);

}
