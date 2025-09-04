package co.com.crediya.pragma.solicitudes.model.page;

import lombok.*;

import java.math.BigDecimal;

@Builder
public record SolicitudFieldsPage(
        BigDecimal monto,
        Integer plazo,
        String email,
        String tipoPrestamo,
        String estado
) {
}