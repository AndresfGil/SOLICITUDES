package co.com.crediya.pragma.solicitudes.model.notificaicones;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CambioEstadoTest {

    @Test
    void builder_ShouldSetFields() {
        CambioEstado cambio = CambioEstado.builder()
                .idSolicitud(5L)
                .idEstado(2L)
                .build();

        assertEquals(5L, cambio.getIdSolicitud());
        assertEquals(2L, cambio.getIdEstado());
    }

    @Test
    void testCambioEstadoNoArgsConstructor() {
        CambioEstado cambio = new CambioEstado();
        
        assertNull(cambio.getIdSolicitud());
        assertNull(cambio.getIdEstado());
    }

    @Test
    void testCambioEstadoAllArgsConstructor() {
        CambioEstado cambio = new CambioEstado(3L, 1L);

        assertEquals(3L, cambio.getIdSolicitud());
        assertEquals(1L, cambio.getIdEstado());
    }

    @Test
    void testCambioEstadoSettersAndGetters() {
        CambioEstado cambio = new CambioEstado();
        
        cambio.setIdSolicitud(7L);
        cambio.setIdEstado(4L);

        assertEquals(7L, cambio.getIdSolicitud());
        assertEquals(4L, cambio.getIdEstado());
    }

    @Test
    void testCambioEstadoToBuilder() {
        CambioEstado original = CambioEstado.builder()
                .idSolicitud(5L)
                .idEstado(2L)
                .build();

        CambioEstado modified = original.toBuilder()
                .idEstado(3L)
                .build();

        assertEquals(5L, modified.getIdSolicitud());
        assertEquals(3L, modified.getIdEstado());
    }
}



