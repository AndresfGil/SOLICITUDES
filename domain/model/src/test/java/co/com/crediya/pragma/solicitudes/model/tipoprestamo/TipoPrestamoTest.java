package co.com.crediya.pragma.solicitudes.model.tipoprestamo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para TipoPrestamo")
class TipoPrestamoTest {

    @Test
    @DisplayName("Debe crear tipo préstamo con builder")
    void shouldCreateTipoPrestamoWithBuilder() {
        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .nombre("Préstamo Personal")
                .montoMinimo(1000000)
                .montoMaximo(50000000)
                .tasaInteres(15)
                .validacionAutomatica(true)
                .build();

        assertEquals(1L, tipoPrestamo.getIdTipoPrestamo());
        assertEquals("Préstamo Personal", tipoPrestamo.getNombre());
        assertEquals(1000000, tipoPrestamo.getMontoMinimo());
        assertEquals(50000000, tipoPrestamo.getMontoMaximo());
        assertEquals(15, tipoPrestamo.getTasaInteres());
        assertTrue(tipoPrestamo.getValidacionAutomatica());
    }

    @Test
    @DisplayName("Debe crear tipo préstamo con constructor por defecto")
    void shouldCreateTipoPrestamoWithDefaultConstructor() {
        TipoPrestamo tipoPrestamo = new TipoPrestamo();

        assertNull(tipoPrestamo.getIdTipoPrestamo());
        assertNull(tipoPrestamo.getNombre());
        assertNull(tipoPrestamo.getMontoMinimo());
        assertNull(tipoPrestamo.getMontoMaximo());
        assertNull(tipoPrestamo.getTasaInteres());
        assertNull(tipoPrestamo.getValidacionAutomatica());
    }

    @Test
    @DisplayName("Debe crear tipo préstamo con constructor con parámetros")
    void shouldCreateTipoPrestamoWithParameterizedConstructor() {
        TipoPrestamo tipoPrestamo = new TipoPrestamo(
                2L,
                "Préstamo Vehicular",
                2000000,
                30000000,
                12,
                false
        );

        assertEquals(2L, tipoPrestamo.getIdTipoPrestamo());
        assertEquals("Préstamo Vehicular", tipoPrestamo.getNombre());
        assertEquals(2000000, tipoPrestamo.getMontoMinimo());
        assertEquals(30000000, tipoPrestamo.getMontoMaximo());
        assertEquals(12, tipoPrestamo.getTasaInteres());
        assertFalse(tipoPrestamo.getValidacionAutomatica());
    }

}
