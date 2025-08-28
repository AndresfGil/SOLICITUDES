package co.com.crediya.pragma.solicitudes.r2dbc;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.r2dbc.entities.SolicitudEntity;
import co.com.crediya.pragma.solicitudes.r2dbc.helper.ReactiveAdapterOperations;
import co.com.crediya.pragma.solicitudes.r2dbc.helper.ReactiveLogger;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class SolicitudReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Solicitud,
        SolicitudEntity,
        Long,
        SolicitudReactiveRepository
> implements SolicitudRepository {

    private final TransactionalOperator transactionalOperator;


    public SolicitudReactiveRepositoryAdapter(SolicitudReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {

        super(repository, mapper, d -> mapper.map(d, Solicitud.class));
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<Solicitud> saveSolicitud(Solicitud solicitud) {
        return super.save(solicitud)
                .doOnSuccess(s -> log.info("Guardado de solicitud ok"))
                .doOnError(e -> log.warn("Guardado de solicitud ha fallado", e.toString()))
                .as(transactionalOperator::transactional);
    }

    @Override
    public Flux<Solicitud> findAllSolicitudes() {
        return ReactiveLogger.logFlux(super.findAll(), "Traer todas las solicitudes");
    }

    @Override
    public Mono<Solicitud> findSolicitudById(Long id) {
        return super.findById(id);
    }
}
