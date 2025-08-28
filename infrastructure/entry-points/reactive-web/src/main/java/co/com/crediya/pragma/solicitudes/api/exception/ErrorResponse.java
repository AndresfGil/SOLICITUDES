package co.com.crediya.pragma.solicitudes.api.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ErrorResponse {
    private String errorCode;
    private String tittle;
    private String message;
    private List<String> errors;
    private HttpStatus status;
    private LocalDateTime timestamp;
}
