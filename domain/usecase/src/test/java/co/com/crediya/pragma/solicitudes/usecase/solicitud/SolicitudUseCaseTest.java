package co.com.crediya.pragma.solicitudes.usecase.solicitud;

import co.com.crediya.pragma.solicitudes.model.exception.MontoFueraDeRangoException;
import co.com.crediya.pragma.solicitudes.model.exception.TipoPrestamoNotFoundException;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.TipoPrestamoRepository;
import co.com.crediya.pragma.solicitudes.model.tipoprestamo.TipoPrestamo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para SolicitudUseCase")
class SolicitudUseCaseTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private TipoPrestamoRepository tipoPrestamoRepository;

    private SolicitudUseCase solicitudUseCase;

    private Solicitud solicitud;
    private TipoPrestamo tipoPrestamo;

    @BeforeEach
    void setUp() {
        solicitudUseCase = new SolicitudUseCase(solicitudRepository, tipoPrestamoRepository);

        tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .nombre("Préstamo de Consumo")
                .montoMinimo(500000)
                .montoMaximo(5000000)
                .tasaInteres(15)
                .validacionAutomatica(false)
                .build();

        solicitud = Solicitud.builder()
                .monto(new BigDecimal("2000000"))
                .plazo(12)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idTipoPrestamo(1L)
                .build();
    }

    @Test
    @DisplayName("Debería guardar una solicitud exitosamente cuando el monto está en rango")
    void shouldSaveSolicitudSuccessfullyWhenAmountIsInRange() {
        // Given
        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));
        when(solicitudRepository.saveSolicitud(any(Solicitud.class))).thenReturn(Mono.just(solicitud));

        // When & Then
        StepVerifier.create(solicitudUseCase.saveSolicitud(solicitud))
                .expectNext(solicitud)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el tipo de préstamo no existe")
    void shouldThrowExceptionWhenTipoPrestamoDoesNotExist() {
        // Given
        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(solicitudUseCase.saveSolicitud(solicitud))
                .expectError(TipoPrestamoNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el monto está por debajo del mínimo")
    void shouldThrowExceptionWhenAmountIsBelowMinimum() {
        // Given
        Solicitud solicitudMontoBajo = solicitud.toBuilder()
                .monto(new BigDecimal("300000")) // Por debajo del mínimo de 500000
                .build();

        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));

        // When & Then
        StepVerifier.create(solicitudUseCase.saveSolicitud(solicitudMontoBajo))
                .expectError(MontoFueraDeRangoException.class)
                .verify();
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el monto está por encima del máximo")
    void shouldThrowExceptionWhenAmountIsAboveMaximum() {
        // Given
        Solicitud solicitudMontoAlto = solicitud.toBuilder()
                .monto(new BigDecimal("6000000")) // Por encima del máximo de 5000000
                .build();

        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));

        // When & Then
        StepVerifier.create(solicitudUseCase.saveSolicitud(solicitudMontoAlto))
                .expectError(MontoFueraDeRangoException.class)
                .verify();
    }

    @Test
    @DisplayName("Debería permitir monto exactamente en el límite mínimo")
    void shouldAllowAmountExactlyAtMinimumLimit() {
        // Given
        Solicitud solicitudMontoMinimo = solicitud.toBuilder()
                .monto(new BigDecimal("500000")) // Exactamente el mínimo
                .build();

        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));
        when(solicitudRepository.saveSolicitud(any(Solicitud.class))).thenReturn(Mono.just(solicitudMontoMinimo));

        // When & Then
        StepVerifier.create(solicitudUseCase.saveSolicitud(solicitudMontoMinimo))
                .expectNext(solicitudMontoMinimo)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería permitir monto exactamente en el límite máximo")
    void shouldAllowAmountExactlyAtMaximumLimit() {
        // Given
        Solicitud solicitudMontoMaximo = solicitud.toBuilder()
                .monto(new BigDecimal("5000000")) // Exactamente el máximo
                .build();

        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));
        when(solicitudRepository.saveSolicitud(any(Solicitud.class))).thenReturn(Mono.just(solicitudMontoMaximo));

        // When & Then
        StepVerifier.create(solicitudUseCase.saveSolicitud(solicitudMontoMaximo))
                .expectNext(solicitudMontoMaximo)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería establecer el estado en 1 al guardar")
    void shouldSetEstadoTo1WhenSaving() {
        // Given
        when(tipoPrestamoRepository.findById(1L)).thenReturn(Mono.just(tipoPrestamo));
        when(solicitudRepository.saveSolicitud(any(Solicitud.class))).thenReturn(Mono.just(solicitud));

        // When & Then
        StepVerifier.create(solicitudUseCase.saveSolicitud(solicitud))
                .expectNextMatches(savedSolicitud -> savedSolicitud.getIdEstado().equals(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería encontrar todas las solicitudes")
    void shouldFindAllSolicitudes() {
        // Given
        when(solicitudRepository.findAllSolicitudes()).thenReturn(Flux.just(solicitud));

        // When & Then
        StepVerifier.create(solicitudUseCase.findAllSolicitudes())
                .expectNext(solicitud)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería encontrar una solicitud por ID")
    void shouldFindSolicitudById() {
        // Given
        Long id = 1L;
        when(solicitudRepository.findSolicitudById(id)).thenReturn(Mono.just(solicitud));

        // When & Then
        StepVerifier.create(solicitudUseCase.findSolicitudById(id))
                .expectNext(solicitud)
                .verifyComplete();
    }
}
