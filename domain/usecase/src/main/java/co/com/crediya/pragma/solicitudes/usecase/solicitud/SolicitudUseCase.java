package co.com.crediya.pragma.solicitudes.usecase.solicitud;

import co.com.crediya.pragma.solicitudes.model.exception.MontoFueraDeRangoException;
import co.com.crediya.pragma.solicitudes.model.exception.TipoPrestamoNotFoundException;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.TipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class SolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private static final Long ESTADO_SOLICITUD_PENDIENTE  = 1L;

    public Mono<Solicitud> saveSolicitud(Solicitud solicitud) {
        solicitud.setIdEstado(ESTADO_SOLICITUD_PENDIENTE );

        return tipoPrestamoRepository.findById(solicitud.getIdTipoPrestamo())
                .switchIfEmpty(Mono.error(new TipoPrestamoNotFoundException(solicitud.getIdTipoPrestamo())))
                .flatMap(tipoPrestamo -> {
                    BigDecimal montoMinimo = new BigDecimal(tipoPrestamo.getMontoMinimo());
                    BigDecimal montoMaximo = new BigDecimal(tipoPrestamo.getMontoMaximo());
                    
                    if (solicitud.getMonto().compareTo(montoMinimo) < 0 || solicitud.getMonto().compareTo(montoMaximo) > 0) {
                        return Mono.error(new MontoFueraDeRangoException(solicitud.getMonto(), montoMinimo, montoMaximo));
                    }
                    
                    return solicitudRepository.saveSolicitud(solicitud);
                });
    }


    public Mono<SolicitudRepository.PaginatedResult<Solicitud>> findAllSolicitudes(
            int page, int size, String sortBy, String sortDirection) {
        return solicitudRepository.findAllSolicitudes(page, size, sortBy, sortDirection);
    }
}
