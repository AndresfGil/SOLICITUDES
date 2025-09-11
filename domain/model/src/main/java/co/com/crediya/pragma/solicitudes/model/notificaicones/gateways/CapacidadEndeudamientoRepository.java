package co.com.crediya.pragma.solicitudes.model.notificaicones.gateways;

import co.com.crediya.pragma.solicitudes.model.capacidad.ValidacionCapacidad;
import reactor.core.publisher.Mono;

public interface CapacidadEndeudamientoRepository {
    Mono<ValidacionCapacidad> sendInfoValidation(ValidacionCapacidad validacionCapacidad);



}
