package co.com.crediya.pragma.solicitudes.r2dbc;

import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.TipoPrestamoRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class TipoPrestamoRepositoryAdapter implements TipoPrestamoRepository {

    private final DatabaseClient client;

    public TipoPrestamoRepositoryAdapter(DatabaseClient client) {
        this.client = client;
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return client.sql("SELECT 1 AS ok FROM tipo_prestamos WHERE id_tipo_prestamo = ? LIMIT 1")
                .bind(0, id)
                .map((row, md) -> true)
                .one()
                .defaultIfEmpty(false);
    }

}

