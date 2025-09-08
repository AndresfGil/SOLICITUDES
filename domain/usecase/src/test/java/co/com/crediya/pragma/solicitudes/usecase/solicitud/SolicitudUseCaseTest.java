package co.com.crediya.pragma.solicitudes.usecase.solicitud;

import co.com.crediya.pragma.solicitudes.model.auth.gateways.AuthenticationGateway;
import co.com.crediya.pragma.solicitudes.model.exception.MontoFueraDeRangoException;
import co.com.crediya.pragma.solicitudes.model.exception.TipoPrestamoNotFoundException;
import co.com.crediya.pragma.solicitudes.model.exception.ValidationUserException;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudFieldsPage;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudPage;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudPageRequest;
import co.com.crediya.pragma.solicitudes.model.page.UsersForPageResponse;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.SolicitudConUsuarioResponse;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.TipoPrestamoRepository;
import co.com.crediya.pragma.solicitudes.model.tipoprestamo.TipoPrestamo;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitudUseCaseTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private TipoPrestamoRepository tipoPrestamoRepository;

    @Mock
    private AuthenticationGateway authenticationGateway;

    private SolicitudUseCase solicitudUseCase;

    @BeforeEach
    void setUp() {
        solicitudUseCase = new SolicitudUseCase(solicitudRepository, tipoPrestamoRepository, authenticationGateway);
    }

    @Test
    void saveSolicitud_WhenValidData_ShouldReturnSavedSolicitud() {
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idTipoPrestamo(1L)
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .nombre("Personal")
                .montoMinimo(1000000)
                .montoMaximo(10000000)
                .tasaInteres(15)
                .validacionAutomatica(true)
                .build();

        Solicitud solicitudSaved = solicitud.toBuilder()
                .idSolicitud(1L)
                .idEstado(1L)
                .build();

        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));
        when(authenticationGateway.validateUser("test@example.com", "12345678")).thenReturn(Mono.just(true));
        when(solicitudRepository.saveSolicitud(any(Solicitud.class))).thenReturn(Mono.just(solicitudSaved));

        StepVerifier.create(solicitudUseCase.saveSolicitud(solicitud))
                .expectNext(solicitudSaved)
                .verifyComplete();
    }

    @Test
    void saveSolicitud_WhenTipoPrestamoNotFound_ShouldThrowTipoPrestamoNotFoundException() {
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
    void saveSolicitud_WhenMontoBelowMinimum_ShouldThrowMontoFueraDeRangoException() {
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("500000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idTipoPrestamo(1L)
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .nombre("Personal")
                .montoMinimo(1000000)
                .montoMaximo(10000000)
                .tasaInteres(15)
                .validacionAutomatica(true)
                .build();

        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));

        StepVerifier.create(solicitudUseCase.saveSolicitud(solicitud))
                .expectError(MontoFueraDeRangoException.class)
                .verify();
    }

    @Test
    void saveSolicitud_WhenMontoAboveMaximum_ShouldThrowMontoFueraDeRangoException() {
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("15000000"))
                .plazo(24)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idTipoPrestamo(1L)
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .nombre("Personal")
                .montoMinimo(1000000)
                .montoMaximo(10000000)
                .tasaInteres(15)
                .validacionAutomatica(true)
                .build();

        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));

        StepVerifier.create(solicitudUseCase.saveSolicitud(solicitud))
                .expectError(MontoFueraDeRangoException.class)
                .verify();
    }

    @Test
    void saveSolicitud_WhenUserInvalid_ShouldThrowValidationUserException() {
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .email("invalid@example.com")
                .documentoIdentidad("12345678")
                .idTipoPrestamo(1L)
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .nombre("Personal")
                .montoMinimo(1000000)
                .montoMaximo(10000000)
                .tasaInteres(15)
                .validacionAutomatica(true)
                .build();

        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));
        when(authenticationGateway.validateUser("invalid@example.com", "12345678")).thenReturn(Mono.just(false));

        StepVerifier.create(solicitudUseCase.saveSolicitud(solicitud))
                .expectError(ValidationUserException.class)
                .verify();
    }

    @Test
    void findAllSolicitudes_WhenValidRequest_ShouldReturnEnrichedPage() {
        SolicitudPageRequest pageRequest = new SolicitudPageRequest();
        pageRequest.setPage(0);
        pageRequest.setSize(10);

        SolicitudFieldsPage solicitudFields = new SolicitudFieldsPage(
                new BigDecimal("5000000"),
                24,
                "test@example.com",
                "Personal",
                "Pendiente"
        );

        SolicitudPage<SolicitudFieldsPage> pagedData = new SolicitudPage<>(
                List.of(solicitudFields),
                1L,
                10,
                0,
                false,
                "ASC"
        );

        UsersForPageResponse userInfo = new UsersForPageResponse(
                "Juan Pérez",
                "test@example.com",
                3000000L
        );

        when(solicitudRepository.page(pageRequest)).thenReturn(Mono.just(pagedData));
        when(authenticationGateway.getUsersForPage(List.of("test@example.com")))
                .thenReturn(Flux.just(userInfo));

        StepVerifier.create(solicitudUseCase.findAllSolicitudes(pageRequest))
                .assertNext(result -> {
                    assert result.getData().size() == 1;
                    assert result.getTotalRows() == 1L;
                    assert result.getPageSize() == 10;
                    assert result.getPageNum() == 0;
                    assert !result.getHasNext();
                    assert result.getSort().equals("ASC");
                    
                    SolicitudConUsuarioResponse response = result.getData().get(0);
                    assert response.getMonto().equals(new BigDecimal("5000000"));
                    assert response.getPlazo() == 24;
                    assert response.getEmail().equals("test@example.com");
                    assert response.getNombreTipoPrestamo().equals("Personal");
                    assert response.getEstadoSolicitud().equals("Pendiente");
                    assert response.getNombre().equals("Juan Pérez");
                    assert response.getSalarioBase() == 3000000L;
                })
                .verifyComplete();
    }

    @Test
    void findAllSolicitudes_WhenNoUsersFound_ShouldReturnPageWithNullUserInfo() {
        SolicitudPageRequest pageRequest = new SolicitudPageRequest();
        pageRequest.setPage(0);
        pageRequest.setSize(10);

        SolicitudFieldsPage solicitudFields = new SolicitudFieldsPage(
                new BigDecimal("5000000"),
                24,
                "test@example.com",
                "Personal",
                "Pendiente"
        );

        SolicitudPage<SolicitudFieldsPage> pagedData = new SolicitudPage<>(
                List.of(solicitudFields),
                1L,
                10,
                0,
                false,
                "ASC"
        );

        when(solicitudRepository.page(pageRequest)).thenReturn(Mono.just(pagedData));
        when(authenticationGateway.getUsersForPage(List.of("test@example.com")))
                .thenReturn(Flux.empty());

        StepVerifier.create(solicitudUseCase.findAllSolicitudes(pageRequest))
                .assertNext(result -> {
                    assert result.getData().size() == 1;
                    SolicitudConUsuarioResponse response = result.getData().get(0);
                    assert response.getMonto().equals(new BigDecimal("5000000"));
                    assert response.getNombre() == null;
                    assert response.getSalarioBase() == null;
                })
                .verifyComplete();
    }

    @Test
    void findAllSolicitudes_WhenAuthenticationGatewayFails_ShouldReturnPageWithEmptyUserInfo() {
        SolicitudPageRequest pageRequest = new SolicitudPageRequest();
        pageRequest.setPage(0);
        pageRequest.setSize(10);

        SolicitudFieldsPage solicitudFields = new SolicitudFieldsPage(
                new BigDecimal("5000000"),
                24,
                "test@example.com",
                "Personal",
                "Pendiente"
        );

        SolicitudPage<SolicitudFieldsPage> pagedData = new SolicitudPage<>(
                List.of(solicitudFields),
                1L,
                10,
                0,
                false,
                "ASC"
        );

        when(solicitudRepository.page(pageRequest)).thenReturn(Mono.just(pagedData));
        when(authenticationGateway.getUsersForPage(List.of("test@example.com")))
                .thenReturn(Flux.error(new RuntimeException("Authentication service error")));

        StepVerifier.create(solicitudUseCase.findAllSolicitudes(pageRequest))
                .assertNext(result -> {
                    assert result.getData().size() == 1;
                    SolicitudConUsuarioResponse response = result.getData().get(0);
                    assert response.getMonto().equals(new BigDecimal("5000000"));
                    assert response.getNombre() == null;
                    assert response.getSalarioBase() == null;
                })
                .verifyComplete();
    }

    @Test
    void findAllSolicitudes_WhenMultipleEmails_ShouldFilterUniqueEmails() {
        SolicitudPageRequest pageRequest = new SolicitudPageRequest();
        pageRequest.setPage(0);
        pageRequest.setSize(10);

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
                "Personal",
                "Pendiente"
        );

        SolicitudFieldsPage solicitud3 = new SolicitudFieldsPage(
                new BigDecimal("7000000"),
                36,
                "test1@example.com",
                "Personal",
                "Pendiente"
        );

        SolicitudPage<SolicitudFieldsPage> pagedData = new SolicitudPage<>(
                List.of(solicitud1, solicitud2, solicitud3),
                3L,
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

        when(solicitudRepository.page(pageRequest)).thenReturn(Mono.just(pagedData));
        when(authenticationGateway.getUsersForPage(List.of("test1@example.com", "test2@example.com")))
                .thenReturn(Flux.just(user1, user2));

        StepVerifier.create(solicitudUseCase.findAllSolicitudes(pageRequest))
                .assertNext(result -> {
                    assert result.getData().size() == 3;
                    assert result.getTotalRows() == 3L;
                    
                    SolicitudConUsuarioResponse response1 = result.getData().get(0);
                    assert response1.getEmail().equals("test1@example.com");
                    assert response1.getNombre().equals("Juan Pérez");
                    
                    SolicitudConUsuarioResponse response2 = result.getData().get(1);
                    assert response2.getEmail().equals("test2@example.com");
                    assert response2.getNombre().equals("María García");
                    
                    SolicitudConUsuarioResponse response3 = result.getData().get(2);
                    assert response3.getEmail().equals("test1@example.com");
                    assert response3.getNombre().equals("Juan Pérez");
                })
                .verifyComplete();
    }
}
