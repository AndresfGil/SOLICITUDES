package co.com.crediya.pragma.solicitudes.model.auth;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UserValidateInfo(
        boolean exists,
        BigDecimal baseSalary
) {
}


