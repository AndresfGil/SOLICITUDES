package co.com.crediya.pragma.solicitudes.usecase.solicitud.capacidad;

import co.com.crediya.pragma.solicitudes.model.auth.UserValidateInfo;
import co.com.crediya.pragma.solicitudes.model.auth.gateways.AuthenticationGateway;
import co.com.crediya.pragma.solicitudes.model.capacidad.ValidacionCapacidad;
import co.com.crediya.pragma.solicitudes.model.notificaicones.gateways.CapacidadEndeudamientoRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.TipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapacidadUseCase {
    private final AuthenticationGateway authenticationGateway;
    private final SolicitudRepository solicitudRepository;
    private final CapacidadEndeudamientoRepository capacidadEndeudamientoRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;

    public Mono<ValidacionCapacidad> validarCapacidad(Solicitud solicitud){
        return tipoPrestamoRepository.findById(solicitud.getIdTipoPrestamo())
                .flatMap(tipo -> authenticationGateway.validateUser(solicitud.getEmail(), solicitud.getDocumentoIdentidad())
                        .map(UserValidateInfo::baseSalary)
                        .flatMap(salario -> solicitudRepository.findSolicitudesAprovadas(solicitud.getEmail())
                                .map(aprobados -> ValidacionCapacidad.builder()
                                        .salarioBase(salario)
                                        .monto(solicitud.getMonto())
                                        .idSolicitud(solicitud.getIdSolicitud())
                                        .email(solicitud.getEmail())
                                        .tasaInteres(tipo.getTasaInteres())
                                        .plazo(solicitud.getPlazo())
                                        .prestamosAprobados(aprobados)
                                        .build())
                                .flatMap(capacidadEndeudamientoRepository::sendInfoValidation)
                        )
                );
    }
}
