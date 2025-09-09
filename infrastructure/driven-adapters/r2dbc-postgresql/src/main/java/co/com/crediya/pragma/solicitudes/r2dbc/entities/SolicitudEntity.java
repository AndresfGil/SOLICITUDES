package co.com.crediya.pragma.solicitudes.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("solicitudes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class SolicitudEntity {

    @Id
    @Column("id_solicitud")
    private Long idSolicitud;

    private BigDecimal monto;

    private Integer plazo;

    private String email;

    @Column("documento_identidad")
    private String documentoIdentidad;

    @Column("id_estado")
    private Long idEstado;

    @Column("id_tipo_prestamo")
    private Long idTipoPrestamo;
}
