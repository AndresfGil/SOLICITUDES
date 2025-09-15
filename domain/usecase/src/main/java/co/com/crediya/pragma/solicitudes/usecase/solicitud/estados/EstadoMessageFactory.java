package co.com.crediya.pragma.solicitudes.usecase.solicitud.estados;

import co.com.crediya.pragma.solicitudes.model.estado.EstadoEnum;
import co.com.crediya.pragma.solicitudes.model.notificaicones.EstadoValidacion;

final class EstadoMessageFactory {

    private static final String APPROVED_SIMPLE_TEMPLATE = "¡Felicitaciones! Su solicitud de %s ha sido aprobada. Pronto recibirá más información sobre el desembolso.";
    private static final String REJECTED_TEMPLATE = "Lamentamos informarle que su solicitud de %s no ha sido aprobada. Puede contactar a nuestro equipo para más información.";
    private static final String GENERIC_TEMPLATE = "Su solicitud de %s ha cambiado de estado a: %s";

    private EstadoMessageFactory() { }

    static String buildMessage(EstadoEnum estadoEnum, String tipoPrestamoNombre, EstadoValidacion estadoValidacion, String estadoNombreOriginal) {
        switch (estadoEnum) {
            case APROBADO:
                if (estadoValidacion != null) {
                    return buildApprovedMessageWithPaymentPlan(tipoPrestamoNombre, estadoValidacion);
                }
                return String.format(APPROVED_SIMPLE_TEMPLATE, tipoPrestamoNombre);
            case RECHAZADO:
                return String.format(REJECTED_TEMPLATE, tipoPrestamoNombre);
            default:
                return String.format(GENERIC_TEMPLATE, tipoPrestamoNombre, estadoNombreOriginal);
        }
    }

    private static String buildApprovedMessageWithPaymentPlan(String tipoPrestamoNombre, EstadoValidacion estadoValidacion) {
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


