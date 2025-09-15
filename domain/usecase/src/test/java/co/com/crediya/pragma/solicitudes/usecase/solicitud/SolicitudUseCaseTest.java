package co.com.crediya.pragma.solicitudes.usecase.solicitud;

import co.com.crediya.pragma.solicitudes.model.auth.UserValidateInfo;
import co.com.crediya.pragma.solicitudes.model.auth.gateways.AuthenticationGateway;
import co.com.crediya.pragma.solicitudes.model.exception.MontoFueraDeRangoException;
import co.com.crediya.pragma.solicitudes.model.exception.TipoPrestamoNotFoundException;
import co.com.crediya.pragma.solicitudes.model.exception.ValidationUserException;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudFieldsPage;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudPage;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudPageRequest;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.TipoPrestamoRepository;
import co.com.crediya.pragma.solicitudes.model.tipoprestamo.TipoPrestamo;
import co.com.crediya.pragma.solicitudes.usecase.solicitud.capacidad.CapacidadUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SolicitudUseCaseTest {

    private SolicitudRepository solicitudRepository;
    private TipoPrestamoRepository tipoPrestamoRepository;
    private AuthenticationGateway authenticationGateway;
    private CapacidadUseCase capacidadUseCase;
    private SolicitudUseCase useCase;

    @BeforeEach
    void setUp() {
        solicitudRepository = mock(SolicitudRepository.class);
        tipoPrestamoRepository = mock(TipoPrestamoRepository.class);
        authenticationGateway = mock(AuthenticationGateway.class);
        capacidadUseCase = mock(CapacidadUseCase.class);
        useCase = new SolicitudUseCase(solicitudRepository, tipoPrestamoRepository, authenticationGateway, capacidadUseCase);
    }

    @Test
    void saveSolicitud_success_withoutAutoValidation() {
        var tipo = TipoPrestamo.builder()
                .idTipoPrestamo(10L)
                .montoMinimo(1000)
                .montoMaximo(100000)
                .tasaInteres(5)
                .validacionAutomatica(false)
                .build();

        var solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .idTipoPrestamo(10L)
                .monto(new BigDecimal("5000"))
                .plazo(12)
                .email("a@b.com")
                .documentoIdentidad("123")
                .build();

        when(tipoPrestamoRepository.findById(10L)).thenReturn(Mono.just(tipo));
        when(authenticationGateway.validateUser("a@b.com", "123"))
                .thenReturn(Mono.just(UserValidateInfo.builder().exists(true).baseSalary(new BigDecimal("10000")).build()));
        when(solicitudRepository.saveSolicitud(any(Solicitud.class)))
                .thenAnswer(inv -> Mono.just(((Solicitud) inv.getArgument(0))));

        StepVerifier.create(useCase.saveSolicitud(solicitud))
                .assertNext(saved -> {
                    assertThat(saved.getIdEstado()).isEqualTo(1L);
                    assertThat(saved.getIdTipoPrestamo()).isEqualTo(10L);
                })
                .verifyComplete();

        verify(capacidadUseCase, never()).validarCapacidad(any());
    }

    @Test
    void saveSolicitud_success_withAutoValidation() {
        var tipo = TipoPrestamo.builder()
                .idTipoPrestamo(10L)
                .montoMinimo(1000)
                .montoMaximo(100000)
                .tasaInteres(5)
                .validacionAutomatica(true)
                .build();

        var solicitud = Solicitud.builder()
                .idSolicitud(2L)
                .idTipoPrestamo(10L)
                .monto(new BigDecimal("7000"))
                .plazo(10)
                .email("x@y.com")
                .documentoIdentidad("987")
                .build();

        when(tipoPrestamoRepository.findById(10L)).thenReturn(Mono.just(tipo));
        when(authenticationGateway.validateUser("x@y.com", "987"))
                .thenReturn(Mono.just(UserValidateInfo.builder().exists(true).baseSalary(new BigDecimal("50000")).build()));
        when(solicitudRepository.saveSolicitud(any(Solicitud.class)))
                .thenAnswer(inv -> Mono.just(((Solicitud) inv.getArgument(0))));
        when(capacidadUseCase.validarCapacidad(any(Solicitud.class))).thenReturn(Mono.empty());

        StepVerifier.create(useCase.saveSolicitud(solicitud))
                .expectNextMatches(saved -> saved.getIdSolicitud().equals(2L))
                .verifyComplete();

        verify(capacidadUseCase, times(1)).validarCapacidad(any(Solicitud.class));
    }

    @Test
    void saveSolicitud_tipoPrestamoNotFound() {
        var solicitud = Solicitud.builder()
                .idSolicitud(3L)
                .idTipoPrestamo(99L)
                .monto(new BigDecimal("5000"))
                .plazo(12)
                .email("a@b.com")
                .documentoIdentidad("123")
                .build();

        when(tipoPrestamoRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.saveSolicitud(solicitud))
                .expectError(TipoPrestamoNotFoundException.class)
                .verify();
    }

    @Test
    void saveSolicitud_montoFueraDeRango() {
        var tipo = TipoPrestamo.builder()
                .idTipoPrestamo(10L)
                .montoMinimo(1000)
                .montoMaximo(2000)
                .tasaInteres(5)
                .validacionAutomatica(false)
                .build();

        var solicitud = Solicitud.builder()
                .idSolicitud(4L)
                .idTipoPrestamo(10L)
                .monto(new BigDecimal("50000"))
                .plazo(12)
                .email("a@b.com")
                .documentoIdentidad("123")
                .build();

        when(tipoPrestamoRepository.findById(10L)).thenReturn(Mono.just(tipo));

        StepVerifier.create(useCase.saveSolicitud(solicitud))
                .expectError(MontoFueraDeRangoException.class)
                .verify();
    }

    @Test
    void saveSolicitud_usuarioNoValido() {
        var tipo = TipoPrestamo.builder()
                .idTipoPrestamo(10L)
                .montoMinimo(1000)
                .montoMaximo(100000)
                .tasaInteres(5)
                .validacionAutomatica(false)
                .build();

        var solicitud = Solicitud.builder()
                .idSolicitud(5L)
                .idTipoPrestamo(10L)
                .monto(new BigDecimal("5000"))
                .plazo(12)
                .email("no@valido.com")
                .documentoIdentidad("000")
                .build();

        when(tipoPrestamoRepository.findById(10L)).thenReturn(Mono.just(tipo));
        when(authenticationGateway.validateUser("no@valido.com", "000"))
                .thenReturn(Mono.just(UserValidateInfo.builder().exists(false).baseSalary(new BigDecimal("0")).build()));

        StepVerifier.create(useCase.saveSolicitud(solicitud))
                .expectError(ValidationUserException.class)
                .verify();
    }

    @Test
    void findAllSolicitudes_enrichesUsers_andHandlesAuthErrorGracefully() {
        var req = new SolicitudPageRequest();

        var page = new SolicitudPage<SolicitudFieldsPage>(
                List.of(
                        SolicitudFieldsPage.builder().monto(new BigDecimal("1000")).plazo(6).email("u1@test.com").tipoPrestamo("TP1").estado("PENDIENTE").build(),
                        SolicitudFieldsPage.builder().monto(new BigDecimal("2000")).plazo(12).email("u2@test.com").tipoPrestamo("TP2").estado("APROBADO").build()
                ),
                2L, 10, 0, true, "monto,asc"
        );

        when(solicitudRepository.page(req)).thenReturn(Mono.just(page));
        when(authenticationGateway.getUsersForPage(anyList()))
                .thenReturn(Flux.error(new RuntimeException("downstream error")));

        StepVerifier.create(useCase.findAllSolicitudes(req))
                .assertNext(result -> {
                    assertThat(result.getData()).hasSize(2);
                    assertThat(result.getData().get(0).getEmail()).isEqualTo("u1@test.com");
                })
                .verifyComplete();
    }

    @Test
    void findAllSolicitudes_enrichesWithUserInfo_whenAuthSucceeds() {
        var req = new SolicitudPageRequest();

        var page = new SolicitudPage<SolicitudFieldsPage>(
                List.of(
                        SolicitudFieldsPage.builder().monto(new BigDecimal("3000")).plazo(8).email("u3@test.com").tipoPrestamo("TP3").estado("PENDIENTE").build()
                ),
                1L, 10, 0, false, "monto,asc"
        );

        when(solicitudRepository.page(req)).thenReturn(Mono.just(page));
        when(authenticationGateway.getUsersForPage(anyList()))
                .thenReturn(Flux.just(
                        co.com.crediya.pragma.solicitudes.model.page.UsersForPageResponse.builder()
                                .name("User 3").email("u3@test.com").baseSalary(9000L).build()
                ));

        StepVerifier.create(useCase.findAllSolicitudes(req))
                .assertNext(result -> {
                    assertThat(result.getData()).hasSize(1);
                    var item = result.getData().get(0);
                    assertThat(item.getEmail()).isEqualTo("u3@test.com");
                    assertThat(item.getNombre()).isEqualTo("User 3");
                    assertThat(item.getSalarioBase()).isEqualTo(9000L);
                })
                .verifyComplete();
    }
}

