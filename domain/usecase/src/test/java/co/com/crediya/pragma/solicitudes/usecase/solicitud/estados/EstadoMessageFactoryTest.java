package co.com.crediya.pragma.solicitudes.usecase.solicitud.estados;

import co.com.crediya.pragma.solicitudes.model.estado.EstadoEnum;
import co.com.crediya.pragma.solicitudes.model.notificaicones.EstadoValidacion;
import co.com.crediya.pragma.solicitudes.model.notificaicones.PaymentPlan;
import co.com.crediya.pragma.solicitudes.model.notificaicones.Totales;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstadoMessageFactoryTest {

    @Test
    void buildMessage_AprobadoWithoutEstadoValidacion_ReturnsSimpleMessage() {
        String result = EstadoMessageFactory.buildMessage(
                EstadoEnum.APROBADO,
                "Personal",
                null,
                "Aprobado"
        );

        assertTrue(result.contains("¡Felicitaciones!"));
        assertTrue(result.contains("Personal"));
        assertTrue(result.contains("aprobada"));
        assertTrue(result.contains("desembolso"));
    }

    @Test
    void buildMessage_AprobadoWithEstadoValidacion_ReturnsDetailedMessage() {
        EstadoValidacion estadoValidacion = EstadoValidacion.builder()
                .cuotaNueva(new BigDecimal("250000"))
                .plazoMeses(24)
                .interesMensual(new BigDecimal("0.02"))
                .capacidadDisponible(new BigDecimal("1000000"))
                .totales(Totales.builder()
                        .totalIntereses(new BigDecimal("500000"))
                        .totalPagado(new BigDecimal("6000000"))
                        .build())
                .paymentPlan(List.of(
                        PaymentPlan.builder()
                                .n(1)
                                .cuota(new BigDecimal("250000"))
                                .abonoCapital(new BigDecimal("200000"))
                                .interes(new BigDecimal("50000"))
                                .saldo(new BigDecimal("4800000"))
                                .build(),
                        PaymentPlan.builder()
                                .n(2)
                                .cuota(new BigDecimal("250000"))
                                .abonoCapital(new BigDecimal("204000"))
                                .interes(new BigDecimal("46000"))
                                .saldo(new BigDecimal("4596000"))
                                .build()
                ))
                .build();

        String result = EstadoMessageFactory.buildMessage(
                EstadoEnum.APROBADO,
                "Personal",
                estadoValidacion,
                "Aprobado"
        );

        assertTrue(result.contains("¡Felicitaciones!"));
        assertTrue(result.contains("Personal"));
        assertTrue(result.contains("DETALLES DEL PRÉSTAMO"));
        assertTrue(result.contains("$250000"));
        assertTrue(result.contains("24 meses"));
        assertTrue(result.contains("2%"));
        assertTrue(result.contains("$1000000"));
        assertTrue(result.contains("TOTALES"));
        assertTrue(result.contains("$500000"));
        assertTrue(result.contains("$6000000"));
        assertTrue(result.contains("PLAN DE PAGOS"));
        assertTrue(result.contains("Cuota 1"));
        assertTrue(result.contains("Cuota 2"));
    }

    @Test
    void buildMessage_AprobadoWithManyPaymentPlans_ShowsOnlyFirstFive() {
        List<PaymentPlan> paymentPlans = List.of(
                PaymentPlan.builder().n(1).cuota(new BigDecimal("250000")).abonoCapital(new BigDecimal("200000")).interes(new BigDecimal("50000")).saldo(new BigDecimal("4800000")).build(),
                PaymentPlan.builder().n(2).cuota(new BigDecimal("250000")).abonoCapital(new BigDecimal("204000")).interes(new BigDecimal("46000")).saldo(new BigDecimal("4596000")).build(),
                PaymentPlan.builder().n(3).cuota(new BigDecimal("250000")).abonoCapital(new BigDecimal("208080")).interes(new BigDecimal("41920")).saldo(new BigDecimal("4387920")).build(),
                PaymentPlan.builder().n(4).cuota(new BigDecimal("250000")).abonoCapital(new BigDecimal("212242")).interes(new BigDecimal("37758")).saldo(new BigDecimal("4175678")).build(),
                PaymentPlan.builder().n(5).cuota(new BigDecimal("250000")).abonoCapital(new BigDecimal("216486")).interes(new BigDecimal("33514")).saldo(new BigDecimal("3959192")).build(),
                PaymentPlan.builder().n(6).cuota(new BigDecimal("250000")).abonoCapital(new BigDecimal("220816")).interes(new BigDecimal("29184")).saldo(new BigDecimal("3738376")).build(),
                PaymentPlan.builder().n(7).cuota(new BigDecimal("250000")).abonoCapital(new BigDecimal("225232")).interes(new BigDecimal("24768")).saldo(new BigDecimal("3513144")).build()
        );

        EstadoValidacion estadoValidacion = EstadoValidacion.builder()
                .cuotaNueva(new BigDecimal("250000"))
                .plazoMeses(24)
                .interesMensual(new BigDecimal("0.02"))
                .capacidadDisponible(new BigDecimal("1000000"))
                .paymentPlan(paymentPlans)
                .build();

        String result = EstadoMessageFactory.buildMessage(
                EstadoEnum.APROBADO,
                "Personal",
                estadoValidacion,
                "Aprobado"
        );

        assertTrue(result.contains("Cuota 1"));
        assertTrue(result.contains("Cuota 2"));
        assertTrue(result.contains("Cuota 3"));
        assertTrue(result.contains("Cuota 4"));
        assertTrue(result.contains("Cuota 5"));
        assertTrue(result.contains("... y 2 cuotas más"));
        assertFalse(result.contains("Cuota 6"));
        assertFalse(result.contains("Cuota 7"));
    }

    @Test
    void buildMessage_Rechazado_ReturnsRejectedMessage() {
        String result = EstadoMessageFactory.buildMessage(
                EstadoEnum.RECHAZADO,
                "Hipotecario",
                null,
                "Rechazado"
        );

        assertTrue(result.contains("Lamentamos informarle"));
        assertTrue(result.contains("Hipotecario"));
        assertTrue(result.contains("no ha sido aprobada"));
        assertTrue(result.contains("contactar a nuestro equipo"));
    }

    @Test
    void buildMessage_Pendiente_ReturnsGenericMessage() {
        String result = EstadoMessageFactory.buildMessage(
                EstadoEnum.PENDIENTE,
                "Vehicular",
                null,
                "En Revisión"
        );

        assertTrue(result.contains("ha cambiado de estado"));
        assertTrue(result.contains("Vehicular"));
        assertTrue(result.contains("En Revisión"));
    }

    @Test
    void buildMessage_AprobadoWithNullTotales_ReturnsMessageWithoutTotales() {
        EstadoValidacion estadoValidacion = EstadoValidacion.builder()
                .cuotaNueva(new BigDecimal("250000"))
                .plazoMeses(24)
                .interesMensual(new BigDecimal("0.02"))
                .capacidadDisponible(new BigDecimal("1000000"))
                .totales(null)
                .paymentPlan(List.of(
                        PaymentPlan.builder()
                                .n(1)
                                .cuota(new BigDecimal("250000"))
                                .abonoCapital(new BigDecimal("200000"))
                                .interes(new BigDecimal("50000"))
                                .saldo(new BigDecimal("4800000"))
                                .build()
                ))
                .build();

        String result = EstadoMessageFactory.buildMessage(
                EstadoEnum.APROBADO,
                "Personal",
                estadoValidacion,
                "Aprobado"
        );

        assertTrue(result.contains("DETALLES DEL PRÉSTAMO"));
        assertTrue(result.contains("PLAN DE PAGOS"));
        assertFalse(result.contains("TOTALES"));
    }

    @Test
    void buildMessage_AprobadoWithEmptyPaymentPlan_ReturnsMessageWithoutPaymentPlan() {
        EstadoValidacion estadoValidacion = EstadoValidacion.builder()
                .cuotaNueva(new BigDecimal("250000"))
                .plazoMeses(24)
                .interesMensual(new BigDecimal("0.02"))
                .capacidadDisponible(new BigDecimal("1000000"))
                .totales(Totales.builder()
                        .totalIntereses(new BigDecimal("500000"))
                        .totalPagado(new BigDecimal("6000000"))
                        .build())
                .paymentPlan(List.of())
                .build();

        String result = EstadoMessageFactory.buildMessage(
                EstadoEnum.APROBADO,
                "Personal",
                estadoValidacion,
                "Aprobado"
        );

        assertTrue(result.contains("DETALLES DEL PRÉSTAMO"));
        assertTrue(result.contains("TOTALES"));
        assertTrue(result.contains("PLAN DE PAGOS"));
        assertFalse(result.contains("Cuota 1"));
    }

    @Test
    void buildMessage_AprobadoWithNullPaymentPlan_ReturnsMessageWithoutPaymentPlan() {
        EstadoValidacion estadoValidacion = EstadoValidacion.builder()
                .cuotaNueva(new BigDecimal("250000"))
                .plazoMeses(24)
                .interesMensual(new BigDecimal("0.02"))
                .capacidadDisponible(new BigDecimal("1000000"))
                .totales(Totales.builder()
                        .totalIntereses(new BigDecimal("500000"))
                        .totalPagado(new BigDecimal("6000000"))
                        .build())
                .paymentPlan(null)
                .build();

        String result = EstadoMessageFactory.buildMessage(
                EstadoEnum.APROBADO,
                "Personal",
                estadoValidacion,
                "Aprobado"
        );

        assertTrue(result.contains("DETALLES DEL PRÉSTAMO"));
        assertTrue(result.contains("TOTALES"));
        assertTrue(result.contains("PLAN DE PAGOS"));
        assertFalse(result.contains("Cuota 1"));
    }

    @Test
    void buildMessage_DifferentTipoPrestamo_IncludesCorrectName() {
        String result = EstadoMessageFactory.buildMessage(
                EstadoEnum.APROBADO,
                "Hipotecario",
                null,
                "Aprobado"
        );

        assertTrue(result.contains("Hipotecario"));
        assertFalse(result.contains("Personal"));
    }

    @Test
    void buildMessage_DifferentEstadoNombre_ShowsCorrectEstado() {
        String result = EstadoMessageFactory.buildMessage(
                EstadoEnum.PENDIENTE,
                "Personal",
                null,
                "En Proceso"
        );

        assertTrue(result.contains("En Proceso"));
    }
}
