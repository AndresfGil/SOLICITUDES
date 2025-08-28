package co.com.crediya.pragma.solicitudes.model.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para la clase MontoFueraDeRangoException")
class MontoFueraDeRangoExceptionTest {

    @Test
    @DisplayName("Debería crear una excepción con montos válidos")
    void shouldCreateExceptionWithValidAmounts() {
        BigDecimal montoSolicitado = new BigDecimal("1000000");
        BigDecimal montoMinimo = new BigDecimal("500000");
        BigDecimal montoMaximo = new BigDecimal("5000000");

        MontoFueraDeRangoException exception = new MontoFueraDeRangoException(
                montoSolicitado, montoMinimo, montoMaximo);

        assertNotNull(exception);
        assertEquals("El monto solicitado $1000000 está fuera del rango permitido para este tipo de préstamo.", 
                exception.getMessage());
        assertEquals("MONTO_FUERA_DE_RANGO", exception.getErrorCode());
        assertEquals("Monto fuera de rango", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(1, exception.getErrors().size());
        assertEquals("El monto debe estar entre $500000 y $5000000", exception.getErrors().get(0));
        assertNotNull(exception.getTimestamp());
    }

    @Test
    @DisplayName("Debería crear una excepción con montos con decimales")
    void shouldCreateExceptionWithDecimalAmounts() {
        BigDecimal montoSolicitado = new BigDecimal("1000000.50");
        BigDecimal montoMinimo = new BigDecimal("500000.25");
        BigDecimal montoMaximo = new BigDecimal("5000000.75");

        MontoFueraDeRangoException exception = new MontoFueraDeRangoException(
                montoSolicitado, montoMinimo, montoMaximo);

        assertNotNull(exception);
        assertEquals("El monto solicitado $1000000.50 está fuera del rango permitido para este tipo de préstamo.", 
                exception.getMessage());
        assertEquals("El monto debe estar entre $500000.25 y $5000000.75", exception.getErrors().get(0));
    }

    @Test
    @DisplayName("Debería crear una excepción con montos grandes")
    void shouldCreateExceptionWithLargeAmounts() {
        BigDecimal montoSolicitado = new BigDecimal("1000000000");
        BigDecimal montoMinimo = new BigDecimal("10000000");
        BigDecimal montoMaximo = new BigDecimal("500000000");

        MontoFueraDeRangoException exception = new MontoFueraDeRangoException(
                montoSolicitado, montoMinimo, montoMaximo);

        assertNotNull(exception);
        assertEquals("El monto solicitado $1000000000 está fuera del rango permitido para este tipo de préstamo.", 
                exception.getMessage());
        assertEquals("El monto debe estar entre $10000000 y $500000000", exception.getErrors().get(0));
    }
}
