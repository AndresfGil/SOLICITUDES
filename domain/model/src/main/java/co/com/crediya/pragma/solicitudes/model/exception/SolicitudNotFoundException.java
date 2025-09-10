package co.com.crediya.pragma.solicitudes.model.exception;

import java.util.List;

public class SolicitudNotFoundException extends BaseException {

    public SolicitudNotFoundException(Long idSolicitud) {
        super(
                "La solicitud con id " + idSolicitud + " no existe.",
                "SOLICITUD_NOT_FOUND",
                "Solicitud no encontrada",
                404,
                List.of("La solicitud seleccionada no existe o ha sido eliminada")
        );
    }
}
