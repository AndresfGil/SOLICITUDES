package co.com.crediya.pragma.solicitudes.model.estado;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para Estado")
class EstadoTest {

    @Test
    @DisplayName("Debe crear estado con builder")
    void shouldCreateEstadoWithBuilder() {
        Estado estado = Estado.builder()
                .idEstado(1L)
                .nombreEstado("PENDIENTE")
                .descripcionEstado("Solicitud en proceso de revisión")
                .build();

        assertEquals(1L, estado.getIdEstado());
        assertEquals("PENDIENTE", estado.getNombreEstado());
        assertEquals("Solicitud en proceso de revisión", estado.getDescripcionEstado());
    }

    @Test
    @DisplayName("Debe crear estado con constructor por defecto")
    void shouldCreateEstadoWithDefaultConstructor() {
        Estado estado = new Estado();

        assertNull(estado.getIdEstado());
        assertNull(estado.getNombreEstado());
        assertNull(estado.getDescripcionEstado());
    }

    @Test
    @DisplayName("Debe crear estado con constructor con parámetros")
    void shouldCreateEstadoWithParameterizedConstructor() {
        Estado estado = new Estado(2L, "APROBADO", "Solicitud aprobada");

        assertEquals(2L, estado.getIdEstado());
        assertEquals("APROBADO", estado.getNombreEstado());
        assertEquals("Solicitud aprobada", estado.getDescripcionEstado());
    }
}
