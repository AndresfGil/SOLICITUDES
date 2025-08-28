package co.com.crediya.pragma.solicitudes.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudResponseDTO {


    @Schema(description = "Correo del solicitante", example = "cliente@correo.com")
    private String email;

    @Schema(description = "Documento de identidad del solicitante", example = "123456789")
    private String documentoIdentidad;

    @Schema(description = "Monto solicitado en pesos colombianos", example = "5000000")
    private BigDecimal monto;

    @Schema(description = "Plazo en meses del préstamo", example = "24")
    private Integer plazo;

    @Schema(description = "Identificador del tipo de préstamo", example = "2")
    private Long idTipoPrestamo;

    @Schema(description = "Identificador del estado actual de la solicitud", example = "1")
    private Long idEstado;
}
