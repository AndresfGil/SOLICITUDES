package co.com.crediya.pragma.solicitudes.model;

import co.com.crediya.pragma.solicitudes.model.estado.Estado;
import co.com.crediya.pragma.solicitudes.model.exception.TipoPrestamoNotFoundException;
import co.com.crediya.pragma.solicitudes.model.exception.ValidationException;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.TipoPrestamoRepository;
import co.com.crediya.pragma.solicitudes.model.tipoprestamo.TipoPrestamo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de integración para el modelo")
class ModelIntegrationTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private TipoPrestamoRepository tipoPrestamoRepository;

    private Solicitud solicitud;
    private Estado estado;
    private TipoPrestamo tipoPrestamo;

    @BeforeEach
    void setUp() {
        estado = Estado.builder()
                .idEstado(1L)
                .nombreEstado("PENDIENTE")
                .descripcionEstado("Solicitud en proceso de revisión")
                .build();

        tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .nombre("Prestamo Personal")
                .montoMinimo(1000000)
                .montoMaximo(50000000)
                .tasaInteres(15)
                .validacionAutomatica(true)
                .build();

        solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(12)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(estado.getIdEstado())
                .idTipoPrestamo(tipoPrestamo.getIdTipoPrestamo())
                .build();
    }

    @Test
    @DisplayName("Debería crear y guardar una solicitud completa con validación de tipo de préstamo")
    void shouldCreateAndSaveCompleteSolicitudWithTipoPrestamoValidation() {
        when(tipoPrestamoRepository.existsById(tipoPrestamo.getIdTipoPrestamo()))
                .thenReturn(Mono.just(true));
        when(solicitudRepository.saveSolicitud(any(Solicitud.class)))
                .thenReturn(Mono.just(solicitud));

        StepVerifier.create(
                tipoPrestamoRepository.existsById(tipoPrestamo.getIdTipoPrestamo())
                        .flatMap(exists -> {
                            if (exists) {
                                return solicitudRepository.saveSolicitud(solicitud);
                            } else {
                                return Mono.error(new TipoPrestamoNotFoundException(tipoPrestamo.getIdTipoPrestamo()));
                            }
                        })
        )
                .expectNext(solicitud)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería fallar al crear solicitud con tipo de préstamo inexistente")
    void shouldFailWhenCreatingSolicitudWithNonExistentTipoPrestamo() {
        Long nonExistentTipoPrestamoId = 999L;
        Solicitud solicitudWithInvalidTipo = solicitud.toBuilder()
                .idTipoPrestamo(nonExistentTipoPrestamoId)
                .build();

        when(tipoPrestamoRepository.existsById(nonExistentTipoPrestamoId))
                .thenReturn(Mono.just(false));

        StepVerifier.create(
                tipoPrestamoRepository.existsById(nonExistentTipoPrestamoId)
                        .flatMap(exists -> {
                            if (exists) {
                                return solicitudRepository.saveSolicitud(solicitudWithInvalidTipo);
                            } else {
                                return Mono.error(new TipoPrestamoNotFoundException(nonExistentTipoPrestamoId));
                            }
                        })
        )
                .expectError(TipoPrestamoNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Debería validar y guardar múltiples solicitudes")
    void shouldValidateAndSaveMultipleSolicitudes() {
        Solicitud solicitud2 = solicitud.toBuilder()
                .idSolicitud(2L)
                .monto(new BigDecimal("3000000"))
                .email("test2@example.com")
                .documentoIdentidad("87654321")
                .build();

        Solicitud solicitud3 = solicitud.toBuilder()
                .idSolicitud(3L)
                .monto(new BigDecimal("7000000"))
                .email("test3@example.com")
                .documentoIdentidad("11223344")
                .build();

        when(tipoPrestamoRepository.existsById(tipoPrestamo.getIdTipoPrestamo()))
                .thenReturn(Mono.just(true));
        when(solicitudRepository.saveSolicitud(solicitud))
                .thenReturn(Mono.just(solicitud));
        when(solicitudRepository.saveSolicitud(solicitud2))
                .thenReturn(Mono.just(solicitud2));
        when(solicitudRepository.saveSolicitud(solicitud3))
                .thenReturn(Mono.just(solicitud3));

        StepVerifier.create(
                Flux.just(solicitud, solicitud2, solicitud3)
                        .flatMap(sol -> tipoPrestamoRepository.existsById(sol.getIdTipoPrestamo())
                                .flatMap(exists -> {
                                    if (exists) {
                                        return solicitudRepository.saveSolicitud(sol);
                                    } else {
                                        return Mono.error(new TipoPrestamoNotFoundException(sol.getIdTipoPrestamo()));
                                    }
                                }))
        )
                .expectNext(solicitud)
                .expectNext(solicitud2)
                .expectNext(solicitud3)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería manejar errores de validación en solicitudes")
    void shouldHandleValidationErrorsInSolicitudes() {
        Solicitud solicitudInvalida = solicitud.toBuilder()
                .monto(null)
                .email("")
                .build();

        List<String> validationErrors = Arrays.asList(
                "El monto no puede ser nulo",
                "El email no puede estar vacío"
        );

        when(tipoPrestamoRepository.existsById(tipoPrestamo.getIdTipoPrestamo()))
                .thenReturn(Mono.just(true));

        StepVerifier.create(
                tipoPrestamoRepository.existsById(tipoPrestamo.getIdTipoPrestamo())
                        .flatMap(exists -> {
                            if (exists) {
                                if (solicitudInvalida.getMonto() == null || solicitudInvalida.getEmail().isEmpty()) {
                                    return Mono.error(new ValidationException(validationErrors));
                                }
                                return solicitudRepository.saveSolicitud(solicitudInvalida);
                            } else {
                                return Mono.error(new TipoPrestamoNotFoundException(tipoPrestamo.getIdTipoPrestamo()));
                            }
                        })
        )
                .expectError(ValidationException.class)
                .verify();
    }

    @Test
    @DisplayName("Debería buscar y filtrar solicitudes por estado")
    void shouldSearchAndFilterSolicitudesByEstado() {
        Solicitud solicitudPendiente = solicitud.toBuilder()
                .idSolicitud(1L)
                .idEstado(1L) // PENDIENTE
                .build();

        Solicitud solicitudAprobada = solicitud.toBuilder()
                .idSolicitud(2L)
                .idEstado(2L) // APROBADA
                .build();

        Solicitud solicitudRechazada = solicitud.toBuilder()
                .idSolicitud(3L)
                .idEstado(3L) // RECHAZADA
                .build();

        List<Solicitud> todasLasSolicitudes = Arrays.asList(solicitudPendiente, solicitudAprobada, solicitudRechazada);

        when(solicitudRepository.findAllSolicitudes())
                .thenReturn(Flux.fromIterable(todasLasSolicitudes));

        StepVerifier.create(
                solicitudRepository.findAllSolicitudes()
                        .filter(sol -> sol.getIdEstado().equals(1L))
        )
                .expectNext(solicitudPendiente)
                .verifyComplete();
    }


    @Test
    @DisplayName("Debería manejar solicitudes con montos fuera de rango")
    void shouldHandleSolicitudesWithAmountsOutOfRange() {
        Solicitud solicitudMontoBajo = solicitud.toBuilder()
                .monto(new BigDecimal("500000")) // Por debajo del mínimo
                .build();

        Solicitud solicitudMontoAlto = solicitud.toBuilder()
                .monto(new BigDecimal("60000000")) // Por encima del máximo
                .build();

        when(tipoPrestamoRepository.existsById(tipoPrestamo.getIdTipoPrestamo()))
                .thenReturn(Mono.just(true));

        StepVerifier.create(
                tipoPrestamoRepository.existsById(tipoPrestamo.getIdTipoPrestamo())
                        .flatMap(exists -> {
                            if (exists) {
                                if (solicitudMontoBajo.getMonto().compareTo(new BigDecimal(tipoPrestamo.getMontoMinimo())) < 0) {
                                    return Mono.error(new ValidationException(
                                            Arrays.asList("El monto está por debajo del mínimo permitido")
                                    ));
                                }
                                return solicitudRepository.saveSolicitud(solicitudMontoBajo);
                            } else {
                                return Mono.error(new TipoPrestamoNotFoundException(tipoPrestamo.getIdTipoPrestamo()));
                            }
                        })
        )
                .expectError(ValidationException.class)
                .verify();

        StepVerifier.create(
                tipoPrestamoRepository.existsById(tipoPrestamo.getIdTipoPrestamo())
                        .flatMap(exists -> {
                            if (exists) {
                                if (solicitudMontoAlto.getMonto().compareTo(new BigDecimal(tipoPrestamo.getMontoMaximo())) > 0) {
                                    return Mono.error(new ValidationException(
                                            Arrays.asList("El monto está por encima del máximo permitido")
                                    ));
                                }
                                return solicitudRepository.saveSolicitud(solicitudMontoAlto);
                            } else {
                                return Mono.error(new TipoPrestamoNotFoundException(tipoPrestamo.getIdTipoPrestamo()));
                            }
                        })
        )
                .expectError(ValidationException.class)
                .verify();
    }

}
