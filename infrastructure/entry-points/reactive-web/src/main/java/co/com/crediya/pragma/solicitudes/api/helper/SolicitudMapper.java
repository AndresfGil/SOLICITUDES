package co.com.crediya.pragma.solicitudes.api.helper;


import co.com.crediya.pragma.solicitudes.api.dto.SolicitudDTO;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import org.springframework.stereotype.Component;

@Component
public class SolicitudMapper {

    public Solicitud toDomain(SolicitudDTO dto) {
        return Solicitud.builder()
                .monto(dto.monto())
                .plazo(dto.plazo())
                .email(lower(trim(dto.email())))
                .documentoIdentidad(trim(dto.documentoIdentidad()))
                .idTipoPrestamo(dto.idTipoPrestamo())
                .build();
    }

    private String trim(String s) {
        return s == null ? null : s.trim();
    }

    private String lower(String s) {
        return s == null ? null : s.toLowerCase();
    }
}
