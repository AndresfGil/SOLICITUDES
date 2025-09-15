package co.com.crediya.pragma.solicitudes.r2dbc;

import co.com.crediya.pragma.solicitudes.r2dbc.dto.PrestamoAprobadoDto;
import co.com.crediya.pragma.solicitudes.r2dbc.dto.SolicitudFieldsDto;
import co.com.crediya.pragma.solicitudes.r2dbc.entities.SolicitudEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface SolicitudReactiveRepository extends ReactiveCrudRepository<SolicitudEntity, Long>, ReactiveQueryByExampleExecutor<SolicitudEntity> {


    @Query("""
    SELECT s.monto, s.plazo, s.email,
           tp.nombre as tipo_prestamo,
           e.nombre_estado as estado
    FROM solicitudes s
    JOIN tipo_prestamos tp ON tp.id_tipo_prestamo = s.id_tipo_prestamo
    JOIN estados e ON e.id_estado = s.id_estado
    WHERE (:estado IS NULL OR e.nombre_estado = :estado)
      AND (:q IS NULL OR LOWER(tp.nombre) LIKE LOWER(:q))
    ORDER BY s.id_solicitud ASC
    LIMIT :size OFFSET :offset
""")
    Flux<SolicitudFieldsDto> solicitudPageASC(
            @Param("estado") String estado,
            @Param("q") String q,
            @Param("size") int size,
            @Param("offset") long offset
    );



    @Query("""
        SELECT s.monto, s.plazo, s.email,
               tp.nombre AS tipo_prestamo,
               e.nombre_estado AS estado
        FROM solicitudes s
        JOIN tipo_prestamos tp ON tp.id_tipo_prestamo = s.id_tipo_prestamo
        JOIN estados e ON e.id_estado = s.id_estado
        WHERE (:estado IS NULL OR e.nombre_estado = :estado)
          AND (:q IS NULL OR LOWER(tp.nombre) LIKE LOWER(:q))
        ORDER BY s.id_solicitud DESC
        LIMIT :size OFFSET :offset
""")
    Flux<SolicitudFieldsDto> solicitudPageDESC(
            @Param("estado") String estado,
            @Param("q") String q,
            @Param("size") int size,
            @Param("offset") long offset
    );


    // COUNT
    @Query("""
        SELECT COUNT(*)
        FROM solicitudes s
        JOIN tipo_prestamos tp ON tp.id_tipo_prestamo = s.id_tipo_prestamo
        JOIN estados e ON e.id_estado = s.id_estado
        WHERE (:estado IS NULL OR e.nombre_estado = :estado)
          AND (:q IS NULL OR LOWER(tp.nombre) LIKE LOWER(:q))
    """)
    Mono<Long> countResumen(@Param("estado") String estado,
                            @Param("q") String q);


    @Query("""
        SELECT s.monto, s.plazo, tp.tasa_interes
        FROM solicitudes s
        INNER JOIN tipo_prestamos tp ON tp.id_tipo_prestamo = s.id_tipo_prestamo
        WHERE LOWER(s.email) = LOWER(:email) AND s.id_estado = 2
        ORDER BY s.id_solicitud ASC
    """)
    Flux<PrestamoAprobadoDto> findAprobadosByEmail(@Param("email") String email);

}
