package co.com.crediya.pragma.solicitudes.model.auth.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para UnauthorizedUserException")
class UnauthorizedUserExceptionTest {

    @Test
    @DisplayName("Debe crear excepción con constructor personalizado")
    void shouldCreateExceptionWithCustomConstructor() {
        String message = "Mensaje personalizado de error";
        
        UnauthorizedUserException exception = new UnauthorizedUserException(message);
        
        assertEquals(message, exception.getMessage());
        assertEquals("UNAUTHORIZED_USER", exception.getErrorCode());
        assertEquals("Usuario no autorizado", exception.getTitle());
        assertEquals(403, exception.getStatusCode());
        assertEquals(List.of(message), exception.getErrors());
    }

    @Test
    @DisplayName("Debe crear excepción notClientUser con mensaje correcto")
    void shouldCreateNotClientUserExceptionWithCorrectMessage() {
        UnauthorizedUserException exception = UnauthorizedUserException.notClientUser();
        
        assertEquals("Solo los usuarios con rol CLIENTE pueden crear solicitudes", exception.getMessage());
        assertEquals("UNAUTHORIZED_USER", exception.getErrorCode());
        assertEquals("Usuario no autorizado", exception.getTitle());
        assertEquals(403, exception.getStatusCode());
    }

    @Test
    @DisplayName("Debe crear excepción notAdminUser con mensaje correcto")
    void shouldCreateNotAdminUserExceptionWithCorrectMessage() {
        UnauthorizedUserException exception = UnauthorizedUserException.notAdminUser();
        
        assertEquals("Usuario no administrativo", exception.getMessage());
        assertEquals("UNAUTHORIZED_USER", exception.getErrorCode());
        assertEquals("Usuario no autorizado", exception.getTitle());
        assertEquals(403, exception.getStatusCode());
    }

    @Test
    @DisplayName("Debe crear excepción documentMismatch con mensaje correcto")
    void shouldCreateDocumentMismatchExceptionWithCorrectMessage() {
        UnauthorizedUserException exception = UnauthorizedUserException.documentMismatch();
        
        assertEquals("El documento de identidad no coincide con el usuario autenticado", exception.getMessage());
        assertEquals("UNAUTHORIZED_USER", exception.getErrorCode());
        assertEquals("Usuario no autorizado", exception.getTitle());
        assertEquals(403, exception.getStatusCode());
    }
}
