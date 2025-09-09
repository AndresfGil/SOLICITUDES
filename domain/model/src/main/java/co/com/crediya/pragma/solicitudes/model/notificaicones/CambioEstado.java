package co.com.crediya.pragma.solicitudes.model.notificaicones;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CambioEstado {
    Long idSolicitud;
    Long idEstado;
}
