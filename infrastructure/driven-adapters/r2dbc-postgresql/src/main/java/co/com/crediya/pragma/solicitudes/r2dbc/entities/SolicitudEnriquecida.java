package co.com.crediya.pragma.solicitudes.r2dbc.entities;

import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SolicitudEnriquecida extends Solicitud {
    
    private String nombreTipoPrestamo;
    private String estadoSolicitud;
    private Integer tasaInteres;
    
    public SolicitudEnriquecida(Solicitud solicitud) {
        this.setIdSolicitud(solicitud.getIdSolicitud());
        this.setMonto(solicitud.getMonto());
        this.setPlazo(solicitud.getPlazo());
        this.setEmail(solicitud.getEmail());
        this.setDocumentoIdentidad(solicitud.getDocumentoIdentidad());
        this.setIdEstado(solicitud.getIdEstado());
        this.setIdTipoPrestamo(solicitud.getIdTipoPrestamo());
    }
}
