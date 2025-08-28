package co.com.crediya.pragma.solicitudes.usecase.solicitud;

import co.com.crediya.pragma.solicitudes.model.exception.TipoPrestamoNotFoundException;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.TipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;

    public Mono<Solicitud> saveSolicitud(Solicitud solicitud) {
        solicitud.setIdEstado(1L);

        return tipoPrestamoRepository.existsById(solicitud.getIdTipoPrestamo())
                .flatMap(exists -> {
                    if (Boolean.FALSE.equals(exists)) {
                        return Mono.error(new TipoPrestamoNotFoundException(solicitud.getIdTipoPrestamo()));
                    }
                    return solicitudRepository.saveSolicitud(solicitud);
                });
    }

    public Flux<Solicitud> findAllSolicitudes() {
        return solicitudRepository.findAllSolicitudes();
    }

    public Mono<Solicitud> findSolicitudById(Long id) {
        return solicitudRepository.findSolicitudById(id);
    }
}
