package co.com.crediya.pragma.solicitudes.usecase.solicitud.estados;

import co.com.crediya.pragma.solicitudes.model.estado.Estado;
import co.com.crediya.pragma.solicitudes.model.estado.gateways.EstadoRepository;
import co.com.crediya.pragma.solicitudes.model.exception.EstadoNotFoundException;
import co.com.crediya.pragma.solicitudes.model.exception.SolicitudNotFoundException;
import co.com.crediya.pragma.solicitudes.model.exception.TipoPrestamoNotFoundException;
import co.com.crediya.pragma.solicitudes.model.notificaicones.CambioEstado;
import co.com.crediya.pragma.solicitudes.model.notificaicones.EmailNotification;
import co.com.crediya.pragma.solicitudes.model.notificaicones.gateways.CambioEstadoRepository;
import co.com.crediya.pragma.solicitudes.model.notificaicones.gateways.EmailNotificationRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.TipoPrestamoRepository;
import co.com.crediya.pragma.solicitudes.model.tipoprestamo.TipoPrestamo;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class EstadoUseCase {
    private final SolicitudRepository solicitudRepository;
    private final CambioEstadoRepository cambioEstadoRepository;
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
                .flatMap(solicitud -> buildEmailNotification(solicitud)
                        .flatMap(emailNotificationRepository::sendNotification)
                        .thenReturn(solicitud));
    }


    private Mono<EmailNotification> buildEmailNotification(Solicitud solicitud) {
        return Mono.zip(
                tipoPrestamoRepository.findById(solicitud.getIdTipoPrestamo())
                        .switchIfEmpty(Mono.error(new TipoPrestamoNotFoundException(solicitud.getIdTipoPrestamo()))),
                estadoRepository.findById(solicitud.getIdEstado())
                        .switchIfEmpty(Mono.error(new EstadoNotFoundException(solicitud.getIdEstado())))
        ).map(tuple -> {
            TipoPrestamo tipoPrestamo = tuple.getT1();
            Estado estado = tuple.getT2();
            
            return EmailNotification.builder()
                    .requestId(solicitud.getIdSolicitud())
                    .status(estado.getNombreEstado())
                    .emailClient(solicitud.getEmail())
                    .identityDocument(solicitud.getDocumentoIdentidad())
                    .loanAmount(solicitud.getMonto())
                    .loanType(tipoPrestamo.getNombre())
                    .customMessage(buildCustomMessage(estado.getNombreEstado(), tipoPrestamo.getNombre()))
                    .build();
        });
    }


    private String buildCustomMessage(String estadoNombre, String tipoPrestamoNombre) {
        switch (estadoNombre.toUpperCase()) {
            case "APROBADO":
                return "¡Felicitaciones! Su solicitud de " + tipoPrestamoNombre + " ha sido aprobada. Pronto recibirá más información sobre el desembolso.";
            case "RECHAZADO":
                return "Lamentamos informarle que su solicitud de " + tipoPrestamoNombre + " no ha sido aprobada. Puede contactar a nuestro equipo para más información.";
            case "PENDIENTE":
                return "Su solicitud de " + tipoPrestamoNombre + " está siendo revisada. Le notificaremos el resultado en breve.";
            default:
                return "Su solicitud de " + tipoPrestamoNombre + " ha cambiado de estado a: " + estadoNombre;
        }
    }
}
