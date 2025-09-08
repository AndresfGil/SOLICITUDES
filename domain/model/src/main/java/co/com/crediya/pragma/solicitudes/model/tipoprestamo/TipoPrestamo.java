package co.com.crediya.pragma.solicitudes.model.tipoprestamo;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)

public class TipoPrestamo {

    private Long idTipoPrestamo;
    private String nombre;
    private Integer montoMinimo;
    private Integer montoMaximo;
    private Integer tasaInteres;
    private Boolean validacionAutomatica;

}
