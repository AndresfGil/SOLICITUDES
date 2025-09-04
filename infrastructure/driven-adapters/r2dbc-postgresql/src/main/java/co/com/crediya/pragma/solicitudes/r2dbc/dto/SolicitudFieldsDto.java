package co.com.crediya.pragma.solicitudes.r2dbc.dto;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SolicitudFieldsDto {

    @Column("monto")
    private BigDecimal monto;

    @Column("plazo")
    private Integer plazo;

    @Column("email")
    private String email;

    @Column("tipo_prestamo")
    private String tipoPrestamo;

    @Column("estado")
    private String estado;
}