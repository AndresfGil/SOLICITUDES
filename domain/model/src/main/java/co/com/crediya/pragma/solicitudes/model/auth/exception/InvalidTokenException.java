package co.com.crediya.pragma.solicitudes.model.auth.exception;

import co.com.crediya.pragma.solicitudes.model.exception.BaseException;

import java.util.List;

public class InvalidTokenException extends BaseException {
    public InvalidTokenException(String message) {
        super(
                message,
                "INVALID_TOKEN",
                "Token invalido",
                401,
                List.of(message)
        );
    }
}
