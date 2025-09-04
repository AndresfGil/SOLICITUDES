package co.com.crediya.pragma.solicitudes.model.solicitud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudConUsuarioResponse {
    
    // Campos de la solicitud que sí queremos mostrar
    private BigDecimal monto;
    private Integer plazo;
    private String email;
    private String nombreTipoPrestamo;
    private String estadoSolicitud;
    private Long deudaTotalMensual;
    
    // Campos del usuario
    private String nombre;
    private Long salarioBase;
    
    public static SolicitudConUsuarioResponse fromSolicitudConUsuario(SolicitudConUsuario solicitudConUsuario) {
        return SolicitudConUsuarioResponse.builder()
                .monto(solicitudConUsuario.getMonto())
                .plazo(solicitudConUsuario.getPlazo())
                .email(solicitudConUsuario.getEmail())
                .nombreTipoPrestamo(solicitudConUsuario.getNombreTipoPrestamo())
                .estadoSolicitud(solicitudConUsuario.getEstadoSolicitud())
                .nombre(solicitudConUsuario.getNombre())
                .salarioBase(solicitudConUsuario.getSalarioBase())
                .build();
    }
}
