package co.com.crediya.pragma.solicitudes.api.helper;
import jakarta.validation.ConstraintViolation;
import co.com.crediya.pragma.solicitudes.model.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor

public class DtoValidator {

    private final jakarta.validation.Validator validator;

    public <T> Mono<T> validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            List<String> errors = violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .toList();

            return Mono.error(new ValidationException(errors));
        }
        return Mono.just(dto);
    }
}