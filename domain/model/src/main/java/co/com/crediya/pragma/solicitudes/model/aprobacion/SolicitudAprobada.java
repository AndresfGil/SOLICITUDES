package co.com.crediya.pragma.solicitudes.model.aprobacion;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SolicitudAprobada {
    private String metrica;
    private BigDecimal monto;
}
