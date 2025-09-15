package co.com.crediya.pragma.solicitudes.usecase.solicitud.estados;

import co.com.crediya.pragma.solicitudes.model.aprobacion.SolicitudAprobada;
import co.com.crediya.pragma.solicitudes.model.aprobacion.gateway.SolicitudAprobadaRepository;
import co.com.crediya.pragma.solicitudes.model.estado.Estado;
import co.com.crediya.pragma.solicitudes.model.estado.EstadoEnum;
import co.com.crediya.pragma.solicitudes.model.estado.gateways.EstadoRepository;
import co.com.crediya.pragma.solicitudes.model.exception.EstadoNotFoundException;
import co.com.crediya.pragma.solicitudes.model.exception.SolicitudNotFoundException;
import co.com.crediya.pragma.solicitudes.model.exception.TipoPrestamoNotFoundException;
import co.com.crediya.pragma.solicitudes.model.notificaicones.CambioEstado;
import co.com.crediya.pragma.solicitudes.model.notificaicones.EmailNotification;
import co.com.crediya.pragma.solicitudes.model.notificaicones.EstadoValidacion;
import co.com.crediya.pragma.solicitudes.model.notificaicones.gateways.CambioEstadoRepository;
import co.com.crediya.pragma.solicitudes.model.notificaicones.gateways.EmailNotificationRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.TipoPrestamoRepository;
import co.com.crediya.pragma.solicitudes.model.tipoprestamo.TipoPrestamo;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class EstadoUseCase {
    private final SolicitudRepository solicitudRepository;
    private final CambioEstadoRepository cambioEstadoRepository;
    private final SolicitudAprobadaRepository solicitudAprobadaRepository;
    private final EmailNotificationRepository emailNotificationRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final EstadoRepository estadoRepository;


    public Mono<Solicitud> updateEstado(CambioEstado cambioEstado) {
        return solicitudRepository.findSolicitudById(cambioEstado.getIdSolicitud())
                .switchIfEmpty(Mono.error(new SolicitudNotFoundException(cambioEstado.getIdSolicitud())))
                .flatMap(solicitud -> estadoRepository.findById(cambioEstado.getIdEstado())
                        .switchIfEmpty(Mono.error(new EstadoNotFoundException(cambioEstado.getIdEstado())))
                        .flatMap(estado -> {
                            solicitud.setIdEstado(cambioEstado.getIdEstado());
                            return cambioEstadoRepository.updateEstado(solicitud);
                        }))
                .flatMap(solicitud -> {
                    Mono<Void> saveAprobada = Mono.empty();
                    if (solicitud.getIdEstado().equals(2L)) {
                        SolicitudAprobada aprobada = new SolicitudAprobada(
                                LocalDateTime.now().toString(),
                                solicitud.getMonto()
                        );
                        saveAprobada = solicitudAprobadaRepository.saveSolicitudAprobada(aprobada).then();
                    }

                    return saveAprobada
                            .then(buildEmailNotification(solicitud)
                                    .flatMap(emailNotificationRepository::sendNotification)
                                    .thenReturn(solicitud));
                });
    }



    public Mono<EstadoValidacion> updateEstadoValidacion(EstadoValidacion estadoValidacion) {
        return solicitudRepository.findSolicitudById(estadoValidacion.getIdSolicitud())
                .switchIfEmpty(Mono.error(new SolicitudNotFoundException(estadoValidacion.getIdSolicitud())))
                .flatMap(solicitud -> {
                    EstadoEnum estadoEnum = EstadoEnum.fromStatusJson(estadoValidacion.getStatus());
                    solicitud.setIdEstado(estadoEnum.getId());
                    return cambioEstadoRepository.updateEstado(solicitud);
                })
                .flatMap(solicitud -> {
                    Mono<Void> saveAprobada = Mono.empty();
                    if (solicitud.getIdEstado().equals(2L)) {
                        SolicitudAprobada aprobada = new SolicitudAprobada(
                                LocalDateTime.now().toString(),
                                solicitud.getMonto()
                        );
                        saveAprobada = solicitudAprobadaRepository.saveSolicitudAprobada(aprobada).then();
                    }

                    return saveAprobada
                            .then(buildEmailNotification(solicitud, estadoValidacion)
                                    .flatMap(emailNotificationRepository::sendNotification)
                                    .thenReturn(estadoValidacion));
                });
    }



    private Mono<EmailNotification> buildEmailNotification(Solicitud solicitud) {
        return buildEmailNotification(solicitud, null);
    }

    private Mono<EmailNotification> buildEmailNotification(Solicitud solicitud, EstadoValidacion estadoValidacion) {
        return Mono.zip(
                tipoPrestamoRepository.findById(solicitud.getIdTipoPrestamo())
                        .switchIfEmpty(Mono.error(new TipoPrestamoNotFoundException(solicitud.getIdTipoPrestamo()))),
                estadoRepository.findById(solicitud.getIdEstado())
                        .switchIfEmpty(Mono.error(new EstadoNotFoundException(solicitud.getIdEstado())))
        ).map(tuple -> {
            TipoPrestamo tipoPrestamo = tuple.getT1();
            Estado estado = tuple.getT2();

            EstadoEnum estadoEnum = resolveEstadoEnum(estado.getNombreEstado());
            String customMessage = EstadoMessageFactory.buildMessage(estadoEnum, tipoPrestamo.getNombre(), estadoValidacion, estado.getNombreEstado());

            return EmailNotification.builder()
                    .requestId(solicitud.getIdSolicitud())
                    .status(estado.getNombreEstado())
                    .emailClient(solicitud.getEmail())
                    .identityDocument(solicitud.getDocumentoIdentidad())
                    .loanAmount(solicitud.getMonto())
                    .loanType(tipoPrestamo.getNombre())
                    .customMessage(customMessage)
                    .build();
        });
    }

    private EstadoEnum resolveEstadoEnum(String estadoNombre) {
        if (estadoNombre == null) {
            return EstadoEnum.PENDIENTE;
        }
        for (EstadoEnum e : EstadoEnum.values()) {
            if (e.getStatusJson().equalsIgnoreCase(estadoNombre) || e.getNombreBd().equalsIgnoreCase(estadoNombre)) {
                return e;
            }
        }
        return EstadoEnum.PENDIENTE;
    }
}
