package co.com.crediya.pragma.solicitudes.model.page;

import lombok.Builder;


@Builder
public record UsersForPageResponse(
        String name,
        String email,
        Long baseSalary
) {
}
