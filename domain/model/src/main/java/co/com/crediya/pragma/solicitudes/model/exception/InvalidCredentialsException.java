package co.com.crediya.pragma.solicitudes.model.exception;

import java.util.List;

public class InvalidCredentialsException extends BaseException {
    public InvalidCredentialsException(String email) {
        super(
                "Credenciales inválidas para el email: " + email,
                "INVALID_CREDENTIALS",
                "Credenciales inválidas",
                401,
                List.of("El email o contraseña proporcionados son incorrectos")
        );
    }
}
