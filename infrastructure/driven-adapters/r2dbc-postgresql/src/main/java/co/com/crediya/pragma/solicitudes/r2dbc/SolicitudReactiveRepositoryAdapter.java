package co.com.crediya.pragma.solicitudes.r2dbc;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudFieldsPage;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudPage;
import co.com.crediya.pragma.solicitudes.model.page.SolicitudPageRequest;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.r2dbc.dto.SolicitudFieldsDto;
import co.com.crediya.pragma.solicitudes.r2dbc.entities.SolicitudEntity;
import co.com.crediya.pragma.solicitudes.r2dbc.helper.ReactiveAdapterOperations;
import co.com.crediya.pragma.solicitudes.r2dbc.mapper.SolicitudPageMapper;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

@Slf4j
@Repository
public class SolicitudReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Solicitud,
        SolicitudEntity,
        Long,
        SolicitudReactiveRepository
> implements SolicitudRepository {

    private final TransactionalOperator transactionalOperator;
    private final SolicitudPageMapper  solicitudPageMapper;

    public SolicitudReactiveRepositoryAdapter(SolicitudReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator, SolicitudPageMapper  solicitudPageMapper) {

        super(repository, mapper, d -> mapper.map(d, Solicitud.class));
        this.transactionalOperator = transactionalOperator;
        this.solicitudPageMapper = solicitudPageMapper;
    }

    @Override
    public Mono<Solicitud> saveSolicitud(Solicitud solicitud) {
        return super.save(solicitud)
                .doOnSuccess(s -> log.info("Guardado de solicitud ok"))
                .doOnError(e -> log.warn("Guardado de solicitud ha fallado", e.toString()))
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<SolicitudPage<SolicitudFieldsPage>> page(SolicitudPageRequest req) {

        // 1) inputs
        int size  = Math.min(Math.max(1, req.getSize() == null ? 50 : req.getSize()), 200);
        int page  = Math.max(0, req.getPage() == null ? 0 : req.getPage());
        long offset = (long) page * size;

        // sort solo decide si llamamos a ASC o DESC (columna fija id_solicitud)
        boolean desc = "DESC".equalsIgnoreCase(req.getSort());
        String sortLabel = "id_solicitud " + (desc ? "DESC" : "ASC");

        // 2) Normaliza filtros
        // query: "*" => "%" , otro => "%texto%"
        String q = req.getQuery();
        q = (q == null || q.isBlank() || "*".equals(q)) ? "%" : "%" + q.trim() + "%";

        // estadoNombre: tomamos el primer estado si viene lista
        String estado = null;
        if (req.getStatus() != null && !req.getStatus().isEmpty()) {
            estado = req.getStatus().get(0);
        }

        // 3) Ejecuta la página + total
        Flux<SolicitudFieldsDto> pageFlux =
                desc
                        ? repository.solicitudPageDESC(estado, q, size, offset)
                        : repository.solicitudPageASC (estado, q, size, offset);

        Mono<List<SolicitudFieldsPage>> itemsMono = pageFlux.map(solicitudPageMapper::toModel).collectList();
        Mono<Long> totalMono = repository.countResumen(estado, q);

        // 4) Combina y arma SolicitudPage
        return Mono.zip(itemsMono, totalMono)
                .map(t -> {
                    var items = t.getT1();
                    var total = t.getT2();
                    boolean hasNext = (offset + items.size()) < total;

                    return new SolicitudPage<>(
                            items,
                            total,
                            size,
                            page,
                            hasNext,
                            sortLabel
                    );
                });
    }
}
