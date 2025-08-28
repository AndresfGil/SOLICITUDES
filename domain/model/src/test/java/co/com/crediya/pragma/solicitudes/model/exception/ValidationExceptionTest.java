package co.com.crediya.pragma.solicitudes.model.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para la clase ValidationException")
class ValidationExceptionTest {


    @Test
    @DisplayName("Debería crear una excepción de validación con lista de errores")
    void shouldCreateValidationExceptionWithErrorsList() {

        List<String> errors = Arrays.asList("Campo requerido", "Formato inválido", "Valor fuera de rango");
        ValidationException exception = new ValidationException(errors);


        assertNotNull(exception);
        assertEquals("Error de validación", exception.getMessage());
        assertEquals("VALIDATION_ERROR", exception.getErrorCode());
        assertEquals("Error de validación", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(3, exception.getErrors().size());
        assertTrue(exception.getErrors().contains("Campo requerido"));
        assertTrue(exception.getErrors().contains("Formato inválido"));
        assertTrue(exception.getErrors().contains("Valor fuera de rango"));
        assertNotNull(exception.getTimestamp());
    }

    @Test
    @DisplayName("Debería crear una excepción de validación con un solo error")
    void shouldCreateValidationExceptionWithSingleError() {

        List<String> errors = Arrays.asList("Solo un error");
        ValidationException exception = new ValidationException(errors);


        assertNotNull(exception);
        assertEquals("Error de validación", exception.getMessage());
        assertEquals("VALIDATION_ERROR", exception.getErrorCode());
        assertEquals("Error de validación", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Solo un error", exception.getErrors().get(0));
    }

    @Test
    @DisplayName("Debería heredar correctamente de BaseException")
    void shouldInheritFromBaseException() {

        ValidationException exception = new ValidationException(Arrays.asList("Test"));


        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Debería manejar errores con strings vacíos")
    void shouldHandleEmptyStringErrors() {

        List<String> errors = Arrays.asList("", "Error válido", "");
        ValidationException exception = new ValidationException(errors);


        assertNotNull(exception);
        assertEquals(3, exception.getErrors().size());
        assertEquals("", exception.getErrors().get(0));
        assertEquals("Error válido", exception.getErrors().get(1));
        assertEquals("", exception.getErrors().get(2));
    }

}
