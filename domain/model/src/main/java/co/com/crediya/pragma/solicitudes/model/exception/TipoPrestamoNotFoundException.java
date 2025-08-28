package co.com.crediya.pragma.solicitudes.model.exception;


import java.util.List;

public class TipoPrestamoNotFoundException extends BaseException {

    public TipoPrestamoNotFoundException(Long idTipoPrestamo) {
        super(
                "El tipo de préstamo con id " + idTipoPrestamo + " no existe.",
                "TIPO_PRESTAMO_NOT_FOUND",
                "Tipo de préstamo inválido",
                400,
                List.of("El tipo de préstamo seleccionado no es válido")
        );
    }
}
