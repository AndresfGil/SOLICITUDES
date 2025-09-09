package co.com.crediya.pragma.solicitudes.r2dbc;

import co.com.crediya.pragma.solicitudes.model.notificaicones.CambioEstadoRepository;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class CambioEstadoRepositoryAdapter implements CambioEstadoRepository {

    private final DatabaseClient client;
    private final TransactionalOperator transactionalOperator;

    public CambioEstadoRepositoryAdapter(DatabaseClient client, TransactionalOperator transactionalOperator) {
        this.client = client;
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<Solicitud> updateEstado(Solicitud solicitud) {
        return client.sql("UPDATE solicitudes SET id_estado = ? WHERE id_solicitud = ?")
                .bind(0, solicitud.getIdEstado())
                .bind(1, solicitud.getIdSolicitud())
                .fetch()
                .rowsUpdated()
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        log.info("Estado actualizado exitosamente para solicitud ID: {}", solicitud.getIdSolicitud());
                        return Mono.just(solicitud);
                    } else {
                        log.warn("No se encontró solicitud con ID: {} para actualizar estado", solicitud.getIdSolicitud());
                        return Mono.just(solicitud);
                    }
                })
                .doOnError(e -> log.error("Error al actualizar estado de solicitud ID {}: {}", solicitud.getIdSolicitud(), e.getMessage()))
                .as(transactionalOperator::transactional);
    }
}
