package co.com.crediya.pragma.solicitudes.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CambioEstadoDTO(
        @NotNull(message = "El ID de la solicitud es obligatorio")
        @Positive(message = "El ID de la solicitud debe ser mayor a 0")
        Long idSolicitud,
        
        @NotNull(message = "El ID del estado es obligatorio")
        @Positive(message = "El ID del estado debe ser mayor a 0")
        Long idEstado
) {}
