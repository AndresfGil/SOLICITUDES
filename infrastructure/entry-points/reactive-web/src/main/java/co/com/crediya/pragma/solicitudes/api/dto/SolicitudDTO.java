package co.com.crediya.pragma.solicitudes.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record SolicitudDTO(

        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "1.0", message = "El monto debe ser mayor a 0")
        BigDecimal monto,

        @NotNull(message = "El plazo es obligatorio")
        @Positive(message = "El plazo debe ser mayor a 0")
        Integer plazo,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        String email,

        @NotBlank(message = "El documento de identidad es obligatorio")
        String documentoIdentidad,

        @NotNull(message = "El tipo de préstamo es obligatorio")
        Long idTipoPrestamo
) {}
