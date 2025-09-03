package co.com.crediya.pragma.solicitudes.model.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para BaseException")
class BaseExceptionTest {

    @Test
    @DisplayName("Debe crear excepción con todos los campos correctos")
    void shouldCreateExceptionWithAllFieldsCorrectly() {
        String message = "Error de prueba";
        String errorCode = "TEST_ERROR";
        String title = "Error de Test";
        int statusCode = 400;
        List<String> errors = List.of("Error 1", "Error 2");

        TestBaseException exception = new TestBaseException(message, errorCode, title, statusCode, errors);

        assertEquals(message, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(title, exception.getTitle());
        assertEquals(statusCode, exception.getStatusCode());
        assertEquals(errors, exception.getErrors());
        assertNotNull(exception.getTimestamp());
        assertTrue(exception.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    @DisplayName("Debe crear excepción con timestamp actual")
    void shouldCreateExceptionWithCurrentTimestamp() {
        LocalDateTime beforeCreation = LocalDateTime.now();
        
        TestBaseException exception = new TestBaseException("Test", "TEST", "Test", 400, List.of());
        
        LocalDateTime afterCreation = LocalDateTime.now();
        
        assertTrue(exception.getTimestamp().isAfter(beforeCreation.minusSeconds(1)));
        assertTrue(exception.getTimestamp().isBefore(afterCreation.plusSeconds(1)));
    }

    private static class TestBaseException extends BaseException {
        public TestBaseException(String message, String errorCode, String title, int statusCode, List<String> errors) {
            super(message, errorCode, title, statusCode, errors);
        }
    }
}
