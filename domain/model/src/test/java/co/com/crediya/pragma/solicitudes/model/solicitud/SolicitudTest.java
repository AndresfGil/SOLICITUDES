package co.com.crediya.pragma.solicitudes.model.solicitud;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para Solicitud")
class SolicitudTest {

    @Test
    @DisplayName("Debe crear solicitud con builder")
    void shouldCreateSolicitudWithBuilder() {
        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(12)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(1L)
                .idTipoPrestamo(1L)
                .build();

        assertEquals(1L, solicitud.getIdSolicitud());
        assertEquals(new BigDecimal("5000000"), solicitud.getMonto());
        assertEquals(12, solicitud.getPlazo());
        assertEquals("test@example.com", solicitud.getEmail());
        assertEquals("12345678", solicitud.getDocumentoIdentidad());
        assertEquals(1L, solicitud.getIdEstado());
        assertEquals(1L, solicitud.getIdTipoPrestamo());
    }

    @Test
    @DisplayName("Debe crear solicitud con constructor por defecto")
    void shouldCreateSolicitudWithDefaultConstructor() {
        Solicitud solicitud = new Solicitud();

        assertNull(solicitud.getIdSolicitud());
        assertNull(solicitud.getMonto());
        assertNull(solicitud.getPlazo());
        assertNull(solicitud.getEmail());
        assertNull(solicitud.getDocumentoIdentidad());
        assertNull(solicitud.getIdEstado());
        assertNull(solicitud.getIdTipoPrestamo());
    }

    @Test
    @DisplayName("Debe crear solicitud con constructor con parámetros")
    void shouldCreateSolicitudWithParameterizedConstructor() {
        Solicitud solicitud = new Solicitud(
                1L,
                new BigDecimal("3000000"),
                24,
                "user@test.com",
                "87654321",
                2L,
                3L
        );

        assertEquals(1L, solicitud.getIdSolicitud());
        assertEquals(new BigDecimal("3000000"), solicitud.getMonto());
        assertEquals(24, solicitud.getPlazo());
        assertEquals("user@test.com", solicitud.getEmail());
        assertEquals("87654321", solicitud.getDocumentoIdentidad());
        assertEquals(2L, solicitud.getIdEstado());
        assertEquals(3L, solicitud.getIdTipoPrestamo());
    }

    @Test
    @DisplayName("Debe modificar solicitud con setters")
    void shouldModifySolicitudWithSetters() {
        Solicitud solicitud = new Solicitud();
        
        solicitud.setIdSolicitud(5L);
        solicitud.setMonto(new BigDecimal("10000000"));
        solicitud.setPlazo(36);
        solicitud.setEmail("updated@test.com");
        solicitud.setDocumentoIdentidad("99999999");
        solicitud.setIdEstado(3L);
        solicitud.setIdTipoPrestamo(2L);

        assertEquals(5L, solicitud.getIdSolicitud());
        assertEquals(new BigDecimal("10000000"), solicitud.getMonto());
        assertEquals(36, solicitud.getPlazo());
        assertEquals("updated@test.com", solicitud.getEmail());
        assertEquals("99999999", solicitud.getDocumentoIdentidad());
        assertEquals(3L, solicitud.getIdEstado());
        assertEquals(2L, solicitud.getIdTipoPrestamo());
    }

}
