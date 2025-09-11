package co.com.crediya.pragma.solicitudes.usecase.solicitud.estados;

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


    public Mono<EstadoValidacion> updateEstadoValidacion(EstadoValidacion estadoValidacion){
        return solicitudRepository.findSolicitudById(estadoValidacion.getIdSolicitud())
                .switchIfEmpty(Mono.error(new SolicitudNotFoundException(estadoValidacion.getIdSolicitud())))
                .flatMap(solicitud -> {
                    EstadoEnum estadoEnum = EstadoEnum.fromStatusJson(estadoValidacion.getStatus());
                    solicitud.setIdEstado(estadoEnum.getId());
                    
                    return cambioEstadoRepository.updateEstado(solicitud);
                })
                .flatMap(solicitud -> buildEmailNotificationFromValidacion(solicitud, estadoValidacion)
                        .flatMap(emailNotificationRepository::sendNotification)
                        .thenReturn(estadoValidacion));
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
            default:
                return "Su solicitud de " + tipoPrestamoNombre + " ha cambiado de estado a: " + estadoNombre;
        }
    }


    private Mono<EmailNotification> buildEmailNotificationFromValidacion(Solicitud solicitud, EstadoValidacion estadoValidacion) {
        return Mono.zip(
                tipoPrestamoRepository.findById(solicitud.getIdTipoPrestamo())
                        .switchIfEmpty(Mono.error(new TipoPrestamoNotFoundException(solicitud.getIdTipoPrestamo()))),
                estadoRepository.findById(solicitud.getIdEstado())
                        .switchIfEmpty(Mono.error(new EstadoNotFoundException(solicitud.getIdEstado())))
        ).map(tuple -> {
            TipoPrestamo tipoPrestamo = tuple.getT1();
            Estado estado = tuple.getT2();
            
            String customMessage = buildCustomMessageFromValidacion(estado.getNombreEstado(), tipoPrestamo.getNombre(), estadoValidacion);
            
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

    private String buildCustomMessageFromValidacion(String estadoNombre, String tipoPrestamoNombre, EstadoValidacion estadoValidacion) {
        switch (estadoNombre.toUpperCase()) {
            case "APROBADA":
                return buildApprovedMessageWithPaymentPlan(tipoPrestamoNombre, estadoValidacion);
            case "RECHAZADA":
                return "Lamentamos informarle que su solicitud de " + tipoPrestamoNombre + " no ha sido aprobada. Puede contactar a nuestro equipo para más información.";
            default:
                return "Su solicitud de " + tipoPrestamoNombre + " ha cambiado de estado a: " + estadoNombre;
        }
    }

    private String buildApprovedMessageWithPaymentPlan(String tipoPrestamoNombre, EstadoValidacion estadoValidacion) {
        StringBuilder message = new StringBuilder();
        message.append("¡Felicitaciones! Su solicitud de ").append(tipoPrestamoNombre).append(" ha sido aprobada.\n\n");

        message.append("DETALLES DEL PRÉSTAMO:\n");
        message.append("• Cuota mensual: $").append(estadoValidacion.getCuotaNueva()).append("\n");
        message.append("• Plazo: ").append(estadoValidacion.getPlazoMeses()).append(" meses\n");
        message.append("• Interés mensual: ").append(estadoValidacion.getInteresMensual().multiply(java.math.BigDecimal.valueOf(100))).append("%\n");
        message.append("• Capacidad disponible: $").append(estadoValidacion.getCapacidadDisponible()).append("\n\n");

        if (estadoValidacion.getTotales() != null) {
            message.append("TOTALES:\n");
            message.append("• Total intereses: $").append(estadoValidacion.getTotales().getTotalIntereses()).append("\n");
            message.append("• Total a pagar: $").append(estadoValidacion.getTotales().getTotalPagado()).append("\n\n");
        }

        message.append("PLAN DE PAGOS:\n");
        if (estadoValidacion.getPaymentPlan() != null && !estadoValidacion.getPaymentPlan().isEmpty()) {
            for (int i = 0; i < Math.min(estadoValidacion.getPaymentPlan().size(), 5); i++) {
                var cuota = estadoValidacion.getPaymentPlan().get(i);
                message.append("Cuota ").append(cuota.getN()).append(": $").append(cuota.getCuota())
                       .append(" (Capital: $").append(cuota.getAbonoCapital())
                       .append(", Interés: $").append(cuota.getInteres())
                       .append(", Saldo: $").append(cuota.getSaldo()).append(")\n");
            }
            if (estadoValidacion.getPaymentPlan().size() > 5) {
                message.append("... y ").append(estadoValidacion.getPaymentPlan().size() - 5).append(" cuotas más\n");
            }
        }

        message.append("\nPronto recibirá más información sobre el desembolso.");

        return message.toString();
    }
}
