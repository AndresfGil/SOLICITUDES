package co.com.crediya.pragma.solicitudes.model.solicitud.gateways;

import co.com.crediya.pragma.solicitudes.model.page.SolicitudFieldsPage;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudPage;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudPageRequest;
import co.com.crediya.pragma.solicitudes.model.solicitud.PrestamoAprobado;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SolicitudRepository  {

    Mono<Solicitud> saveSolicitud(Solicitud solicitud);

    Mono<Solicitud> findSolicitudById(Long idSolicitud);

    Mono<List<PrestamoAprobado>> findSolicitudesAprovadas(String email);

    Mono<SolicitudPage<SolicitudFieldsPage>> page(SolicitudPageRequest pageRequest);

}
