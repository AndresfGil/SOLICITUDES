package co.com.crediya.pragma.solicitudes.model.estado;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoEnumTest {

    @Test
    void fromStatusJson_matchesKnownValues_caseInsensitive() {
        assertEquals(EstadoEnum.APROBADO, EstadoEnum.fromStatusJson("APROBADA"));
        assertEquals(EstadoEnum.APROBADO, EstadoEnum.fromStatusJson("aprobada"));
        assertEquals(EstadoEnum.RECHAZADO, EstadoEnum.fromStatusJson("RECHAZADA"));
        assertEquals(EstadoEnum.PENDIENTE, EstadoEnum.fromStatusJson("PENDIENTE"));
    }

    @Test
    void fromStatusJson_unknownOrNull_returnsPendiente() {
        assertEquals(EstadoEnum.PENDIENTE, EstadoEnum.fromStatusJson("DESCONOCIDO"));
        assertEquals(EstadoEnum.PENDIENTE, EstadoEnum.fromStatusJson(null));
    }
}


