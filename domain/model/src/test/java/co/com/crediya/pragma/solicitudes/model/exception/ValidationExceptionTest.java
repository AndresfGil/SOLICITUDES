package co.com.crediya.pragma.solicitudes.model.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para ValidationException")
class ValidationExceptionTest {

    @Test
    @DisplayName("Debe crear excepción con lista de errores")
    void shouldCreateExceptionWithErrorList() {
        List<String> errors = List.of("Campo requerido", "Formato inválido", "Valor fuera de rango");

        ValidationException exception = new ValidationException(errors);

        assertEquals("Error de validación", exception.getMessage());
        assertEquals("VALIDATION_ERROR", exception.getErrorCode());
        assertEquals("Error de validación", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(errors, exception.getErrors());
    }

    @Test
    @DisplayName("Debe crear excepción con lista vacía")
    void shouldCreateExceptionWithEmptyList() {
        List<String> errors = List.of();

        ValidationException exception = new ValidationException(errors);

        assertEquals("Error de validación", exception.getMessage());
        assertEquals("VALIDATION_ERROR", exception.getErrorCode());
        assertEquals("Error de validación", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(errors, exception.getErrors());
    }

    @Test
    @DisplayName("Debe crear excepción con un solo error")
    void shouldCreateExceptionWithSingleError() {
        List<String> errors = List.of("Solo un error");

        ValidationException exception = new ValidationException(errors);

        assertEquals("Error de validación", exception.getMessage());
        assertEquals("VALIDATION_ERROR", exception.getErrorCode());
        assertEquals("Error de validación", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(errors, exception.getErrors());
    }
}
