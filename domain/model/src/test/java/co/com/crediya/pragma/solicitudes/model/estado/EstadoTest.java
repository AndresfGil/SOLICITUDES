package co.com.crediya.pragma.solicitudes.model.estado;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para la entidad Estado")
class EstadoTest {

    private Estado estado;

    @BeforeEach
    void setUp() {
        estado = Estado.builder()
                .idEstado(1L)
                .nombreEstado("PENDIENTE")
                .descripcionEstado("Solicitud en proceso de revisión")
                .build();
    }

    @Test
    @DisplayName("Debería crear un estado con builder")
    void shouldCreateEstadoWithBuilder() {

        Estado nuevoEstado = Estado.builder()
                .idEstado(2L)
                .nombreEstado("APROBADO")
                .descripcionEstado("Solicitud aprobada")
                .build();


        assertNotNull(nuevoEstado);
        assertEquals(2L, nuevoEstado.getIdEstado());
        assertEquals("APROBADO", nuevoEstado.getNombreEstado());
        assertEquals("Solicitud aprobada", nuevoEstado.getDescripcionEstado());
    }

    @Test
    @DisplayName("Debería crear un estado con constructor por defecto")
    void shouldCreateEstadoWithDefaultConstructor() {

        Estado nuevoEstado = new Estado();


        assertNotNull(nuevoEstado);
        assertNull(nuevoEstado.getIdEstado());
        assertNull(nuevoEstado.getNombreEstado());
        assertNull(nuevoEstado.getDescripcionEstado());
    }

    @Test
    @DisplayName("Debería crear un estado con constructor con parámetros")
    void shouldCreateEstadoWithParameterizedConstructor() {

        Estado nuevoEstado = new Estado(
                3L,
                "RECHAZADO",
                "Solicitud rechazada"
        );


        assertNotNull(nuevoEstado);
        assertEquals(3L, nuevoEstado.getIdEstado());
        assertEquals("RECHAZADO", nuevoEstado.getNombreEstado());
        assertEquals("Solicitud rechazada", nuevoEstado.getDescripcionEstado());
    }

    @Test
    @DisplayName("Debería modificar valores del estado con setters")
    void shouldModifyEstadoValuesWithSetters() {
        // Given
        Estado estadoModificado = new Estado();

        // When
        estadoModificado.setIdEstado(4L);
        estadoModificado.setNombreEstado("EN_REVISION");
        estadoModificado.setDescripcionEstado("Solicitud en revisión técnica");


        assertEquals(4L, estadoModificado.getIdEstado());
        assertEquals("EN_REVISION", estadoModificado.getNombreEstado());
        assertEquals("Solicitud en revisión técnica", estadoModificado.getDescripcionEstado());
    }

}
