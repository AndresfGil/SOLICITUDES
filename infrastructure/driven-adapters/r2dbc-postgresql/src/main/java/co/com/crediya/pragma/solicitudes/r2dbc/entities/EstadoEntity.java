package co.com.crediya.pragma.solicitudes.r2dbc.entities;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("estados")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)

public class EstadoEntity {

    @Id
    @Column("id_estado")
    private Long idEstado;

    @Column("nombre_estado")
    private String nombreEstado;

    @Column("descripcion_estado")
    private String descripcionEstado;

}
