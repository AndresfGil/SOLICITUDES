package co.com.crediya.pragma.solicitudes.usecase.solicitud;

import co.com.crediya.pragma.solicitudes.model.auth.UserValidateInfo;
import co.com.crediya.pragma.solicitudes.model.auth.gateways.AuthenticationGateway;
import co.com.crediya.pragma.solicitudes.model.capacidad.ValidacionCapacidad;
import co.com.crediya.pragma.solicitudes.model.exception.MontoFueraDeRangoException;
import co.com.crediya.pragma.solicitudes.model.exception.TipoPrestamoNotFoundException;
import co.com.crediya.pragma.solicitudes.model.exception.ValidationUserException;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudFieldsPage;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudPage;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudPageRequest;
import co.com.crediya.pragma.solicitudes.model.page.UsersForPageResponse;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.SolicitudConUsuario;
import co.com.crediya.pragma.solicitudes.model.solicitud.SolicitudConUsuarioResponse;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.TipoPrestamoRepository;
import co.com.crediya.pragma.solicitudes.model.tipoprestamo.TipoPrestamo;
import co.com.crediya.pragma.solicitudes.usecase.solicitud.capacidad.CapacidadUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitudUseCaseTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private TipoPrestamoRepository tipoPrestamoRepository;

    @Mock
    private AuthenticationGateway authenticationGateway;

    @Mock
    private CapacidadUseCase capacidadUseCase;

    private SolicitudUseCase solicitudUseCase;

    @BeforeEach
    void setUp() {
        solicitudUseCase = new SolicitudUseCase(
                solicitudRepository,
                tipoPrestamoRepository,
                authenticationGateway,
                capacidadUseCase
        );
    }

    @Test
    void saveSolicitud_ValidSolicitud_ReturnsSavedSolicitud() {
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idTipoPrestamo(1L)
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .montoMinimo(1000000)
                .montoMaximo(10000000)
                .validacionAutomatica(false)
                .build();

        UserValidateInfo userInfo = UserValidateInfo.builder()
                .exists(true)
                .baseSalary(new BigDecimal("3000000"))
                .build();

        Solicitud savedSolicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(1L)
                .idTipoPrestamo(1L)
                .build();

        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));
        when(authenticationGateway.validateUser("test@example.com", "12345678"))
                .thenReturn(Mono.just(userInfo));
        when(solicitudRepository.saveSolicitud(any(Solicitud.class)))
                .thenReturn(Mono.just(savedSolicitud));

        StepVerifier.create(solicitudUseCase.saveSolicitud(solicitud))
                .expectNext(savedSolicitud)
                .verifyComplete();
    }

    @Test
    void saveSolicitud_TipoPrestamoNotFound_ThrowsException() {
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idTipoPrestamo(999L)
                .build();

        when(tipoPrestamoRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(solicitudUseCase.saveSolicitud(solicitud))
                .expectError(TipoPrestamoNotFoundException.class)
                .verify();
    }

    @Test
    void saveSolicitud_MontoFueraDeRango_ThrowsException() {
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("500000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idTipoPrestamo(1L)
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .montoMinimo(1000000)
                .montoMaximo(10000000)
                .validacionAutomatica(false)
                .build();

        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));

        StepVerifier.create(solicitudUseCase.saveSolicitud(solicitud))
                .expectError(MontoFueraDeRangoException.class)
                .verify();
    }

    @Test
    void saveSolicitud_UserNotExists_ThrowsException() {
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idTipoPrestamo(1L)
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .montoMinimo(1000000)
                .montoMaximo(10000000)
                .validacionAutomatica(false)
                .build();

        UserValidateInfo userInfo = UserValidateInfo.builder()
                .exists(false)
                .baseSalary(new BigDecimal("3000000"))
                .build();

        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));
        when(authenticationGateway.validateUser("test@example.com", "12345678"))
                .thenReturn(Mono.just(userInfo));

        StepVerifier.create(solicitudUseCase.saveSolicitud(solicitud))
                .expectError(ValidationUserException.class)
                .verify();
    }

    @Test
    void saveSolicitud_ValidacionAutomaticaTrue_CallsCapacidadUseCase() {
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idTipoPrestamo(1L)
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .montoMinimo(1000000)
                .montoMaximo(10000000)
                .validacionAutomatica(true)
                .build();

        UserValidateInfo userInfo = UserValidateInfo.builder()
                .exists(true)
                .baseSalary(new BigDecimal("3000000"))
                .build();

        Solicitud savedSolicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(1L)
                .idTipoPrestamo(1L)
                .build();

        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));
        when(authenticationGateway.validateUser("test@example.com", "12345678"))
                .thenReturn(Mono.just(userInfo));
        when(solicitudRepository.saveSolicitud(any(Solicitud.class)))
                .thenReturn(Mono.just(savedSolicitud));
        when(capacidadUseCase.validarCapacidad(savedSolicitud))
                .thenReturn(Mono.just(ValidacionCapacidad.builder().build()));

        StepVerifier.create(solicitudUseCase.saveSolicitud(solicitud))
                .expectNext(savedSolicitud)
                .verifyComplete();
    }



    @Test
    void findAllSolicitudes_EmptyEmails_ReturnsPagedSolicitudes() {
        SolicitudPageRequest request = new SolicitudPageRequest();
        request.setPage(0);
        request.setSize(10);

        SolicitudFieldsPage solicitudFields = new SolicitudFieldsPage(
                new BigDecimal("5000000"),
                24,
                null,
                "Personal",
                "Pendiente"
        );

        SolicitudPage<SolicitudFieldsPage> pagedSolicitudes = new SolicitudPage<>(
                List.of(solicitudFields),
                1L,
                10,
                0,
                false,
                "ASC"
        );

        when(solicitudRepository.page(request)).thenReturn(Mono.just(pagedSolicitudes));
        when(authenticationGateway.getUsersForPage(List.of()))
                .thenReturn(Flux.empty());

        StepVerifier.create(solicitudUseCase.findAllSolicitudes(request))
                .assertNext(result -> {
                    assert result.getData().size() == 1;
                    assert result.getData().get(0).getEmail() == null;
                    assert result.getData().get(0).getNombre() == null;
                })
                .verifyComplete();
    }

  
    @Test
    void findAllSolicitudes_MultipleEmails_ReturnsPagedSolicitudes() {
        SolicitudPageRequest request = new SolicitudPageRequest();
        request.setPage(0);
        request.setSize(10);

        SolicitudFieldsPage solicitud1 = new SolicitudFieldsPage(
                new BigDecimal("5000000"),
                24,
                "test1@example.com",
                "Personal",
                "Pendiente"
        );

        SolicitudFieldsPage solicitud2 = new SolicitudFieldsPage(
                new BigDecimal("3000000"),
                12,
                "test2@example.com",
                "Hipotecario",
                "Aprobado"
        );

        SolicitudPage<SolicitudFieldsPage> pagedSolicitudes = new SolicitudPage<>(
                List.of(solicitud1, solicitud2),
                2L,
                10,
                0,
                false,
                "ASC"
        );

        UsersForPageResponse user1 = new UsersForPageResponse(
                "Juan Pérez",
                "test1@example.com",
                3000000L
        );

        UsersForPageResponse user2 = new UsersForPageResponse(
                "María García",
                "test2@example.com",
                4000000L
        );

        when(solicitudRepository.page(request)).thenReturn(Mono.just(pagedSolicitudes));
        when(authenticationGateway.getUsersForPage(List.of("test1@example.com", "test2@example.com")))
                .thenReturn(Mono.just(user1).concatWith(Mono.just(user2)));

        StepVerifier.create(solicitudUseCase.findAllSolicitudes(request))
                .assertNext(result -> {
                    assert result.getData().size() == 2;
                    assert result.getData().get(0).getNombre().equals("Juan Pérez");
                    assert result.getData().get(1).getNombre().equals("María García");
                })
                .verifyComplete();
    }
}
