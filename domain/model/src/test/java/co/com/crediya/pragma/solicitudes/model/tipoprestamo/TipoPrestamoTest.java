package co.com.crediya.pragma.solicitudes.model.tipoprestamo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TipoPrestamoTest {

    @Test
    void testTipoPrestamoBuilder() {
        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .nombre("Personal")
                .montoMinimo(1000000)
                .montoMaximo(10000000)
                .tasaInteres(15)
                .validacionAutomatica(true)
                .build();

        assertEquals(1L, tipoPrestamo.getIdTipoPrestamo());
        assertEquals("Personal", tipoPrestamo.getNombre());
        assertEquals(1000000, tipoPrestamo.getMontoMinimo());
        assertEquals(10000000, tipoPrestamo.getMontoMaximo());
        assertEquals(15, tipoPrestamo.getTasaInteres());
        assertTrue(tipoPrestamo.getValidacionAutomatica());
    }

    @Test
    void testTipoPrestamoNoArgsConstructor() {
        TipoPrestamo tipoPrestamo = new TipoPrestamo();
        
        assertNull(tipoPrestamo.getIdTipoPrestamo());
        assertNull(tipoPrestamo.getNombre());
        assertNull(tipoPrestamo.getMontoMinimo());
        assertNull(tipoPrestamo.getMontoMaximo());
        assertNull(tipoPrestamo.getTasaInteres());
        assertNull(tipoPrestamo.getValidacionAutomatica());
    }

    @Test
    void testTipoPrestamoAllArgsConstructor() {
        TipoPrestamo tipoPrestamo = new TipoPrestamo(
                2L,
                "Hipotecario",
                5000000,
                50000000,
                8,
                false
        );

        assertEquals(2L, tipoPrestamo.getIdTipoPrestamo());
        assertEquals("Hipotecario", tipoPrestamo.getNombre());
        assertEquals(5000000, tipoPrestamo.getMontoMinimo());
        assertEquals(50000000, tipoPrestamo.getMontoMaximo());
        assertEquals(8, tipoPrestamo.getTasaInteres());
        assertFalse(tipoPrestamo.getValidacionAutomatica());
    }

    @Test
    void testTipoPrestamoSettersAndGetters() {
        TipoPrestamo tipoPrestamo = new TipoPrestamo();
        
        tipoPrestamo.setIdTipoPrestamo(3L);
        tipoPrestamo.setNombre("Vehicular");
        tipoPrestamo.setMontoMinimo(2000000);
        tipoPrestamo.setMontoMaximo(20000000);
        tipoPrestamo.setTasaInteres(12);
        tipoPrestamo.setValidacionAutomatica(true);

        assertEquals(3L, tipoPrestamo.getIdTipoPrestamo());
        assertEquals("Vehicular", tipoPrestamo.getNombre());
        assertEquals(2000000, tipoPrestamo.getMontoMinimo());
        assertEquals(20000000, tipoPrestamo.getMontoMaximo());
        assertEquals(12, tipoPrestamo.getTasaInteres());
        assertTrue(tipoPrestamo.getValidacionAutomatica());
    }

    @Test
    void testTipoPrestamoToBuilder() {
        TipoPrestamo original = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .nombre("Personal")
                .montoMinimo(1000000)
                .montoMaximo(10000000)
                .tasaInteres(15)
                .validacionAutomatica(true)
                .build();

        TipoPrestamo modified = original.toBuilder()
                .nombre("Personal Plus")
                .tasaInteres(18)
                .build();

        assertEquals(1L, modified.getIdTipoPrestamo());
        assertEquals("Personal Plus", modified.getNombre());
        assertEquals(1000000, modified.getMontoMinimo());
        assertEquals(10000000, modified.getMontoMaximo());
        assertEquals(18, modified.getTasaInteres());
        assertTrue(modified.getValidacionAutomatica());
    }
}
