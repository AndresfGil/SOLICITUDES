package co.com.crediya.pragma.solicitudes.model.exception;

import java.util.List;

public class ValidationUserException extends BaseException {

    public ValidationUserException(String email, String documentoIdentidad) {
        super(
                "Usuario no válido",
                "USER_VALIDATION_ERROR",
                String.format("El usuario con email '%s' y documento '%s' no es válido o no existe", email, documentoIdentidad),
                400,
                List.of("La informacion del usuario no coincide con el usuario registrado")
        );
    }
}
