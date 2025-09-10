package co.com.crediya.pragma.solicitudes.r2dbc;

import co.com.crediya.pragma.solicitudes.model.estado.Estado;
import co.com.crediya.pragma.solicitudes.model.estado.gateways.EstadoRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class EstadoRepositoryAdapter implements EstadoRepository {

    private final DatabaseClient client;

    public EstadoRepositoryAdapter(DatabaseClient client) {
        this.client = client;
    }

    @Override
    public Mono<Estado> findById(Long id) {
        return client.sql("SELECT id_estado, nombre_estado, descripcion_estado FROM estados WHERE id_estado = ?")
                .bind(0, id)
                .map((row, md) -> Estado.builder()
                        .idEstado(row.get("id_estado", Long.class))
                        .nombreEstado(row.get("nombre_estado", String.class))
                        .descripcionEstado(row.get("descripcion_estado", String.class))
                        .build())
                .one();
    }
}
