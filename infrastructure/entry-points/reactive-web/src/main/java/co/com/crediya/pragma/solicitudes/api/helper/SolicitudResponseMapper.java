package co.com.crediya.pragma.solicitudes.api.helper;


import co.com.crediya.pragma.solicitudes.api.dto.SolicitudResponseDTO;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import org.springframework.stereotype.Component;

@Component
public class SolicitudResponseMapper {

    public SolicitudResponseDTO toResponse(Solicitud solicitud) {
        return new SolicitudResponseDTO(
                solicitud.getEmail(),
                solicitud.getDocumentoIdentidad(),
                solicitud.getMonto(),
                solicitud.getPlazo(),
                solicitud.getIdTipoPrestamo(),
                solicitud.getIdEstado()
        );
    }
}
