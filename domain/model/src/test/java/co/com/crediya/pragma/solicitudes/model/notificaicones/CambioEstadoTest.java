package co.com.crediya.pragma.solicitudes.model.notificaicones;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}



