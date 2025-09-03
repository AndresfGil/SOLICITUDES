package co.com.crediya.pragma.solicitudes.model.auth.exception;

import co.com.crediya.pragma.solicitudes.model.exception.BaseException;

import java.util.List;

public class UnauthorizedUserException extends BaseException {
    
    public UnauthorizedUserException(String message) {
        super(
                message,
                "UNAUTHORIZED_USER",
                "Usuario no autorizado",
                403,
                List.of(message)
        );
    }
    
    public static UnauthorizedUserException notClientUser() {
        return new UnauthorizedUserException("Solo los usuarios con rol CLIENTE pueden crear solicitudes");
    }
    
    public static UnauthorizedUserException notAdminUser() {
        return new UnauthorizedUserException("Usuario no administrativo");
    }
    
    public static UnauthorizedUserException documentMismatch() {
        return new UnauthorizedUserException("El documento de identidad no coincide con el usuario autenticado");
    }
}
