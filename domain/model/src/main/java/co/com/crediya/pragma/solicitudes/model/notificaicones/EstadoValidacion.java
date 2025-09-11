package co.com.crediya.pragma.solicitudes.model.notificaicones;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EstadoValidacion {
    
    private Long idSolicitud;
    private String status;
    private String statusCode;
    private String email;
    private BigDecimal cuotaNueva;
    private BigDecimal capacidadDisponible;
    private BigDecimal interesMensual;
    private Integer plazoMeses;
    private List<PaymentPlan> paymentPlan;
    private Totales totales;
    private LocalDateTime decidedAt;
}
