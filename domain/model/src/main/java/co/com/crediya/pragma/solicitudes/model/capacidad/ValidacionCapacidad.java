package co.com.crediya.pragma.solicitudes.model.capacidad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import co.com.crediya.pragma.solicitudes.model.solicitud.PrestamoAprobado;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ValidacionCapacidad {

    private BigDecimal salarioBase;
    private BigDecimal monto;
    private Integer tasaInteres;
    private Integer plazo;
    private List<PrestamoAprobado> prestamosAprobados;
    private String email;
    private Long idSolicitud;
}
