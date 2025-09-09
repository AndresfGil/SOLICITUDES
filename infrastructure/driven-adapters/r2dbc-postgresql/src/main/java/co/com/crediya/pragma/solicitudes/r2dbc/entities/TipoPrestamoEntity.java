package co.com.crediya.pragma.solicitudes.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("tipo_prestamos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class TipoPrestamoEntity {


    @Id
    @Column("id_tipo_prestamo")
    private Long idTipoPrestamo;

    private String nombre;

    @Column("monto_minimo")
    private BigDecimal montoMinimo;

    @Column("monto_maximo")
    private BigDecimal montoMaximo;

    @Column("tasa_interes")
    private BigDecimal tasaInteres;

    @Column("validacion_automatica")
    private Boolean validacionAutomatica;

}
