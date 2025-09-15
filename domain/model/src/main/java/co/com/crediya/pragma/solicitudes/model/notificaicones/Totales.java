package co.com.crediya.pragma.solicitudes.model.notificaicones;

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
public class Totales {
    
    private BigDecimal totalIntereses;
    private BigDecimal totalPagado;
}
