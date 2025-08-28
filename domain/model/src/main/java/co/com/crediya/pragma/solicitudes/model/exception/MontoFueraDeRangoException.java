package co.com.crediya.pragma.solicitudes.model.exception;

import java.math.BigDecimal;
import java.util.List;

public class MontoFueraDeRangoException extends BaseException {

    public MontoFueraDeRangoException(BigDecimal montoSolicitado, BigDecimal montoMinimo, BigDecimal montoMaximo) {
        super(
                "El monto solicitado $" + montoSolicitado + " está fuera del rango permitido para este tipo de préstamo.",
                "MONTO_FUERA_DE_RANGO",
                "Monto fuera de rango",
                400,
                List.of("El monto debe estar entre $" + montoMinimo + " y $" + montoMaximo)
        );
    }
}
