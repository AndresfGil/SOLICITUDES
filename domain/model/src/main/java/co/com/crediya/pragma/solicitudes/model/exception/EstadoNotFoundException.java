package co.com.crediya.pragma.solicitudes.model.exception;

import java.util.List;

public class EstadoNotFoundException extends BaseException {

    public EstadoNotFoundException(Long idEstado) {
        super(
                "El estado con id " + idEstado + " no existe.",
                "ESTADO_NOT_FOUND",
                "Estado no encontrado",
                404,
                List.of("El estado seleccionado no es válido o no existe")
        );
    }
}
