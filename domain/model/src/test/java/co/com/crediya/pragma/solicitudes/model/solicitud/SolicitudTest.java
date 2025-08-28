package co.com.crediya.pragma.solicitudes.model.solicitud;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para la entidad Solicitud")
class SolicitudTest {

    private Solicitud solicitud;

    @BeforeEach
    void setUp() {
        solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(12)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(1L)
                .idTipoPrestamo(1L)
                .build();
    }

    @Test
    @DisplayName("Debería crear una solicitud con builder")
    void shouldCreateSolicitudWithBuilder() {
        // Given & When
        Solicitud nuevaSolicitud = Solicitud.builder()
                .idSolicitud(2L)
                .monto(new BigDecimal("3000000"))
                .plazo(6)
                .email("nuevo@example.com")
                .documentoIdentidad("87654321")
                .idEstado(2L)
                .idTipoPrestamo(2L)
                .build();

        // Then
        assertNotNull(nuevaSolicitud);
        assertEquals(2L, nuevaSolicitud.getIdSolicitud());
        assertEquals(new BigDecimal("3000000"), nuevaSolicitud.getMonto());
        assertEquals(6, nuevaSolicitud.getPlazo());
        assertEquals("nuevo@example.com", nuevaSolicitud.getEmail());
        assertEquals("87654321", nuevaSolicitud.getDocumentoIdentidad());
        assertEquals(2L, nuevaSolicitud.getIdEstado());
        assertEquals(2L, nuevaSolicitud.getIdTipoPrestamo());
    }

    @Test
    @DisplayName("Debería crear una solicitud con constructor por defecto")
    void shouldCreateSolicitudWithDefaultConstructor() {
        // Given & When
        Solicitud nuevaSolicitud = new Solicitud();

        // Then
        assertNotNull(nuevaSolicitud);
        assertNull(nuevaSolicitud.getIdSolicitud());
        assertNull(nuevaSolicitud.getMonto());
        assertNull(nuevaSolicitud.getPlazo());
        assertNull(nuevaSolicitud.getEmail());
        assertNull(nuevaSolicitud.getDocumentoIdentidad());
        assertNull(nuevaSolicitud.getIdEstado());
        assertNull(nuevaSolicitud.getIdTipoPrestamo());
    }

    @Test
    @DisplayName("Debería crear una solicitud con constructor con parámetros")
    void shouldCreateSolicitudWithParameterizedConstructor() {
        // Given & When
        Solicitud nuevaSolicitud = new Solicitud(
                3L,
                new BigDecimal("7000000"),
                24,
                "parametros@example.com",
                "11223344",
                3L,
                3L
        );

        // Then
        assertNotNull(nuevaSolicitud);
        assertEquals(3L, nuevaSolicitud.getIdSolicitud());
        assertEquals(new BigDecimal("7000000"), nuevaSolicitud.getMonto());
        assertEquals(24, nuevaSolicitud.getPlazo());
        assertEquals("parametros@example.com", nuevaSolicitud.getEmail());
        assertEquals("11223344", nuevaSolicitud.getDocumentoIdentidad());
        assertEquals(3L, nuevaSolicitud.getIdEstado());
        assertEquals(3L, nuevaSolicitud.getIdTipoPrestamo());
    }

    @Test
    @DisplayName("Debería modificar valores de la solicitud con setters")
    void shouldModifySolicitudValuesWithSetters() {
        // Given
        Solicitud solicitudModificada = new Solicitud();

        // When
        solicitudModificada.setIdSolicitud(4L);
        solicitudModificada.setMonto(new BigDecimal("4000000"));
        solicitudModificada.setPlazo(18);
        solicitudModificada.setEmail("modificado@example.com");
        solicitudModificada.setDocumentoIdentidad("99887766");
        solicitudModificada.setIdEstado(4L);
        solicitudModificada.setIdTipoPrestamo(4L);

        // Then
        assertEquals(4L, solicitudModificada.getIdSolicitud());
        assertEquals(new BigDecimal("4000000"), solicitudModificada.getMonto());
        assertEquals(18, solicitudModificada.getPlazo());
        assertEquals("modificado@example.com", solicitudModificada.getEmail());
        assertEquals("99887766", solicitudModificada.getDocumentoIdentidad());
        assertEquals(4L, solicitudModificada.getIdEstado());
        assertEquals(4L, solicitudModificada.getIdTipoPrestamo());
    }

    @Test
    @DisplayName("Debería crear una copia modificada con toBuilder")
    void shouldCreateModifiedCopyWithToBuilder() {
        // Given
        Solicitud solicitudOriginal = solicitud;

        // When
        Solicitud solicitudModificada = solicitudOriginal.toBuilder()
                .monto(new BigDecimal("6000000"))
                .plazo(36)
                .email("copia@example.com")
                .build();

        // Then
        assertNotNull(solicitudModificada);
        assertEquals(solicitudOriginal.getIdSolicitud(), solicitudModificada.getIdSolicitud());
        assertEquals(new BigDecimal("6000000"), solicitudModificada.getMonto());
        assertEquals(36, solicitudModificada.getPlazo());
        assertEquals("copia@example.com", solicitudModificada.getEmail());
        assertEquals(solicitudOriginal.getDocumentoIdentidad(), solicitudModificada.getDocumentoIdentidad());
        assertEquals(solicitudOriginal.getIdEstado(), solicitudModificada.getIdEstado());
        assertEquals(solicitudOriginal.getIdTipoPrestamo(), solicitudModificada.getIdTipoPrestamo());
    }

    @Test
    @DisplayName("Debería manejar valores nulos correctamente")
    void shouldHandleNullValuesCorrectly() {
        // Given & When
        Solicitud solicitudNula = Solicitud.builder()
                .idSolicitud(null)
                .monto(null)
                .plazo(null)
                .email(null)
                .documentoIdentidad(null)
                .idEstado(null)
                .idTipoPrestamo(null)
                .build();

        // Then
        assertNotNull(solicitudNula);
        assertNull(solicitudNula.getIdSolicitud());
        assertNull(solicitudNula.getMonto());
        assertNull(solicitudNula.getPlazo());
        assertNull(solicitudNula.getEmail());
        assertNull(solicitudNula.getDocumentoIdentidad());
        assertNull(solicitudNula.getIdEstado());
        assertNull(solicitudNula.getIdTipoPrestamo());
    }
}
