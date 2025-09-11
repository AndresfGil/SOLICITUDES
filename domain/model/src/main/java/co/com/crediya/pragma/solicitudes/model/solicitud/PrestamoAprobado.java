package co.com.crediya.pragma.solicitudes.model.solicitud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PrestamoAprobado {
    private BigDecimal monto;
    private Integer plazo;
    private BigDecimal tasaInteres;
}


