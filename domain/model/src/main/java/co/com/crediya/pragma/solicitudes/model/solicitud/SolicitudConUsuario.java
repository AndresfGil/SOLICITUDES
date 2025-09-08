package co.com.crediya.pragma.solicitudes.model.solicitud;

import co.com.crediya.pragma.solicitudes.model.page.SolicitudFieldsPage;
import co.com.crediya.pragma.solicitudes.model.page.UsersForPageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudConUsuario {
    
    // Campos de Solicitud
    private Long idSolicitud;
    private BigDecimal monto;
    private Integer plazo;
    private String email;
    private String documentoIdentidad;
    private Long idEstado;
    private Long idTipoPrestamo;
    
    // Campos enriquecidos de SolicitudEnriquecida
    private String nombreTipoPrestamo;
    private String estadoSolicitud;
    private Integer tasaInteres;
    
    // Campos del usuario
    private String nombre;
    private Long salarioBase;


    public static SolicitudConUsuario fromSolicitudFieldsAndUser(
            SolicitudFieldsPage solicitudFields,
            UsersForPageResponse userInfo) {
        
        SolicitudConUsuario result = new SolicitudConUsuario();
        
        // datos básicos de solicitud desde SolicitudFieldsPage
        result.setIdSolicitud(null); // No disponible en SolicitudFieldsPage
        result.setMonto(solicitudFields.monto());
        result.setPlazo(solicitudFields.plazo());
        result.setEmail(solicitudFields.email());
        result.setDocumentoIdentidad(null); // No disponible en SolicitudFieldsPage
        result.setIdEstado(null); // No disponible en SolicitudFieldsPage
        result.setIdTipoPrestamo(null); // No disponible en SolicitudFieldsPage
        
        // campos enriquecidos desde SolicitudFieldsPage
        result.setNombreTipoPrestamo(solicitudFields.tipoPrestamo());
        result.setEstadoSolicitud(solicitudFields.estado());
        result.setTasaInteres(null); // No disponible en SolicitudFieldsPage
        
        // información del usuario
        if (userInfo != null) {
            result.setNombre(userInfo.name());
            result.setSalarioBase(userInfo.baseSalary());
        }
        
        return result;
    }
}
