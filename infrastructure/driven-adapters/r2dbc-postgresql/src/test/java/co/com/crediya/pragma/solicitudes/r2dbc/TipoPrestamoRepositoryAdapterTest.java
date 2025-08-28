package co.com.crediya.pragma.solicitudes.r2dbc;

import co.com.crediya.pragma.solicitudes.model.tipoprestamo.TipoPrestamo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para TipoPrestamoRepositoryAdapter")
class TipoPrestamoRepositoryAdapterTest {

    @Mock
    private DatabaseClient client;

    @Mock
    private DatabaseClient.GenericExecuteSpec executeSpec;

    @Mock
    private RowsFetchSpec<Boolean> booleanRowsFetchSpec;

    @Mock
    private RowsFetchSpec<TipoPrestamo> tipoPrestamoRowsFetchSpec;

    private TipoPrestamoRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new TipoPrestamoRepositoryAdapter(client);
    }

    @Test
    @DisplayName("Debería retornar true cuando existe un tipo de préstamo")
    void shouldReturnTrueWhenTipoPrestamoExists() {
        // Given
        Long id = 1L;
        when(client.sql(anyString())).thenReturn(executeSpec);
        when(executeSpec.bind(anyInt(), any())).thenReturn(executeSpec);
        when(executeSpec.map(any())).thenReturn(booleanRowsFetchSpec);
        when(booleanRowsFetchSpec.one()).thenReturn(Mono.just(true));

        // When & Then
        StepVerifier.create(adapter.existsById(id))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería retornar false cuando no existe un tipo de préstamo")
    void shouldReturnFalseWhenTipoPrestamoDoesNotExist() {
        // Given
        Long id = 999L;
        when(client.sql(anyString())).thenReturn(executeSpec);
        when(executeSpec.bind(anyInt(), any())).thenReturn(executeSpec);
        when(executeSpec.map(any())).thenReturn(booleanRowsFetchSpec);
        when(booleanRowsFetchSpec.one()).thenReturn(Mono.empty());
        when(booleanRowsFetchSpec.defaultIfEmpty(any())).thenReturn(Mono.just(false));

        // When & Then
        StepVerifier.create(adapter.existsById(id))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería encontrar un tipo de préstamo por ID")
    void shouldFindTipoPrestamoById() {
        // Given
        Long id = 1L;
        TipoPrestamo expectedTipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .nombre("Préstamo de Consumo")
                .montoMinimo(500000)
                .montoMaximo(5000000)
                .tasaInteres(15)
                .validacionAutomatica(false)
                .build();

        when(client.sql(anyString())).thenReturn(executeSpec);
        when(executeSpec.bind(anyInt(), any())).thenReturn(executeSpec);
        when(executeSpec.map(any())).thenReturn(tipoPrestamoRowsFetchSpec);
        when(tipoPrestamoRowsFetchSpec.one()).thenReturn(Mono.just(expectedTipoPrestamo));

        // When & Then
        StepVerifier.create(adapter.findById(id))
                .expectNext(expectedTipoPrestamo)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería retornar vacío cuando no encuentra el tipo de préstamo por ID")
    void shouldReturnEmptyWhenTipoPrestamoNotFoundById() {
        // Given
        Long id = 999L;
        when(client.sql(anyString())).thenReturn(executeSpec);
        when(executeSpec.bind(anyInt(), any())).thenReturn(executeSpec);
        when(executeSpec.map(any())).thenReturn(tipoPrestamoRowsFetchSpec);
        when(tipoPrestamoRowsFetchSpec.one()).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(adapter.findById(id))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería manejar ID cero en existsById")
    void shouldHandleZeroIdInExistsById() {
        // Given
        Long id = 0L;
        when(client.sql(anyString())).thenReturn(executeSpec);
        when(executeSpec.bind(anyInt(), any())).thenReturn(executeSpec);
        when(executeSpec.map(any())).thenReturn(booleanRowsFetchSpec);
        when(booleanRowsFetchSpec.one()).thenReturn(Mono.empty());
        when(booleanRowsFetchSpec.defaultIfEmpty(any())).thenReturn(Mono.just(false));

        // When & Then
        StepVerifier.create(adapter.existsById(id))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería manejar ID cero en findById")
    void shouldHandleZeroIdInFindById() {
        // Given
        Long id = 0L;
        when(client.sql(anyString())).thenReturn(executeSpec);
        when(executeSpec.bind(anyInt(), any())).thenReturn(executeSpec);
        when(executeSpec.map(any())).thenReturn(tipoPrestamoRowsFetchSpec);
        when(tipoPrestamoRowsFetchSpec.one()).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(adapter.findById(id))
                .verifyComplete();
    }
}
