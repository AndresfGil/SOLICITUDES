package co.com.crediya.pragma.solicitudes.r2dbc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PrestamoAprobadoDto {

    @Column("monto")
    private BigDecimal monto;

    @Column("plazo")
    private Integer plazo;

    @Column("tasa_interes")
    private java.math.BigDecimal tasaInteres;
}


