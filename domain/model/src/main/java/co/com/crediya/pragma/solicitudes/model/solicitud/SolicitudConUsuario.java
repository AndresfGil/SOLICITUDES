package co.com.crediya.pragma.solicitudes.model.solicitud;

import co.com.crediya.pragma.solicitudes.model.page.SolicitudFieldsPage;
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
    
    public static SolicitudConUsuario fromSolicitudAndUser(
            Solicitud solicitud,
            co.com.crediya.pragma.solicitudes.model.auth.gateways.AuthenticationGateway.UserSolicitudInfo userInfo) {
        
        SolicitudConUsuario result = new SolicitudConUsuario();
        
        // datos básicos de solicitud
        result.setIdSolicitud(solicitud.getIdSolicitud());
        result.setMonto(solicitud.getMonto());
        result.setPlazo(solicitud.getPlazo());
        result.setEmail(solicitud.getEmail());
        result.setDocumentoIdentidad(solicitud.getDocumentoIdentidad());
        result.setIdEstado(solicitud.getIdEstado());
        result.setIdTipoPrestamo(solicitud.getIdTipoPrestamo());
        
        // información del usuario
        if (userInfo != null) {
            result.setNombre(userInfo.name());
            result.setSalarioBase(userInfo.baseSalary());
        }
        
        return result;
    }
    
    public static SolicitudConUsuario fromSolicitudAndUserWithEnrichment(
            Solicitud solicitud,
            co.com.crediya.pragma.solicitudes.model.auth.gateways.AuthenticationGateway.UserSolicitudInfo userInfo,
            String nombreTipoPrestamo,
            String estadoSolicitud,
            Integer tasaInteres) {
        
        SolicitudConUsuario result = fromSolicitudAndUser(solicitud, userInfo);
        
        result.setNombreTipoPrestamo(nombreTipoPrestamo);
        result.setEstadoSolicitud(estadoSolicitud);
        result.setTasaInteres(tasaInteres);
        
        return result;
    }
    
    public static SolicitudConUsuario fromSolicitudFieldsAndUser(
            SolicitudFieldsPage solicitudFields,
            co.com.crediya.pragma.solicitudes.model.auth.gateways.AuthenticationGateway.UserSolicitudInfo userInfo) {
        
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
