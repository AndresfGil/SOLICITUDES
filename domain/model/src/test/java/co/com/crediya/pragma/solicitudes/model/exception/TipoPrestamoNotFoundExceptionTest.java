package co.com.crediya.pragma.solicitudes.model.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para TipoPrestamoNotFoundException")
class TipoPrestamoNotFoundExceptionTest {

    @Test
    @DisplayName("Debe crear excepción con ID válido")
    void shouldCreateExceptionWithValidId() {
        Long idTipoPrestamo = 123L;

        TipoPrestamoNotFoundException exception = new TipoPrestamoNotFoundException(idTipoPrestamo);

        String expectedMessage = "El tipo de préstamo con id 123 no existe.";
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals("TIPO_PRESTAMO_NOT_FOUND", exception.getErrorCode());
        assertEquals("Tipo de préstamo inválido", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(List.of("El tipo de préstamo seleccionado no es válido"), exception.getErrors());
    }

    @Test
    @DisplayName("Debe crear excepción con ID nulo")
    void shouldCreateExceptionWithNullId() {
        TipoPrestamoNotFoundException exception = new TipoPrestamoNotFoundException(null);

        String expectedMessage = "El tipo de préstamo con id null no existe.";
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals("TIPO_PRESTAMO_NOT_FOUND", exception.getErrorCode());
        assertEquals("Tipo de préstamo inválido", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
    }

    @Test
    @DisplayName("Debe crear excepción con ID cero")
    void shouldCreateExceptionWithZeroId() {
        Long idTipoPrestamo = 0L;

        TipoPrestamoNotFoundException exception = new TipoPrestamoNotFoundException(idTipoPrestamo);

        String expectedMessage = "El tipo de préstamo con id 0 no existe.";
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals("TIPO_PRESTAMO_NOT_FOUND", exception.getErrorCode());
        assertEquals("Tipo de préstamo inválido", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
    }
}
