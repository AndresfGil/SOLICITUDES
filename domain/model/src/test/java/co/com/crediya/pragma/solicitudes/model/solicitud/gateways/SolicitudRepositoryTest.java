package co.com.crediya.pragma.solicitudes.model.solicitud.gateways;

import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para la interfaz SolicitudRepository")
class SolicitudRepositoryTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    private Solicitud solicitud1;
    private Solicitud solicitud2;
    private Solicitud solicitud3;

    @BeforeEach
    void setUp() {
        solicitud1 = Solicitud.builder()
                .idSolicitud(1L)
                .monto(new BigDecimal("5000000"))
                .plazo(12)
                .email("test1@example.com")
                .documentoIdentidad("12345678")
                .idEstado(1L)
                .idTipoPrestamo(1L)
                .build();

        solicitud2 = Solicitud.builder()
                .idSolicitud(2L)
                .monto(new BigDecimal("3000000"))
                .plazo(6)
                .email("test2@example.com")
                .documentoIdentidad("87654321")
                .idEstado(2L)
                .idTipoPrestamo(2L)
                .build();

        solicitud3 = Solicitud.builder()
                .idSolicitud(3L)
                .monto(new BigDecimal("7000000"))
                .plazo(24)
                .email("test3@example.com")
                .documentoIdentidad("11223344")
                .idEstado(1L)
                .idTipoPrestamo(1L)
                .build();
    }

    @Test
    @DisplayName("Debería guardar una solicitud exitosamente")
    void shouldSaveSolicitudSuccessfully() {

        Solicitud solicitudToSave = solicitud1.toBuilder().idSolicitud(null).build();
        Solicitud savedSolicitud = solicitud1;

        when(solicitudRepository.saveSolicitud(any(Solicitud.class)))
                .thenReturn(Mono.just(savedSolicitud));


        StepVerifier.create(solicitudRepository.saveSolicitud(solicitudToSave))
                .expectNext(savedSolicitud)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería encontrar todas las solicitudes")
    void shouldFindAllSolicitudes() {

        List<Solicitud> solicitudes = List.of(solicitud1, solicitud2, solicitud3);

        when(solicitudRepository.findAllSolicitudes())
                .thenReturn(Flux.fromIterable(solicitudes));


        StepVerifier.create(solicitudRepository.findAllSolicitudes())
                .expectNext(solicitud1)
                .expectNext(solicitud2)
                .expectNext(solicitud3)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería encontrar todas las solicitudes cuando la lista está vacía")
    void shouldFindAllSolicitudesWhenEmpty() {

        when(solicitudRepository.findAllSolicitudes())
                .thenReturn(Flux.empty());


        StepVerifier.create(solicitudRepository.findAllSolicitudes())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería encontrar una solicitud por ID exitosamente")
    void shouldFindSolicitudByIdSuccessfully() {

        Long solicitudId = 1L;

        when(solicitudRepository.findSolicitudById(solicitudId))
                .thenReturn(Mono.just(solicitud1));


        StepVerifier.create(solicitudRepository.findSolicitudById(solicitudId))
                .expectNext(solicitud1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería retornar Mono vacío cuando no encuentra la solicitud por ID")
    void shouldReturnEmptyMonoWhenSolicitudNotFound() {

        Long nonExistentId = 999L;

        when(solicitudRepository.findSolicitudById(nonExistentId))
                .thenReturn(Mono.empty());


        StepVerifier.create(solicitudRepository.findSolicitudById(nonExistentId))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería manejar errores al guardar solicitud")
    void shouldHandleErrorWhenSavingSolicitud() {

        Solicitud solicitudToSave = solicitud1;
        RuntimeException error = new RuntimeException("Error de base de datos");

        when(solicitudRepository.saveSolicitud(any(Solicitud.class)))
                .thenReturn(Mono.error(error));


        StepVerifier.create(solicitudRepository.saveSolicitud(solicitudToSave))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    @DisplayName("Debería manejar errores al buscar todas las solicitudes")
    void shouldHandleErrorWhenFindingAllSolicitudes() {

        RuntimeException error = new RuntimeException("Error de conexión");

        when(solicitudRepository.findAllSolicitudes())
                .thenReturn(Flux.error(error));


        StepVerifier.create(solicitudRepository.findAllSolicitudes())
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    @DisplayName("Debería manejar errores al buscar solicitud por ID")
    void shouldHandleErrorWhenFindingSolicitudById() {

        Long solicitudId = 1L;
        RuntimeException error = new RuntimeException("Error de consulta");

        when(solicitudRepository.findSolicitudById(solicitudId))
                .thenReturn(Mono.error(error));


        StepVerifier.create(solicitudRepository.findSolicitudById(solicitudId))
                .expectError(RuntimeException.class)
                .verify();
    }
}
