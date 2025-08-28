package co.com.crediya.pragma.solicitudes.model.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;


import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para la clase TipoPrestamoNotFoundException")
class TipoPrestamoNotFoundExceptionTest {


    @Test
    @DisplayName("Debería crear una excepción con ID válido")
    void shouldCreateExceptionWithValidId() {

        TipoPrestamoNotFoundException exception = new TipoPrestamoNotFoundException(123L);


        assertNotNull(exception);
        assertEquals("El tipo de préstamo con id 123 no existe.", exception.getMessage());
        assertEquals("TIPO_PRESTAMO_NOT_FOUND", exception.getErrorCode());
        assertEquals("Tipo de préstamo inválido", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(1, exception.getErrors().size());
        assertEquals("El tipo de préstamo seleccionado no es válido", exception.getErrors().get(0));
        assertNotNull(exception.getTimestamp());
    }

    @Test
    @DisplayName("Debería crear una excepción con ID cero")
    void shouldCreateExceptionWithZeroId() {

        TipoPrestamoNotFoundException exception = new TipoPrestamoNotFoundException(0L);


        assertNotNull(exception);
        assertEquals("El tipo de préstamo con id 0 no existe.", exception.getMessage());
        assertEquals("TIPO_PRESTAMO_NOT_FOUND", exception.getErrorCode());
        assertEquals("Tipo de préstamo inválido", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(1, exception.getErrors().size());
        assertEquals("El tipo de préstamo seleccionado no es válido", exception.getErrors().get(0));
    }

    @Test
    @DisplayName("Debería crear una excepción con ID negativo")
    void shouldCreateExceptionWithNegativeId() {

        TipoPrestamoNotFoundException exception = new TipoPrestamoNotFoundException(-1L);


        assertNotNull(exception);
        assertEquals("El tipo de préstamo con id -1 no existe.", exception.getMessage());
        assertEquals("TIPO_PRESTAMO_NOT_FOUND", exception.getErrorCode());
        assertEquals("Tipo de préstamo inválido", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(1, exception.getErrors().size());
        assertEquals("El tipo de préstamo seleccionado no es válido", exception.getErrors().get(0));
    }

    @Test
    @DisplayName("Debería heredar correctamente de BaseException")
    void shouldInheritFromBaseException() {

        TipoPrestamoNotFoundException exception = new TipoPrestamoNotFoundException(999L);


        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }
}
