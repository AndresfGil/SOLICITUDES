package co.com.crediya.pragma.solicitudes.model.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public abstract class BaseException extends RuntimeException {

    private final String errorCode;
    private final String title;
    private final int statusCode;
    private final List<String> errors;
    private final LocalDateTime timestamp;

    protected BaseException(String message, String errorCode, String title, int statusCode, List<String> errors) {
        super(message);
        this.errorCode = errorCode;
        this.title = title;
        this.statusCode = statusCode;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }
}

