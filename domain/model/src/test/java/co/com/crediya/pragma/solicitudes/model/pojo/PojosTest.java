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
        assertNotNull(vc.getPrestamosAprobados());
    }

    @Test
    void validacionCapacidad_noArgsConstructor() {
        var vc = new ValidacionCapacidad();
        assertNull(vc.getSalarioBase());
        assertNull(vc.getMonto());
        assertNull(vc.getTasaInteres());
        assertNull(vc.getPlazo());
        assertNull(vc.getPrestamosAprobados());
        assertNull(vc.getEmail());
        assertNull(vc.getIdSolicitud());
    }

    @Test
    void validacionCapacidad_allArgsConstructor() {
        var prestamos = List.of(PrestamoAprobado.builder().monto(new BigDecimal("1000")).plazo(6).tasaInteres(new BigDecimal("0.02")).build());
        var vc = new ValidacionCapacidad(
                new BigDecimal("15000"),
                new BigDecimal("8000"),
                8,
                18,
                prestamos,
                "test@example.com",
                2L
        );

        assertEquals(new BigDecimal("15000"), vc.getSalarioBase());
        assertEquals(new BigDecimal("8000"), vc.getMonto());
        assertEquals(8, vc.getTasaInteres());
        assertEquals(18, vc.getPlazo());
        assertEquals(prestamos, vc.getPrestamosAprobados());
        assertEquals("test@example.com", vc.getEmail());
        assertEquals(2L, vc.getIdSolicitud());
    }

    @Test
    void validacionCapacidad_settersAndGetters() {
        var vc = new ValidacionCapacidad();
        var prestamos = List.of(PrestamoAprobado.builder().monto(new BigDecimal("2000")).plazo(12).tasaInteres(new BigDecimal("0.03")).build());
        
        vc.setSalarioBase(new BigDecimal("20000"));
        vc.setMonto(new BigDecimal("10000"));
        vc.setTasaInteres(10);
        vc.setPlazo(24);
        vc.setPrestamosAprobados(prestamos);
        vc.setEmail("new@example.com");
        vc.setIdSolicitud(3L);

        assertEquals(new BigDecimal("20000"), vc.getSalarioBase());
        assertEquals(new BigDecimal("10000"), vc.getMonto());
        assertEquals(10, vc.getTasaInteres());
        assertEquals(24, vc.getPlazo());
        assertEquals(prestamos, vc.getPrestamosAprobados());
        assertEquals("new@example.com", vc.getEmail());
        assertEquals(3L, vc.getIdSolicitud());
    }

    @Test
    void validacionCapacidad_toBuilder() {
        var original = ValidacionCapacidad.builder()
                .salarioBase(new BigDecimal("10000"))
                .monto(new BigDecimal("5000"))
                .tasaInteres(5)
                .plazo(12)
                .prestamosAprobados(List.of())
                .email("a@b.com")
                .idSolicitud(1L)
                .build();

        var modified = original.toBuilder()
                .salarioBase(new BigDecimal("12000"))
                .tasaInteres(6)
                .build();

        assertEquals(new BigDecimal("12000"), modified.getSalarioBase());
        assertEquals(new BigDecimal("5000"), modified.getMonto());
        assertEquals(6, modified.getTasaInteres());
        assertEquals(12, modified.getPlazo());
        assertEquals("a@b.com", modified.getEmail());
        assertEquals(1L, modified.getIdSolicitud());
    }

    @Test
    void paymentPlan_and_totales_builders() {
        var pp = PaymentPlan.builder()
                .n(1).cuota(new BigDecimal("100")).interes(new BigDecimal("10"))
                .abonoCapital(new BigDecimal("90")).saldo(new BigDecimal("900"))
                .build();
        assertEquals(1, pp.getN());
        assertEquals(new BigDecimal("100"), pp.getCuota());
        assertEquals(new BigDecimal("10"), pp.getInteres());
        assertEquals(new BigDecimal("90"), pp.getAbonoCapital());
        assertEquals(new BigDecimal("900"), pp.getSaldo());

        var tot = Totales.builder().totalIntereses(new BigDecimal("200")).totalPagado(new BigDecimal("1200")).build();
        assertEquals(new BigDecimal("200"), tot.getTotalIntereses());
        assertEquals(new BigDecimal("1200"), tot.getTotalPagado());
    }

    @Test
    void paymentPlan_noArgsConstructor() {
        var pp = new PaymentPlan();
        assertNull(pp.getN());
        assertNull(pp.getCuota());
        assertNull(pp.getInteres());
        assertNull(pp.getAbonoCapital());
        assertNull(pp.getSaldo());
    }

    @Test
    void paymentPlan_allArgsConstructor() {
        var pp = new PaymentPlan(2, new BigDecimal("200"), new BigDecimal("20"), new BigDecimal("180"), new BigDecimal("800"));
        assertEquals(2, pp.getN());
        assertEquals(new BigDecimal("200"), pp.getCuota());
        assertEquals(new BigDecimal("20"), pp.getInteres());
        assertEquals(new BigDecimal("180"), pp.getAbonoCapital());
        assertEquals(new BigDecimal("800"), pp.getSaldo());
    }

    @Test
    void paymentPlan_settersAndGetters() {
        var pp = new PaymentPlan();
        pp.setN(3);
        pp.setCuota(new BigDecimal("300"));
        pp.setInteres(new BigDecimal("30"));
        pp.setAbonoCapital(new BigDecimal("270"));
        pp.setSaldo(new BigDecimal("700"));

        assertEquals(3, pp.getN());
        assertEquals(new BigDecimal("300"), pp.getCuota());
        assertEquals(new BigDecimal("30"), pp.getInteres());
        assertEquals(new BigDecimal("270"), pp.getAbonoCapital());
        assertEquals(new BigDecimal("700"), pp.getSaldo());
    }

    @Test
    void paymentPlan_toBuilder() {
        var original = PaymentPlan.builder()
                .n(1).cuota(new BigDecimal("100")).interes(new BigDecimal("10"))
                .abonoCapital(new BigDecimal("90")).saldo(new BigDecimal("900"))
                .build();

        var modified = original.toBuilder()
                .n(2)
                .cuota(new BigDecimal("200"))
                .build();

        assertEquals(2, modified.getN());
        assertEquals(new BigDecimal("200"), modified.getCuota());
        assertEquals(new BigDecimal("10"), modified.getInteres());
        assertEquals(new BigDecimal("90"), modified.getAbonoCapital());
        assertEquals(new BigDecimal("900"), modified.getSaldo());
    }

    @Test
    void totales_noArgsConstructor() {
        var tot = new Totales();
        assertNull(tot.getTotalIntereses());
        assertNull(tot.getTotalPagado());
    }

    @Test
    void totales_allArgsConstructor() {
        var tot = new Totales(new BigDecimal("500"), new BigDecimal("2500"));
        assertEquals(new BigDecimal("500"), tot.getTotalIntereses());
        assertEquals(new BigDecimal("2500"), tot.getTotalPagado());
    }

    @Test
    void totales_settersAndGetters() {
        var tot = new Totales();
        tot.setTotalIntereses(new BigDecimal("600"));
        tot.setTotalPagado(new BigDecimal("3000"));

        assertEquals(new BigDecimal("600"), tot.getTotalIntereses());
        assertEquals(new BigDecimal("3000"), tot.getTotalPagado());
    }

    @Test
    void totales_toBuilder() {
        var original = Totales.builder()
                .totalIntereses(new BigDecimal("200"))
                .totalPagado(new BigDecimal("1200"))
                .build();

        var modified = original.toBuilder()
                .totalIntereses(new BigDecimal("300"))
                .build();

        assertEquals(new BigDecimal("300"), modified.getTotalIntereses());
        assertEquals(new BigDecimal("1200"), modified.getTotalPagado());
    }

    @Test
    void prestamoAprobado_builder() {
        var pa = PrestamoAprobado.builder().monto(new BigDecimal("1000")).plazo(6).tasaInteres(new BigDecimal("0.02")).build();
        assertEquals(new BigDecimal("1000"), pa.getMonto());
        assertEquals(6, pa.getPlazo());
        assertEquals(new BigDecimal("0.02"), pa.getTasaInteres());
    }

    @Test
    void prestamoAprobado_noArgsConstructor() {
        var pa = new PrestamoAprobado();
        assertNull(pa.getMonto());
        assertNull(pa.getPlazo());
        assertNull(pa.getTasaInteres());
    }

    @Test
    void prestamoAprobado_allArgsConstructor() {
        var pa = new PrestamoAprobado(new BigDecimal("2000"), 12, new BigDecimal("0.03"));
        assertEquals(new BigDecimal("2000"), pa.getMonto());
        assertEquals(12, pa.getPlazo());
        assertEquals(new BigDecimal("0.03"), pa.getTasaInteres());
    }

    @Test
    void prestamoAprobado_settersAndGetters() {
        var pa = new PrestamoAprobado();
        pa.setMonto(new BigDecimal("3000"));
        pa.setPlazo(18);
        pa.setTasaInteres(new BigDecimal("0.04"));

        assertEquals(new BigDecimal("3000"), pa.getMonto());
        assertEquals(18, pa.getPlazo());
        assertEquals(new BigDecimal("0.04"), pa.getTasaInteres());
    }

    @Test
    void prestamoAprobado_toBuilder() {
        var original = PrestamoAprobado.builder()
                .monto(new BigDecimal("1000"))
                .plazo(6)
                .tasaInteres(new BigDecimal("0.02"))
                .build();

        var modified = original.toBuilder()
                .monto(new BigDecimal("1500"))
                .build();

        assertEquals(new BigDecimal("1500"), modified.getMonto());
        assertEquals(6, modified.getPlazo());
        assertEquals(new BigDecimal("0.02"), modified.getTasaInteres());
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
    void estadoValidacion_noArgsConstructor() {
        var ev = new EstadoValidacion();
        assertNull(ev.getIdSolicitud());
        assertNull(ev.getStatus());
        assertNull(ev.getStatusCode());
        assertNull(ev.getEmail());
        assertNull(ev.getCuotaNueva());
        assertNull(ev.getCapacidadDisponible());
        assertNull(ev.getInteresMensual());
        assertNull(ev.getPlazoMeses());
        assertNull(ev.getPaymentPlan());
        assertNull(ev.getTotales());
        assertNull(ev.getDecidedAt());
    }

    @Test
    void estadoValidacion_allArgsConstructor() {
        var now = LocalDateTime.now();
        var paymentPlan = List.of(PaymentPlan.builder().n(1).cuota(new BigDecimal("100")).build());
        var totales = Totales.builder().totalIntereses(new BigDecimal("50")).totalPagado(new BigDecimal("500")).build();
        
        var ev = new EstadoValidacion(
                6L,
                "RECHAZADA",
                "400",
                "test@example.com",
                new BigDecimal("200"),
                new BigDecimal("400"),
                new BigDecimal("0.03"),
                18,
                paymentPlan,
                totales,
                now
        );

        assertEquals(6L, ev.getIdSolicitud());
        assertEquals("RECHAZADA", ev.getStatus());
        assertEquals("400", ev.getStatusCode());
        assertEquals("test@example.com", ev.getEmail());
        assertEquals(new BigDecimal("200"), ev.getCuotaNueva());
        assertEquals(new BigDecimal("400"), ev.getCapacidadDisponible());
        assertEquals(new BigDecimal("0.03"), ev.getInteresMensual());
        assertEquals(18, ev.getPlazoMeses());
        assertEquals(paymentPlan, ev.getPaymentPlan());
        assertEquals(totales, ev.getTotales());
        assertEquals(now, ev.getDecidedAt());
    }

    @Test
    void estadoValidacion_settersAndGetters() {
        var ev = new EstadoValidacion();
        var now = LocalDateTime.now();
        var paymentPlan = List.of(PaymentPlan.builder().n(2).cuota(new BigDecimal("150")).build());
        var totales = Totales.builder().totalIntereses(new BigDecimal("75")).totalPagado(new BigDecimal("750")).build();
        
        ev.setIdSolicitud(7L);
        ev.setStatus("PENDIENTE");
        ev.setStatusCode("202");
        ev.setEmail("new@example.com");
        ev.setCuotaNueva(new BigDecimal("250"));
        ev.setCapacidadDisponible(new BigDecimal("500"));
        ev.setInteresMensual(new BigDecimal("0.04"));
        ev.setPlazoMeses(24);
        ev.setPaymentPlan(paymentPlan);
        ev.setTotales(totales);
        ev.setDecidedAt(now);

        assertEquals(7L, ev.getIdSolicitud());
        assertEquals("PENDIENTE", ev.getStatus());
        assertEquals("202", ev.getStatusCode());
        assertEquals("new@example.com", ev.getEmail());
        assertEquals(new BigDecimal("250"), ev.getCuotaNueva());
        assertEquals(new BigDecimal("500"), ev.getCapacidadDisponible());
        assertEquals(new BigDecimal("0.04"), ev.getInteresMensual());
        assertEquals(24, ev.getPlazoMeses());
        assertEquals(paymentPlan, ev.getPaymentPlan());
        assertEquals(totales, ev.getTotales());
        assertEquals(now, ev.getDecidedAt());
    }

    @Test
    void estadoValidacion_toBuilder() {
        var now = LocalDateTime.now();
        var original = EstadoValidacion.builder()
                .idSolicitud(5L)
                .status("APROBADA")
                .statusCode("200")
                .email("e@test.com")
                .cuotaNueva(new BigDecimal("100"))
                .capacidadDisponible(new BigDecimal("300"))
                .interesMensual(new BigDecimal("0.02"))
                .plazoMeses(12)
                .paymentPlan(List.of())
                .totales(Totales.builder().totalIntereses(new BigDecimal("1")).totalPagado(new BigDecimal("2")).build())
                .decidedAt(now)
                .build();

        var modified = original.toBuilder()
                .status("RECHAZADA")
                .statusCode("400")
                .build();

        assertEquals(5L, modified.getIdSolicitud());
        assertEquals("RECHAZADA", modified.getStatus());
        assertEquals("400", modified.getStatusCode());
        assertEquals("e@test.com", modified.getEmail());
        assertEquals(new BigDecimal("100"), modified.getCuotaNueva());
        assertEquals(new BigDecimal("300"), modified.getCapacidadDisponible());
        assertEquals(new BigDecimal("0.02"), modified.getInteresMensual());
        assertEquals(12, modified.getPlazoMeses());
        assertNotNull(modified.getPaymentPlan());
        assertNotNull(modified.getTotales());
        assertEquals(now, modified.getDecidedAt());
    }

    @Test
    void userValidateInfo_recordBuilder() {
        var info = UserValidateInfo.builder().exists(true).baseSalary(new BigDecimal("8000")).build();
        assertTrue(info.exists());
        assertEquals(new BigDecimal("8000"), info.baseSalary());
    }
}


