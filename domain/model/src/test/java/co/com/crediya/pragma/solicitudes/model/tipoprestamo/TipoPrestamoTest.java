package co.com.crediya.pragma.solicitudes.model.tipoprestamo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para la entidad TipoPrestamo")
class TipoPrestamoTest {

    @Test
    @DisplayName("Debería crear un tipo de préstamo con builder")
    void shouldCreateTipoPrestamoWithBuilder() {
        TipoPrestamo nuevoTipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(2L)
                .nombre("Prestamo Hipotecario")
                .montoMinimo(50000000)
                .montoMaximo(500000000)
                .tasaInteres(8)
                .validacionAutomatica(false)
                .build();

        assertNotNull(nuevoTipoPrestamo);
        assertEquals(2L, nuevoTipoPrestamo.getIdTipoPrestamo());
        assertEquals("Prestamo Hipotecario", nuevoTipoPrestamo.getNombre());
        assertEquals(50000000, nuevoTipoPrestamo.getMontoMinimo());
        assertEquals(500000000, nuevoTipoPrestamo.getMontoMaximo());
        assertEquals(8, nuevoTipoPrestamo.getTasaInteres());
        assertFalse(nuevoTipoPrestamo.getValidacionAutomatica());
    }

    @Test
    @DisplayName("Debería crear un tipo de préstamo con constructor genérico")
    void shouldCreateTipoPrestamoWithGenericConstructor() {
        TipoPrestamo nuevoTipoPrestamo = new TipoPrestamo(4L, "Prestamo Comercial");

        assertNotNull(nuevoTipoPrestamo);
    }

    @Test
    @DisplayName("Debería modificar valores del tipo de préstamo con setters")
    void shouldModifyTipoPrestamoValuesWithSetters() {
        TipoPrestamo tipoPrestamoModificado = new TipoPrestamo();

        tipoPrestamoModificado.setIdTipoPrestamo(5L);
        tipoPrestamoModificado.setNombre("Prestamo Microempresa");
        tipoPrestamoModificado.setMontoMinimo(500000);
        tipoPrestamoModificado.setMontoMaximo(10000000);
        tipoPrestamoModificado.setTasaInteres(18);
        tipoPrestamoModificado.setValidacionAutomatica(true);

        assertEquals(5L, tipoPrestamoModificado.getIdTipoPrestamo());
        assertEquals("Prestamo Microempresa", tipoPrestamoModificado.getNombre());
        assertEquals(500000, tipoPrestamoModificado.getMontoMinimo());
        assertEquals(10000000, tipoPrestamoModificado.getMontoMaximo());
        assertEquals(18, tipoPrestamoModificado.getTasaInteres());
        assertTrue(tipoPrestamoModificado.getValidacionAutomatica());
    }

    @Test
    @DisplayName("Debería manejar valores nulos correctamente")
    void shouldHandleNullValuesCorrectly() {
        TipoPrestamo tipoPrestamoNulo = TipoPrestamo.builder()
                .idTipoPrestamo(null)
                .nombre(null)
                .montoMinimo(null)
                .montoMaximo(null)
                .tasaInteres(null)
                .validacionAutomatica(null)
                .build();

        assertNotNull(tipoPrestamoNulo);
        assertNull(tipoPrestamoNulo.getIdTipoPrestamo());
        assertNull(tipoPrestamoNulo.getNombre());
        assertNull(tipoPrestamoNulo.getMontoMinimo());
        assertNull(tipoPrestamoNulo.getMontoMaximo());
        assertNull(tipoPrestamoNulo.getTasaInteres());
        assertNull(tipoPrestamoNulo.getValidacionAutomatica());
    }

    @Test
    @DisplayName("Debería manejar valores booleanos correctamente")
    void shouldHandleBooleanValuesCorrectly() {
        TipoPrestamo tipoPrestamoTrue = TipoPrestamo.builder()
                .idTipoPrestamo(6L)
                .nombre("Prestamo Automático")
                .validacionAutomatica(true)
                .build();

        TipoPrestamo tipoPrestamoFalse = TipoPrestamo.builder()
                .idTipoPrestamo(7L)
                .nombre("Prestamo Manual")
                .validacionAutomatica(false)
                .build();

        assertTrue(tipoPrestamoTrue.getValidacionAutomatica());
        assertFalse(tipoPrestamoFalse.getValidacionAutomatica());
    }

    @Test
    @DisplayName("Debería manejar valores numéricos extremos correctamente")
    void shouldHandleExtremeNumericValuesCorrectly() {
        TipoPrestamo tipoPrestamoExtremo = TipoPrestamo.builder()
                .idTipoPrestamo(Long.MAX_VALUE)
                .nombre("Prestamo Extremo")
                .montoMinimo(Integer.MIN_VALUE)
                .montoMaximo(Integer.MAX_VALUE)
                .tasaInteres(0)
                .validacionAutomatica(true)
                .build();

        assertEquals(Long.MAX_VALUE, tipoPrestamoExtremo.getIdTipoPrestamo());
        assertEquals(Integer.MIN_VALUE, tipoPrestamoExtremo.getMontoMinimo());
        assertEquals(Integer.MAX_VALUE, tipoPrestamoExtremo.getMontoMaximo());
        assertEquals(0, tipoPrestamoExtremo.getTasaInteres());
        assertTrue(tipoPrestamoExtremo.getValidacionAutomatica());
    }

    @Test
    @DisplayName("Debería manejar strings vacíos correctamente")
    void shouldHandleEmptyStringsCorrectly() {
        TipoPrestamo tipoPrestamoVacio = TipoPrestamo.builder()
                .idTipoPrestamo(8L)
                .nombre("")
                .montoMinimo(1000000)
                .montoMaximo(50000000)
                .tasaInteres(15)
                .validacionAutomatica(true)
                .build();

        assertNotNull(tipoPrestamoVacio);
        assertEquals(8L, tipoPrestamoVacio.getIdTipoPrestamo());
        assertEquals("", tipoPrestamoVacio.getNombre());
        assertEquals(1000000, tipoPrestamoVacio.getMontoMinimo());
        assertEquals(50000000, tipoPrestamoVacio.getMontoMaximo());
        assertEquals(15, tipoPrestamoVacio.getTasaInteres());
        assertTrue(tipoPrestamoVacio.getValidacionAutomatica());
    }
}
