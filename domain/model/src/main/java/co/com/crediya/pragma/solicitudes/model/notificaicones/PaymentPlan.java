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
public class PaymentPlan {
    
    private Integer n;
    private BigDecimal cuota;
    private BigDecimal interes;
    private BigDecimal abonoCapital;
    private BigDecimal saldo;
}
