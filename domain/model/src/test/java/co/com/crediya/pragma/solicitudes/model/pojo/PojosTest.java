package co.com.crediya.pragma.solicitudes.model.pojo;

import co.com.crediya.pragma.solicitudes.model.auth.UserValidateInfo;
import co.com.crediya.pragma.solicitudes.model.capacidad.ValidacionCapacidad;
import co.com.crediya.pragma.solicitudes.model.notificaicones.EstadoValidacion;
import co.com.crediya.pragma.solicitudes.model.notificaicones.PaymentPlan;
import co.com.crediya.pragma.solicitudes.model.notificaicones.Totales;
import co.com.crediya.pragma.solicitudes.model.solicitud.PrestamoAprobado;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PojosTest {

    @Test
    void validacionCapacidad_builderAndSetters() {
        var vc = ValidacionCapacidad.builder()
                .salarioBase(new BigDecimal("10000"))
                .monto(new BigDecimal("5000"))
                .tasaInteres(5)
                .plazo(12)
                .prestamosAprobados(List.of())
                .email("a@b.com")
                .idSolicitud(1L)
                .build();

        assertEquals(new BigDecimal("10000"), vc.getSalarioBase());
        assertEquals(new BigDecimal("5000"), vc.getMonto());
        assertEquals(5, vc.getTasaInteres());
        assertEquals(12, vc.getPlazo());
        assertEquals("a@b.com", vc.getEmail());
        assertEquals(1L, vc.getIdSolicitud());
    }

    @Test
    void paymentPlan_and_totales_builders() {
        var pp = PaymentPlan.builder()
                .n(1).cuota(new BigDecimal("100")).interes(new BigDecimal("10"))
                .abonoCapital(new BigDecimal("90")).saldo(new BigDecimal("900"))
                .build();
        assertEquals(1, pp.getN());
        assertEquals(new BigDecimal("100"), pp.getCuota());

        var tot = Totales.builder().totalIntereses(new BigDecimal("200")).totalPagado(new BigDecimal("1200")).build();
        assertEquals(new BigDecimal("200"), tot.getTotalIntereses());
        assertEquals(new BigDecimal("1200"), tot.getTotalPagado());
    }

    @Test
    void prestamoAprobado_builder() {
        var pa = PrestamoAprobado.builder().monto(new BigDecimal("1000")).plazo(6).tasaInteres(new BigDecimal("0.02")).build();
        assertEquals(new BigDecimal("1000"), pa.getMonto());
        assertEquals(6, pa.getPlazo());
        assertEquals(new BigDecimal("0.02"), pa.getTasaInteres());
    }

    @Test
    void estadoValidacion_builder() {
        var now = LocalDateTime.now();
        var ev = EstadoValidacion.builder()
                .idSolicitud(5L).status("APROBADA").statusCode("200")
                .email("e@test.com").cuotaNueva(new BigDecimal("100"))
                .capacidadDisponible(new BigDecimal("300")).interesMensual(new BigDecimal("0.02"))
                .plazoMeses(12)
                .paymentPlan(List.of())
                .totales(Totales.builder().totalIntereses(new BigDecimal("1")).totalPagado(new BigDecimal("2")).build())
                .decidedAt(now)
                .build();

        assertEquals(5L, ev.getIdSolicitud());
        assertEquals("APROBADA", ev.getStatus());
        assertEquals("200", ev.getStatusCode());
        assertEquals("e@test.com", ev.getEmail());
        assertEquals(new BigDecimal("100"), ev.getCuotaNueva());
        assertEquals(new BigDecimal("300"), ev.getCapacidadDisponible());
        assertEquals(new BigDecimal("0.02"), ev.getInteresMensual());
        assertEquals(12, ev.getPlazoMeses());
        assertNotNull(ev.getPaymentPlan());
        assertNotNull(ev.getTotales());
        assertEquals(now, ev.getDecidedAt());
    }

    @Test
    void userValidateInfo_recordBuilder() {
        var info = UserValidateInfo.builder().exists(true).baseSalary(new BigDecimal("8000")).build();
        assertTrue(info.exists());
        assertEquals(new BigDecimal("8000"), info.baseSalary());
    }
}


