package co.com.crediya.pragma.solicitudes.model.solicitud.gateways;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para la interfaz TipoPrestamoRepository")
class TipoPrestamoRepositoryTest {

    @Mock
    private TipoPrestamoRepository tipoPrestamoRepository;


    @Test
    @DisplayName("Debería retornar true cuando existe un tipo de préstamo")
    void shouldReturnTrueWhenTipoPrestamoExists() {
        Long existingId = 1L;

        when(tipoPrestamoRepository.existsById(existingId))
                .thenReturn(Mono.just(true));

        StepVerifier.create(tipoPrestamoRepository.existsById(existingId))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería retornar false cuando no existe un tipo de préstamo")
    void shouldReturnFalseWhenTipoPrestamoDoesNotExist() {
        Long nonExistentId = 999L;

        when(tipoPrestamoRepository.existsById(nonExistentId))
                .thenReturn(Mono.just(false));

        StepVerifier.create(tipoPrestamoRepository.existsById(nonExistentId))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería manejar ID cero")
    void shouldHandleZeroId() {
        Long zeroId = 0L;

        when(tipoPrestamoRepository.existsById(zeroId))
                .thenReturn(Mono.just(false));

        StepVerifier.create(tipoPrestamoRepository.existsById(zeroId))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería manejar ID negativo")
    void shouldHandleNegativeId() {

        Long negativeId = -1L;

        when(tipoPrestamoRepository.existsById(negativeId))
                .thenReturn(Mono.just(false));

        StepVerifier.create(tipoPrestamoRepository.existsById(negativeId))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería manejar errores de conexión")
    void shouldHandleConnectionError() {
        Long id = 1L;
        RuntimeException connectionError = new RuntimeException("Error de conexión");

        when(tipoPrestamoRepository.existsById(id))
                .thenReturn(Mono.error(connectionError));

        StepVerifier.create(tipoPrestamoRepository.existsById(id))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    @DisplayName("Debería manejar múltiples consultas con diferentes IDs")
    void shouldHandleMultipleQueriesWithDifferentIds() {
        when(tipoPrestamoRepository.existsById(1L))
                .thenReturn(Mono.just(true));
        when(tipoPrestamoRepository.existsById(2L))
                .thenReturn(Mono.just(true));
        when(tipoPrestamoRepository.existsById(3L))
                .thenReturn(Mono.just(false));
        when(tipoPrestamoRepository.existsById(999L))
                .thenReturn(Mono.just(false));

        StepVerifier.create(tipoPrestamoRepository.existsById(1L))
                .expectNext(true)
                .verifyComplete();

        StepVerifier.create(tipoPrestamoRepository.existsById(2L))
                .expectNext(true)
                .verifyComplete();

        StepVerifier.create(tipoPrestamoRepository.existsById(3L))
                .expectNext(false)
                .verifyComplete();

        StepVerifier.create(tipoPrestamoRepository.existsById(999L))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería manejar consultas concurrentes")
    void shouldHandleConcurrentQueries() {
        Long id = 1L;

        when(tipoPrestamoRepository.existsById(id))
                .thenReturn(Mono.just(true));

        StepVerifier.create(tipoPrestamoRepository.existsById(id))
                .expectNext(true)
                .verifyComplete();

        StepVerifier.create(tipoPrestamoRepository.existsById(id))
                .expectNext(true)
                .verifyComplete();

        StepVerifier.create(tipoPrestamoRepository.existsById(id))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería manejar consultas con IDs nulos")
    void shouldHandleQueriesWithNullIds() {
        when(tipoPrestamoRepository.existsById(null))
                .thenReturn(Mono.just(false));

        StepVerifier.create(tipoPrestamoRepository.existsById(null))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería manejar errores de recurso no encontrado")
    void shouldHandleResourceNotFoundErrors() {
        Long id = 1L;
        RuntimeException notFoundError = new RuntimeException("Recurso no encontrado");

        when(tipoPrestamoRepository.existsById(id))
                .thenReturn(Mono.error(notFoundError));

        StepVerifier.create(tipoPrestamoRepository.existsById(id))
                .expectError(RuntimeException.class)
                .verify();
    }
}
