package co.com.crediya.pragma.solicitudes.usecase.solicitud.capacidad;

import co.com.crediya.pragma.solicitudes.model.auth.UserValidateInfo;
import co.com.crediya.pragma.solicitudes.model.auth.gateways.AuthenticationGateway;
import co.com.crediya.pragma.solicitudes.model.capacidad.ValidacionCapacidad;
import co.com.crediya.pragma.solicitudes.model.notificaicones.gateways.CapacidadEndeudamientoRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.PrestamoAprobado;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.TipoPrestamoRepository;
import co.com.crediya.pragma.solicitudes.model.tipoprestamo.TipoPrestamo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapacidadUseCaseTest {

    @Mock
    private AuthenticationGateway authenticationGateway;

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private CapacidadEndeudamientoRepository capacidadEndeudamientoRepository;

    @Mock
    private TipoPrestamoRepository tipoPrestamoRepository;

    private CapacidadUseCase capacidadUseCase;

    @BeforeEach
    void setUp() {
        capacidadUseCase = new CapacidadUseCase(
                authenticationGateway,
                solicitudRepository,
                capacidadEndeudamientoRepository,
                tipoPrestamoRepository
        );
    }

    @Test
    void validarCapacidad_ValidSolicitud_ReturnsValidacionCapacidad() {
        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idTipoPrestamo(1L)
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .tasaInteres(15)
                .build();

        UserValidateInfo userInfo = UserValidateInfo.builder()
                .exists(true)
                .baseSalary(new BigDecimal("3000000"))
                .build();

        PrestamoAprobado prestamoAprobado = PrestamoAprobado.builder()
                .monto(new BigDecimal("2000000"))
                .plazo(12)
                .tasaInteres(new BigDecimal("0.12"))
                .build();

        ValidacionCapacidad validacionCapacidad = ValidacionCapacidad.builder()
                .salarioBase(new BigDecimal("3000000"))
                .monto(new BigDecimal("5000000"))
                .idSolicitud(1L)
                .email("test@example.com")
                .tasaInteres(15)
                .plazo(24)
                .prestamosAprobados(List.of(prestamoAprobado))
                .build();

        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));
        when(authenticationGateway.validateUser("test@example.com", "12345678"))
                .thenReturn(Mono.just(userInfo));
        when(solicitudRepository.findSolicitudesAprovadas("test@example.com"))
                .thenReturn(Mono.just(List.of(prestamoAprobado)));
        when(capacidadEndeudamientoRepository.sendInfoValidation(any(ValidacionCapacidad.class)))
                .thenReturn(Mono.just(validacionCapacidad));

        StepVerifier.create(capacidadUseCase.validarCapacidad(solicitud))
                .expectNext(validacionCapacidad)
                .verifyComplete();
    }

    @Test
    void validarCapacidad_NoPrestamosAprobados_ReturnsValidacionCapacidad() {
        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idTipoPrestamo(1L)
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .tasaInteres(15)
                .build();

        UserValidateInfo userInfo = UserValidateInfo.builder()
                .exists(true)
                .baseSalary(new BigDecimal("3000000"))
                .build();

        ValidacionCapacidad validacionCapacidad = ValidacionCapacidad.builder()
                .salarioBase(new BigDecimal("3000000"))
                .monto(new BigDecimal("5000000"))
                .idSolicitud(1L)
                .email("test@example.com")
                .tasaInteres(15)
                .plazo(24)
                .prestamosAprobados(List.of())
                .build();

        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));
        when(authenticationGateway.validateUser("test@example.com", "12345678"))
                .thenReturn(Mono.just(userInfo));
        when(solicitudRepository.findSolicitudesAprovadas("test@example.com"))
                .thenReturn(Mono.just(List.of()));
        when(capacidadEndeudamientoRepository.sendInfoValidation(any(ValidacionCapacidad.class)))
                .thenReturn(Mono.just(validacionCapacidad));

        StepVerifier.create(capacidadUseCase.validarCapacidad(solicitud))
                .expectNext(validacionCapacidad)
                .verifyComplete();
    }

    @Test
    void validarCapacidad_MultiplePrestamosAprobados_ReturnsValidacionCapacidad() {
        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idTipoPrestamo(1L)
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .tasaInteres(15)
                .build();

        UserValidateInfo userInfo = UserValidateInfo.builder()
                .exists(true)
                .baseSalary(new BigDecimal("3000000"))
                .build();

        PrestamoAprobado prestamo1 = PrestamoAprobado.builder()
                .monto(new BigDecimal("2000000"))
                .plazo(12)
                .tasaInteres(new BigDecimal("0.12"))
                .build();

        PrestamoAprobado prestamo2 = PrestamoAprobado.builder()
                .monto(new BigDecimal("3000000"))
                .plazo(18)
                .tasaInteres(new BigDecimal("0.15"))
                .build();

        ValidacionCapacidad validacionCapacidad = ValidacionCapacidad.builder()
                .salarioBase(new BigDecimal("3000000"))
                .monto(new BigDecimal("5000000"))
                .idSolicitud(1L)
                .email("test@example.com")
                .tasaInteres(15)
                .plazo(24)
                .prestamosAprobados(List.of(prestamo1, prestamo2))
                .build();

        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));
        when(authenticationGateway.validateUser("test@example.com", "12345678"))
                .thenReturn(Mono.just(userInfo));
        when(solicitudRepository.findSolicitudesAprovadas("test@example.com"))
                .thenReturn(Mono.just(List.of(prestamo1, prestamo2)));
        when(capacidadEndeudamientoRepository.sendInfoValidation(any(ValidacionCapacidad.class)))
                .thenReturn(Mono.just(validacionCapacidad));

        StepVerifier.create(capacidadUseCase.validarCapacidad(solicitud))
                .expectNext(validacionCapacidad)
                .verifyComplete();
    }

    @Test
    void validarCapacidad_TipoPrestamoNotFound_ThrowsException() {
        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idTipoPrestamo(999L)
                .build();

        when(tipoPrestamoRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(capacidadUseCase.validarCapacidad(solicitud))
                .expectError()
                .verify();
    }

    @Test
    void validarCapacidad_AuthenticationError_ThrowsException() {
        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idTipoPrestamo(1L)
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .tasaInteres(15)
                .build();

        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));
        when(authenticationGateway.validateUser("test@example.com", "12345678"))
                .thenReturn(Mono.error(new RuntimeException("Auth error")));

        StepVerifier.create(capacidadUseCase.validarCapacidad(solicitud))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void validarCapacidad_SolicitudesAprobadasError_ThrowsException() {
        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idTipoPrestamo(1L)
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .tasaInteres(15)
                .build();

        UserValidateInfo userInfo = UserValidateInfo.builder()
                .exists(true)
                .baseSalary(new BigDecimal("3000000"))
                .build();

        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));
        when(authenticationGateway.validateUser("test@example.com", "12345678"))
                .thenReturn(Mono.just(userInfo));
        when(solicitudRepository.findSolicitudesAprovadas("test@example.com"))
                .thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(capacidadUseCase.validarCapacidad(solicitud))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void validarCapacidad_CapacidadEndeudamientoError_ThrowsException() {
        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idTipoPrestamo(1L)
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .tasaInteres(15)
                .build();

        UserValidateInfo userInfo = UserValidateInfo.builder()
                .exists(true)
                .baseSalary(new BigDecimal("3000000"))
                .build();

        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));
        when(authenticationGateway.validateUser("test@example.com", "12345678"))
                .thenReturn(Mono.just(userInfo));
        when(solicitudRepository.findSolicitudesAprovadas("test@example.com"))
                .thenReturn(Mono.just(List.of()));
        when(capacidadEndeudamientoRepository.sendInfoValidation(any(ValidacionCapacidad.class)))
                .thenReturn(Mono.error(new RuntimeException("External service error")));

        StepVerifier.create(capacidadUseCase.validarCapacidad(solicitud))
                .expectError(RuntimeException.class)
                .verify();
    }
}
