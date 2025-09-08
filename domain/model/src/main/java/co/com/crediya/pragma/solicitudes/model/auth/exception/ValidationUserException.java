package co.com.crediya.pragma.solicitudes.model.auth.exception;

import co.com.crediya.pragma.solicitudes.model.exception.BaseException;

import java.util.List;

public class ValidationUserException extends BaseException {
    
    public ValidationUserException(String email) {
        super(
                "El documento del usuario" + email + " no coincide.",
                "VALDIATION ERROR",
                "Usuario no encontrado",
                403,
                List.of("La informacion del usuario no coincide con el usuario registrado")
        );
    }

}
