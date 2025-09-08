package co.com.crediya.pragma.solicitudes.api.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String code;
    private String message;
    private int status;
    private String path;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant timestamp;

    private String correlationId;
    private List<ErrorDetail> details;

    public static ErrorResponse of(String code, String message, int status, String path,
                                   List<ErrorDetail> details, String correlationId) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .status(status)
                .path(path)
                .timestamp(Instant.now())
                .correlationId(correlationId)
                .details(details)
                .build();
    }
}
