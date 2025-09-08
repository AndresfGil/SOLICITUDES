package co.com.crediya.pragma.solicitudes.model.solicitud;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudConUsuarioResponseTest {

    @Test
    void testSolicitudConUsuarioResponseBuilder() {
        SolicitudConUsuarioResponse response = SolicitudConUsuarioResponse.builder()
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .nombreTipoPrestamo("Personal")
                .estadoSolicitud("Pendiente")
                .deudaTotalMensual(250000L)
                .nombre("Juan Pérez")
                .salarioBase(3000000L)
                .build();

        assertEquals(new BigDecimal("5000000"), response.getMonto());
        assertEquals(24, response.getPlazo());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Personal", response.getNombreTipoPrestamo());
        assertEquals("Pendiente", response.getEstadoSolicitud());
        assertEquals(250000L, response.getDeudaTotalMensual());
        assertEquals("Juan Pérez", response.getNombre());
        assertEquals(3000000L, response.getSalarioBase());
    }

    @Test
    void testSolicitudConUsuarioResponseNoArgsConstructor() {
        SolicitudConUsuarioResponse response = new SolicitudConUsuarioResponse();
        
        assertNull(response.getMonto());
        assertNull(response.getPlazo());
        assertNull(response.getEmail());
        assertNull(response.getNombreTipoPrestamo());
        assertNull(response.getEstadoSolicitud());
        assertNull(response.getDeudaTotalMensual());
        assertNull(response.getNombre());
        assertNull(response.getSalarioBase());
    }

    @Test
    void testSolicitudConUsuarioResponseAllArgsConstructor() {
        SolicitudConUsuarioResponse response = new SolicitudConUsuarioResponse(
                new BigDecimal("3000000"),
                12,
                "user@example.com",
                "Hipotecario",
                "Aprobado",
                150000L,
                "María García",
                4000000L
        );

        assertEquals(new BigDecimal("3000000"), response.getMonto());
        assertEquals(12, response.getPlazo());
        assertEquals("user@example.com", response.getEmail());
        assertEquals("Hipotecario", response.getNombreTipoPrestamo());
        assertEquals("Aprobado", response.getEstadoSolicitud());
        assertEquals(150000L, response.getDeudaTotalMensual());
        assertEquals("María García", response.getNombre());
        assertEquals(4000000L, response.getSalarioBase());
    }

    @Test
    void testSolicitudConUsuarioResponseSettersAndGetters() {
        SolicitudConUsuarioResponse response = new SolicitudConUsuarioResponse();
        
        response.setMonto(new BigDecimal("7000000"));
        response.setPlazo(36);
        response.setEmail("newuser@example.com");
        response.setNombreTipoPrestamo("Vehicular");
        response.setEstadoSolicitud("En Revisión");
        response.setDeudaTotalMensual(350000L);
        response.setNombre("Carlos López");
        response.setSalarioBase(5000000L);

        assertEquals(new BigDecimal("7000000"), response.getMonto());
        assertEquals(36, response.getPlazo());
        assertEquals("newuser@example.com", response.getEmail());
        assertEquals("Vehicular", response.getNombreTipoPrestamo());
        assertEquals("En Revisión", response.getEstadoSolicitud());
        assertEquals(350000L, response.getDeudaTotalMensual());
        assertEquals("Carlos López", response.getNombre());
        assertEquals(5000000L, response.getSalarioBase());
    }

    @Test
    void testFromSolicitudConUsuario() {
        SolicitudConUsuario solicitudConUsuario = SolicitudConUsuario.builder()
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .nombreTipoPrestamo("Personal")
                .estadoSolicitud("Pendiente")
                .nombre("Juan Pérez")
                .salarioBase(3000000L)
                .build();

        SolicitudConUsuarioResponse response = SolicitudConUsuarioResponse.fromSolicitudConUsuario(solicitudConUsuario);

        assertEquals(new BigDecimal("5000000"), response.getMonto());
        assertEquals(24, response.getPlazo());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Personal", response.getNombreTipoPrestamo());
        assertEquals("Pendiente", response.getEstadoSolicitud());
        assertNull(response.getDeudaTotalMensual());
        assertEquals("Juan Pérez", response.getNombre());
        assertEquals(3000000L, response.getSalarioBase());
    }

    @Test
    void testFromSolicitudConUsuarioWithNullValues() {
        SolicitudConUsuario solicitudConUsuario = SolicitudConUsuario.builder()
                .monto(null)
                .plazo(null)
                .email(null)
                .nombreTipoPrestamo(null)
                .estadoSolicitud(null)
                .nombre(null)
                .salarioBase(null)
                .build();

        SolicitudConUsuarioResponse response = SolicitudConUsuarioResponse.fromSolicitudConUsuario(solicitudConUsuario);

        assertNull(response.getMonto());
        assertNull(response.getPlazo());
        assertNull(response.getEmail());
        assertNull(response.getNombreTipoPrestamo());
        assertNull(response.getEstadoSolicitud());
        assertNull(response.getDeudaTotalMensual());
        assertNull(response.getNombre());
        assertNull(response.getSalarioBase());
    }
}
