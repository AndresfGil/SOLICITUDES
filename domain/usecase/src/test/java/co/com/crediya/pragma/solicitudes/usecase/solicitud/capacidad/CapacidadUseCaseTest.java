package co.com.crediya.pragma.solicitudes.usecase.solicitud.capacidad;

import co.com.crediya.pragma.solicitudes.model.auth.UserValidateInfo;
import co.com.crediya.pragma.solicitudes.model.auth.gateways.AuthenticationGateway;
import co.com.crediya.pragma.solicitudes.model.capacidad.ValidacionCapacidad;
import co.com.crediya.pragma.solicitudes.model.notificaicones.gateways.CapacidadEndeudamientoRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.TipoPrestamoRepository;
import co.com.crediya.pragma.solicitudes.model.tipoprestamo.TipoPrestamo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CapacidadUseCaseTest {

    private AuthenticationGateway authenticationGateway;
    private SolicitudRepository solicitudRepository;
    private CapacidadEndeudamientoRepository capacidadEndeudamientoRepository;
    private TipoPrestamoRepository tipoPrestamoRepository;
    private CapacidadUseCase useCase;

    @BeforeEach
    void setUp() {
        authenticationGateway = mock(AuthenticationGateway.class);
        solicitudRepository = mock(SolicitudRepository.class);
        capacidadEndeudamientoRepository = mock(CapacidadEndeudamientoRepository.class);
        tipoPrestamoRepository = mock(TipoPrestamoRepository.class);
        useCase = new CapacidadUseCase(authenticationGateway, solicitudRepository, capacidadEndeudamientoRepository, tipoPrestamoRepository);
    }

    @Test
    void validarCapacidad_success_sendsToRepository() {
        var solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .email("a@b.com")
                .documentoIdentidad("123")
                .monto(new BigDecimal("10000"))
                .plazo(12)
                .idTipoPrestamo(10L)
                .build();

        var tipo = TipoPrestamo.builder().idTipoPrestamo(10L).tasaInteres(5).build();

        when(tipoPrestamoRepository.findById(10L)).thenReturn(Mono.just(tipo));
        when(authenticationGateway.validateUser("a@b.com", "123")).thenReturn(Mono.just(UserValidateInfo.builder().exists(true).baseSalary(new BigDecimal("50000")).build()));
        when(solicitudRepository.findSolicitudesAprovadas("a@b.com")).thenReturn(Mono.just(List.of()));
        when(capacidadEndeudamientoRepository.sendInfoValidation(any(ValidacionCapacidad.class)))
                .thenAnswer(inv -> Mono.just((ValidacionCapacidad) inv.getArgument(0)));

        StepVerifier.create(useCase.validarCapacidad(solicitud))
                .assertNext(vc -> {
                    assertThat(vc.getEmail()).isEqualTo("a@b.com");
                    assertThat(vc.getIdSolicitud()).isEqualTo(1L);
                    assertThat(vc.getTasaInteres()).isEqualTo(5);
                    assertThat(vc.getMonto()).isEqualTo(new BigDecimal("10000"));
                })
                .verifyComplete();
    }

    @Test
    void validarCapacidad_tipoPrestamoNotFound_returnsEmpty() {
        var solicitud = Solicitud.builder()
                .idSolicitud(2L)
                .email("x@y.com")
                .documentoIdentidad("321")
                .monto(new BigDecimal("5000"))
                .plazo(6)
                .idTipoPrestamo(99L)
                .build();

        when(tipoPrestamoRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.validarCapacidad(solicitud))
                .verifyComplete();
    }
}


