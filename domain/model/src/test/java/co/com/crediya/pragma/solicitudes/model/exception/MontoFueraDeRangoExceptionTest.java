package co.com.crediya.pragma.solicitudes.model.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para MontoFueraDeRangoException")
class MontoFueraDeRangoExceptionTest {

    @Test
    @DisplayName("Debe crear excepción con montos correctos")
    void shouldCreateExceptionWithCorrectAmounts() {
        BigDecimal montoSolicitado = new BigDecimal("10000000");
        BigDecimal montoMinimo = new BigDecimal("1000000");
        BigDecimal montoMaximo = new BigDecimal("5000000");

        MontoFueraDeRangoException exception = new MontoFueraDeRangoException(
                montoSolicitado, montoMinimo, montoMaximo);

        String expectedMessage = "El monto solicitado $10000000 está fuera del rango permitido para este tipo de préstamo.";
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals("MONTO_FUERA_DE_RANGO", exception.getErrorCode());
        assertEquals("Monto fuera de rango", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(List.of("El monto debe estar entre $1000000 y $5000000"), exception.getErrors());
    }

    @Test
    @DisplayName("Debe crear excepción con montos decimales")
    void shouldCreateExceptionWithDecimalAmounts() {
        BigDecimal montoSolicitado = new BigDecimal("500.50");
        BigDecimal montoMinimo = new BigDecimal("1000.00");
        BigDecimal montoMaximo = new BigDecimal("5000.00");

        MontoFueraDeRangoException exception = new MontoFueraDeRangoException(
                montoSolicitado, montoMinimo, montoMaximo);

        String expectedMessage = "El monto solicitado $500.50 está fuera del rango permitido para este tipo de préstamo.";
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(List.of("El monto debe estar entre $1000.00 y $5000.00"), exception.getErrors());
    }

    @Test
    @DisplayName("Debe crear excepción con montos grandes")
    void shouldCreateExceptionWithLargeAmounts() {
        BigDecimal montoSolicitado = new BigDecimal("1000000000");
        BigDecimal montoMinimo = new BigDecimal("100000000");
        BigDecimal montoMaximo = new BigDecimal("500000000");

        MontoFueraDeRangoException exception = new MontoFueraDeRangoException(
                montoSolicitado, montoMinimo, montoMaximo);

        String expectedMessage = "El monto solicitado $1000000000 está fuera del rango permitido para este tipo de préstamo.";
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(List.of("El monto debe estar entre $100000000 y $500000000"), exception.getErrors());
    }
}
