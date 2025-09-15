package co.com.crediya.pragma.solicitudes.model.exception;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionsTest {

    @Test
    void montoFueraDeRango_populatesBaseFields() {
        var ex = new MontoFueraDeRangoException(new BigDecimal("5000"), new BigDecimal("1000"), new BigDecimal("4000"));
        assertEquals("MONTO_FUERA_DE_RANGO", ex.getErrorCode());
        assertEquals(400, ex.getStatusCode());
        assertNotNull(ex.getErrors());
        assertTrue(ex.getMessage().contains("fuera del rango"));
        assertNotNull(ex.getTimestamp());
    }

    @Test
    void tipoPrestamoNotFound_populatesBaseFields() {
        var ex = new TipoPrestamoNotFoundException(99L);
        assertEquals("TIPO_PRESTAMO_NOT_FOUND", ex.getErrorCode());
        assertEquals(400, ex.getStatusCode());
        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    void solicitudNotFound_populatesBaseFields() {
        var ex = new SolicitudNotFoundException(7L);
        assertEquals("SOLICITUD_NOT_FOUND", ex.getErrorCode());
        assertEquals(404, ex.getStatusCode());
        assertTrue(ex.getMessage().contains("7"));
    }

    @Test
    void estadoNotFound_populatesBaseFields() {
        var ex = new EstadoNotFoundException(3L);
        assertEquals("ESTADO_NOT_FOUND", ex.getErrorCode());
        assertEquals(404, ex.getStatusCode());
        assertTrue(ex.getMessage().contains("3"));
    }

    @Test
    void validationException_populatesBaseFields() {
        var ex = new ValidationException(java.util.List.of("e1", "e2"));
        assertEquals("VALIDATION_ERROR", ex.getErrorCode());
        assertEquals(400, ex.getStatusCode());
        assertEquals(2, ex.getErrors().size());
    }

    @Test
    void validationUserException_populatesBaseFields() {
        var ex = new ValidationUserException("a@b.com", "123");
        assertEquals("USER_VALIDATION_ERROR", ex.getErrorCode());
        assertEquals(400, ex.getStatusCode());
    }

    @Test
    void invalidCredentials_populatesBaseFields() {
        var ex = new InvalidCredentialsException("user@test.com");
        assertEquals("INVALID_CREDENTIALS", ex.getErrorCode());
        assertEquals(401, ex.getStatusCode());
    }

    @Test
    void invalidToken_populatesBaseFields() {
        var ex = new InvalidTokenException("Bad token");
        assertEquals("INVALID_TOKEN", ex.getErrorCode());
        assertEquals(401, ex.getStatusCode());
    }
}


