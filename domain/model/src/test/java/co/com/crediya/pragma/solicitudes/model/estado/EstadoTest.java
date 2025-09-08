package co.com.crediya.pragma.solicitudes.model.estado;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoTest {

    @Test
    void testEstadoBuilder() {
        Estado estado = Estado.builder()
                .idEstado(1L)
                .nombreEstado("Pendiente")
                .descripcionEstado("Solicitud en proceso de revisión")
                .build();

        assertEquals(1L, estado.getIdEstado());
        assertEquals("Pendiente", estado.getNombreEstado());
        assertEquals("Solicitud en proceso de revisión", estado.getDescripcionEstado());
    }

    @Test
    void testEstadoNoArgsConstructor() {
        Estado estado = new Estado();
        
        assertNull(estado.getIdEstado());
        assertNull(estado.getNombreEstado());
        assertNull(estado.getDescripcionEstado());
    }

    @Test
    void testEstadoAllArgsConstructor() {
        Estado estado = new Estado(
                2L,
                "Aprobado",
                "Solicitud aprobada para procesamiento"
        );

        assertEquals(2L, estado.getIdEstado());
        assertEquals("Aprobado", estado.getNombreEstado());
        assertEquals("Solicitud aprobada para procesamiento", estado.getDescripcionEstado());
    }

    @Test
    void testEstadoSettersAndGetters() {
        Estado estado = new Estado();
        
        estado.setIdEstado(3L);
        estado.setNombreEstado("Rechazado");
        estado.setDescripcionEstado("Solicitud rechazada por incumplimiento de requisitos");

        assertEquals(3L, estado.getIdEstado());
        assertEquals("Rechazado", estado.getNombreEstado());
        assertEquals("Solicitud rechazada por incumplimiento de requisitos", estado.getDescripcionEstado());
    }

    @Test
    void testEstadoToBuilder() {
        Estado original = Estado.builder()
                .idEstado(1L)
                .nombreEstado("Pendiente")
                .descripcionEstado("Solicitud en proceso de revisión")
                .build();

        Estado modified = original.toBuilder()
                .nombreEstado("En Revisión")
                .descripcionEstado("Solicitud siendo evaluada por el equipo")
                .build();

        assertEquals(1L, modified.getIdEstado());
        assertEquals("En Revisión", modified.getNombreEstado());
        assertEquals("Solicitud siendo evaluada por el equipo", modified.getDescripcionEstado());
    }
}
