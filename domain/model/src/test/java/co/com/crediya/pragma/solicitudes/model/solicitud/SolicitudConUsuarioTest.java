package co.com.crediya.pragma.solicitudes.model.solicitud;

import co.com.crediya.pragma.solicitudes.model.page.SolicitudFieldsPage;
import co.com.crediya.pragma.solicitudes.model.page.UsersForPageResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudConUsuarioTest {

    @Test
    void testSolicitudConUsuarioBuilder() {
        SolicitudConUsuario solicitud = SolicitudConUsuario.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(1L)
                .idTipoPrestamo(1L)
                .nombreTipoPrestamo("Personal")
                .estadoSolicitud("Pendiente")
                .tasaInteres(15)
                .nombre("Juan Pérez")
                .salarioBase(3000000L)
                .build();

        assertEquals(1L, solicitud.getIdSolicitud());
        assertEquals(new BigDecimal("5000000"), solicitud.getMonto());
        assertEquals(24, solicitud.getPlazo());
        assertEquals("test@example.com", solicitud.getEmail());
        assertEquals("12345678", solicitud.getDocumentoIdentidad());
        assertEquals(1L, solicitud.getIdEstado());
        assertEquals(1L, solicitud.getIdTipoPrestamo());
        assertEquals("Personal", solicitud.getNombreTipoPrestamo());
        assertEquals("Pendiente", solicitud.getEstadoSolicitud());
        assertEquals(15, solicitud.getTasaInteres());
        assertEquals("Juan Pérez", solicitud.getNombre());
        assertEquals(3000000L, solicitud.getSalarioBase());
    }

    @Test
    void testSolicitudConUsuarioNoArgsConstructor() {
        SolicitudConUsuario solicitud = new SolicitudConUsuario();
        
        assertNull(solicitud.getIdSolicitud());
        assertNull(solicitud.getMonto());
        assertNull(solicitud.getPlazo());
        assertNull(solicitud.getEmail());
        assertNull(solicitud.getDocumentoIdentidad());
        assertNull(solicitud.getIdEstado());
        assertNull(solicitud.getIdTipoPrestamo());
        assertNull(solicitud.getNombreTipoPrestamo());
        assertNull(solicitud.getEstadoSolicitud());
        assertNull(solicitud.getTasaInteres());
        assertNull(solicitud.getNombre());
        assertNull(solicitud.getSalarioBase());
    }

    @Test
    void testSolicitudConUsuarioAllArgsConstructor() {
        SolicitudConUsuario solicitud = new SolicitudConUsuario(
                2L,
                new BigDecimal("3000000"),
                12,
                "user@example.com",
                "87654321",
                2L,
                2L,
                "Hipotecario",
                "Aprobado",
                8,
                "María García",
                4000000L
        );

        assertEquals(2L, solicitud.getIdSolicitud());
        assertEquals(new BigDecimal("3000000"), solicitud.getMonto());
        assertEquals(12, solicitud.getPlazo());
        assertEquals("user@example.com", solicitud.getEmail());
        assertEquals("87654321", solicitud.getDocumentoIdentidad());
        assertEquals(2L, solicitud.getIdEstado());
        assertEquals(2L, solicitud.getIdTipoPrestamo());
        assertEquals("Hipotecario", solicitud.getNombreTipoPrestamo());
        assertEquals("Aprobado", solicitud.getEstadoSolicitud());
        assertEquals(8, solicitud.getTasaInteres());
        assertEquals("María García", solicitud.getNombre());
        assertEquals(4000000L, solicitud.getSalarioBase());
    }

    @Test
    void testFromSolicitudFieldsAndUserWithUserInfo() {
        SolicitudFieldsPage solicitudFields = new SolicitudFieldsPage(
                new BigDecimal("5000000"),
                24,
                "test@example.com",
                "Personal",
                "Pendiente"
        );

        UsersForPageResponse userInfo = new UsersForPageResponse(
                "Juan Pérez",
                "test@example.com",
                3000000L
        );

        SolicitudConUsuario result = SolicitudConUsuario.fromSolicitudFieldsAndUser(solicitudFields, userInfo);

        assertNull(result.getIdSolicitud());
        assertEquals(new BigDecimal("5000000"), result.getMonto());
        assertEquals(24, result.getPlazo());
        assertEquals("test@example.com", result.getEmail());
        assertNull(result.getDocumentoIdentidad());
        assertNull(result.getIdEstado());
        assertNull(result.getIdTipoPrestamo());
        assertEquals("Personal", result.getNombreTipoPrestamo());
        assertEquals("Pendiente", result.getEstadoSolicitud());
        assertNull(result.getTasaInteres());
        assertEquals("Juan Pérez", result.getNombre());
        assertEquals(3000000L, result.getSalarioBase());
    }

    @Test
    void testFromSolicitudFieldsAndUserWithNullUserInfo() {
        SolicitudFieldsPage solicitudFields = new SolicitudFieldsPage(
                new BigDecimal("3000000"),
                12,
                "user@example.com",
                "Hipotecario",
                "Aprobado"
        );

        SolicitudConUsuario result = SolicitudConUsuario.fromSolicitudFieldsAndUser(solicitudFields, null);

        assertNull(result.getIdSolicitud());
        assertEquals(new BigDecimal("3000000"), result.getMonto());
        assertEquals(12, result.getPlazo());
        assertEquals("user@example.com", result.getEmail());
        assertNull(result.getDocumentoIdentidad());
        assertNull(result.getIdEstado());
        assertNull(result.getIdTipoPrestamo());
        assertEquals("Hipotecario", result.getNombreTipoPrestamo());
        assertEquals("Aprobado", result.getEstadoSolicitud());
        assertNull(result.getTasaInteres());
        assertNull(result.getNombre());
        assertNull(result.getSalarioBase());
    }
}
