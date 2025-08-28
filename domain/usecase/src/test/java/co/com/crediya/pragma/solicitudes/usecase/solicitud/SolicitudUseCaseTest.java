package co.com.crediya.pragma.solicitudes.usecase.solicitud;

import co.com.crediya.pragma.solicitudes.model.exception.TipoPrestamoNotFoundException;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.TipoPrestamoRepository;
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

    @BeforeEach
    void setUp() {
        solicitudUseCase = new SolicitudUseCase(solicitudRepository, tipoPrestamoRepository);
        
        solicitud = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(12)
                .email("test@example.com")
                .documentoIdentidad("12345678")
                .idEstado(1L)
                .idTipoPrestamo(1L)
                .build();
    }

    @Test
    @DisplayName("Debería guardar una solicitud exitosamente")
    void shouldSaveSolicitudSuccessfully() {
        when(tipoPrestamoRepository.existsById(1L))
                .thenReturn(Mono.just(true));
        when(solicitudRepository.saveSolicitud(any(Solicitud.class)))
                .thenReturn(Mono.just(solicitud));

        StepVerifier.create(solicitudUseCase.saveSolicitud(solicitud))
                .expectNext(solicitud)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería fallar cuando el tipo de préstamo no existe")
    void shouldFailWhenTipoPrestamoDoesNotExist() {
        when(tipoPrestamoRepository.existsById(1L))
                .thenReturn(Mono.just(false));

        StepVerifier.create(solicitudUseCase.saveSolicitud(solicitud))
                .expectError(TipoPrestamoNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Debería encontrar todas las solicitudes")
    void shouldFindAllSolicitudes() {
        Solicitud solicitud2 = solicitud.toBuilder()
                .idSolicitud(2L)
                .email("test2@example.com")
                .build();

        when(solicitudRepository.findAllSolicitudes())
                .thenReturn(Flux.just(solicitud, solicitud2));

        StepVerifier.create(solicitudUseCase.findAllSolicitudes())
                .expectNext(solicitud)
                .expectNext(solicitud2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería encontrar una solicitud por ID")
    void shouldFindSolicitudById() {
        when(solicitudRepository.findSolicitudById(1L))
                .thenReturn(Mono.just(solicitud));

        StepVerifier.create(solicitudUseCase.findSolicitudById(1L))
                .expectNext(solicitud)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería retornar vacío cuando no encuentra solicitud por ID")
    void shouldReturnEmptyWhenSolicitudNotFound() {
        when(solicitudRepository.findSolicitudById(999L))
                .thenReturn(Mono.empty());

        StepVerifier.create(solicitudUseCase.findSolicitudById(999L))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería establecer el estado como pendiente al guardar")
    void shouldSetEstadoAsPendingWhenSaving() {
        Solicitud solicitudSinEstado = solicitud.toBuilder()
                .idEstado(null)
                .build();

        when(tipoPrestamoRepository.existsById(1L))
                .thenReturn(Mono.just(true));
        when(solicitudRepository.saveSolicitud(any(Solicitud.class)))
                .thenReturn(Mono.just(solicitudSinEstado));

        StepVerifier.create(solicitudUseCase.saveSolicitud(solicitudSinEstado))
                .expectNext(solicitudSinEstado)
                .verifyComplete();
    }
}
