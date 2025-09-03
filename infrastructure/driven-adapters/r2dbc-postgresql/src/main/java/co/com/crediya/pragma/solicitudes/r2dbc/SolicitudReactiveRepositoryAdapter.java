package co.com.crediya.pragma.solicitudes.r2dbc;
import co.com.crediya.pragma.solicitudes.model.solicitud.Solicitud;
import co.com.crediya.pragma.solicitudes.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.pragma.solicitudes.r2dbc.entities.SolicitudEntity;
import co.com.crediya.pragma.solicitudes.r2dbc.entities.SolicitudEnriquecida;
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
import org.springframework.r2dbc.core.DatabaseClient;

@Slf4j
@Repository
public class SolicitudReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Solicitud,
        SolicitudEntity,
        Long,
        SolicitudReactiveRepository
> implements SolicitudRepository {

    private final TransactionalOperator transactionalOperator;
    private final DatabaseClient databaseClient;


    public SolicitudReactiveRepositoryAdapter(SolicitudReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator, DatabaseClient databaseClient) {

        super(repository, mapper, d -> mapper.map(d, Solicitud.class));
        this.transactionalOperator = transactionalOperator;
        this.databaseClient = databaseClient;
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
        
        Flux<SolicitudEnriquecida> solicitudesEnriquecidas = solicitudes.flatMap(this::enriquecerSolicitud);
        
        return Mono.zip(totalElements, solicitudesEnriquecidas.collectList())
            .map(tuple -> {
                long total = tuple.getT1();
                int totalPages = (int) Math.ceil((double) total / size);
                
                Flux<Solicitud> content = Flux.fromIterable(tuple.getT2());
                
                return new SolicitudRepository.PaginatedResult<>(
                    content,
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

    private Mono<SolicitudEnriquecida> enriquecerSolicitud(Solicitud solicitud) {
        if (solicitud.getIdTipoPrestamo() == null && solicitud.getIdEstado() == null) {
            return Mono.just(new SolicitudEnriquecida(solicitud));
        }
        
        Mono<Object[]> datosTipoPrestamo = solicitud.getIdTipoPrestamo() != null ?
            databaseClient.sql("SELECT nombre, tasa_interes FROM tipo_prestamos WHERE id_tipo_prestamo = ?")
                .bind(0, solicitud.getIdTipoPrestamo())
                .map(row -> new Object[]{
                    row.get("nombre", String.class),
                    row.get("tasa_interes", Integer.class)
                })
                .one()
                .defaultIfEmpty(new Object[]{"", 0}) :
            Mono.just(new Object[]{"", 0});
        
        Mono<String> nombreEstado = solicitud.getIdEstado() != null ?
            databaseClient.sql("SELECT nombre_estado FROM estados WHERE id_estado = ?")
                .bind(0, solicitud.getIdEstado())
                .map(row -> row.get("nombre_estado", String.class))
                .one()
                .defaultIfEmpty("") :
            Mono.just("");
        
        return Mono.zip(datosTipoPrestamo, nombreEstado)
            .map(tuple -> {
                Object[] datosPrestamo = tuple.getT1();
                String nombre = (String) datosPrestamo[0];
                Integer tasaInteres = (Integer) datosPrestamo[1];
                
                SolicitudEnriquecida solicitudEnriquecida = new SolicitudEnriquecida(solicitud);
                solicitudEnriquecida.setNombreTipoPrestamo(nombre);
                solicitudEnriquecida.setEstadoSolicitud(tuple.getT2());
                solicitudEnriquecida.setTasaInteres(tasaInteres);
                return solicitudEnriquecida;
            });
    }
}
