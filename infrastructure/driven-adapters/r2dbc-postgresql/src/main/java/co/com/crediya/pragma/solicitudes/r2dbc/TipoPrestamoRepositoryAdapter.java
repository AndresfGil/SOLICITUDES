package co.com.crediya.pragma.solicitudes.r2dbc;

import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.TipoPrestamoRepository;
import co.com.crediya.pragma.solicitudes.model.tipoprestamo.TipoPrestamo;
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

    @Override
    public Mono<TipoPrestamo> findById(Long id) {
        return client.sql("SELECT id_tipo_prestamo, nombre, monto_minimo, monto_maximo, tasa_interes, validacion_automatica FROM tipo_prestamos WHERE id_tipo_prestamo = ?")
                .bind(0, id)
                .map((row, md) -> TipoPrestamo.builder()
                        .idTipoPrestamo(row.get("id_tipo_prestamo", Long.class))
                        .nombre(row.get("nombre", String.class))
                        .montoMinimo(row.get("monto_minimo", java.math.BigDecimal.class).intValue())
                        .montoMaximo(row.get("monto_maximo", java.math.BigDecimal.class).intValue())
                        .tasaInteres(row.get("tasa_interes", java.math.BigDecimal.class).intValue())
                        .validacionAutomatica(row.get("validacion_automatica", Boolean.class))
                        .build())
                .one();
    }
}

