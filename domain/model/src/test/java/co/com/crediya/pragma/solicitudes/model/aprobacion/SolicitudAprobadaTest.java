package co.com.crediya.pragma.solicitudes.model.aprobacion;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudAprobadaTest {

    @Test
    void testSolicitudAprobadaBuilder() {
        SolicitudAprobada solicitud = SolicitudAprobada.builder()
                .metrica("APROBADA")
                .monto(new BigDecimal("5000000"))
                .build();

        assertEquals("APROBADA", solicitud.getMetrica());
        assertEquals(new BigDecimal("5000000"), solicitud.getMonto());
    }

    @Test
    void testSolicitudAprobadaNoArgsConstructor() {
        SolicitudAprobada solicitud = new SolicitudAprobada();
        
        assertNull(solicitud.getMetrica());
        assertNull(solicitud.getMonto());
    }

    @Test
    void testSolicitudAprobadaAllArgsConstructor() {
        SolicitudAprobada solicitud = new SolicitudAprobada(
                "RECHAZADA",
                new BigDecimal("3000000")
        );

        assertEquals("RECHAZADA", solicitud.getMetrica());
        assertEquals(new BigDecimal("3000000"), solicitud.getMonto());
    }

    @Test
    void testSolicitudAprobadaSettersAndGetters() {
        SolicitudAprobada solicitud = new SolicitudAprobada();
        
        solicitud.setMetrica("PENDIENTE");
        solicitud.setMonto(new BigDecimal("7000000"));

        assertEquals("PENDIENTE", solicitud.getMetrica());
        assertEquals(new BigDecimal("7000000"), solicitud.getMonto());
    }

    @Test
    void testSolicitudAprobadaToBuilder() {
        SolicitudAprobada original = SolicitudAprobada.builder()
                .metrica("APROBADA")
                .monto(new BigDecimal("5000000"))
                .build();

        SolicitudAprobada modified = original.toBuilder()
                .monto(new BigDecimal("6000000"))
                .build();

        assertEquals("APROBADA", modified.getMetrica());
        assertEquals(new BigDecimal("6000000"), modified.getMonto());
    }
}
