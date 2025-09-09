package co.com.crediya.pragma.solicitudes.usecase.solicitud.estados;

import co.com.crediya.pragma.solicitudes.model.exception.TipoPrestamoNotFoundException;
import co.com.crediya.pragma.solicitudes.model.notificaicones.CambioEstado;
import co.com.crediya.pragma.solicitudes.model.notificaicones.CambioEstadoRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor

public class EstadoUseCase {
    private final SolicitudRepository  solicitudRepository;
    private final CambioEstadoRepository cambioEstadoRepository;

    public Mono<Solicitud> updateEstado(CambioEstado cambioEstado) {
        return solicitudRepository.findSolicitudById(cambioEstado.getIdSolicitud())
                .switchIfEmpty(Mono.error(new TipoPrestamoNotFoundException(cambioEstado.getIdSolicitud())))
                .flatMap(solicitud -> {
                    solicitud.setIdEstado(cambioEstado.getIdEstado());
                    return cambioEstadoRepository.updateEstado(solicitud);
                });
    }
}
