package co.com.crediya.pragma.solicitudes.r2dbc;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.r2dbc.entities.SolicitudEntity;
import co.com.crediya.pragma.solicitudes.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Mono<SolicitudRepository.PaginatedResult<Solicitud>> findAllSolicitudes(
            int page, int size, String sortBy, String sortDirection) {
        
        Sort sort = Sort.by(
            "ASC".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC,
            sortBy != null ? sortBy : "idSolicitud"
        );
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Mono<Long> totalElements = repository.count();
        
        Flux<SolicitudEntity> entities = repository.findAllBy(pageable);
        Flux<Solicitud> solicitudes = entities.map(this::toEntity);
        
        return Mono.zip(totalElements, solicitudes.collectList())
            .map(tuple -> {
                long total = tuple.getT1();
                int totalPages = (int) Math.ceil((double) total / size);
                
                return new SolicitudRepository.PaginatedResult<>(
                    solicitudes,
                    total,
                    totalPages,
                    page,
                    size,
                    page < totalPages - 1,
                    page > 0
                );
            })
            .doOnSuccess(result -> log.info("Paginación de solicitudes exitosa: página {}, tamaño {}, total: {}", 
                page, size, result.totalElements()))
            .doOnError(e -> log.warn("Error en paginación de solicitudes: {}", e.getMessage()));
    }
}
