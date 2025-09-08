package co.com.crediya.pragma.solicitudes.model.auth.exception;

import co.com.crediya.pragma.solicitudes.model.exception.BaseException;

import java.util.List;

public class UnauthorizedException extends BaseException {

    public UnauthorizedException(String message) {
        super(
                message,
                "UNAUTHORIZED",
                "No autorizado",
                403,
                List.of(message)
        );
    }

}
